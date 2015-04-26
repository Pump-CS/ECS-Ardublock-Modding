package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidButtonException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

import javax.swing.JOptionPane;

public class ECSButtonPressedBlock extends TranslatorBlock
{

	private static final int DEFAULT_BUTTON = 1;

	public ECSButtonPressedBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String number  = translatorBlock.toCode();


		if (translatorBlock instanceof NumberBlock) {
			// Make sure the given int is between 1 and 4
			if (Integer.parseInt(number.trim()) == 1) {
				number = BUTTON_PIN_1;
			} else if (Integer.parseInt(number.trim()) == 2) {
				number = BUTTON_PIN_2;
			} else if (Integer.parseInt(number.trim()) == 3) {
				number = BUTTON_PIN_3;
			} else if (Integer.parseInt(number.trim()) == 4) {
				number = BUTTON_PIN_4;
			} else {
				 throw new InvalidButtonException(blockId);
			}
		} else if (translatorBlock instanceof VariableNumberBlock) {
			number = String.format("(((%s >= 1) && (%s <= 4)) ? (%s) : (%d))", number, number, number, DEFAULT_BUTTON);
			JOptionPane.showMessageDialog(null, "You have used a variable as an input to a 'Button Pressed' block instead of a constant integer. This is okay, but note that if the value of the variable is not 1, 2, 3, or 4, then the value will default to 1."
					, "Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			// Error: given block must be either NumberBlock or VariableNumberBlock
			throw new InvalidButtonException(blockId);
		}


		// Add all button pins as input pins.
		// This will cause problems if somebody wants to use this software without 
		// the MU ECS shield and wishes to use these pins as output instead.
		translator.addInputPin(BUTTON_PIN_1);
		translator.addInputPin(BUTTON_PIN_2);
		translator.addInputPin(BUTTON_PIN_3);
		translator.addInputPin(BUTTON_PIN_4);
	
/*
		int pinAdjust = 0;
		number = translatorBlock.toCode();
		if (Integer.parseInt(number.trim()) == 1) {
			pinAdjust = Integer.parseInt(BUTTON_PIN_1);
		} else if (Integer.parseInt(number.trim()) == 2) {
			pinAdjust = Integer.parseInt(BUTTON_PIN_2);
		} else if (Integer.parseInt(number.trim()) == 3) {
			pinAdjust = Integer.parseInt(BUTTON_PIN_3);
		} else if (Integer.parseInt(number.trim()) == 4) {
			pinAdjust = Integer.parseInt(BUTTON_PIN_4);
		} else {
			throw new InvalidButtonException(blockId);
		}
*/
		//number = "" + pinAdjust;
		//translator.addInputPin(number);
		
		String ret = "digitalRead(";
		ret = ret + number;
		ret = ret + ")";
		return codePrefix + ret + codeSuffix;
	}
}
