package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSButtonPressedBlock extends TranslatorBlock
{
	public ECSButtonPressedBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
	
		String number;
		number = translatorBlock.toCode();
		int pinAdjust = Integer.parseInt(BUTTON_PIN_1) - 1;
		pinAdjust += Integer.parseInt(number.trim());
		number = "" + pinAdjust;
		translator.addInputPin(number);
		
		String ret = "digitalRead(";
		ret = ret + number;
		ret = ret + ")";
		return codePrefix + ret + codeSuffix;
	}
}
