package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidButtonException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ECSButtonPressedBlock extends TranslatorBlock
{
	public ECSButtonPressedBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
	
		int pinAdjust = 0;
		String number = translatorBlock.toCode();
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

		number = "" + pinAdjust;
		translator.addInputPin(number);
		
		String ret = "digitalRead(";
		ret = ret + number;
		ret = ret + ")";
		return codePrefix + ret + codeSuffix;
	}
}
