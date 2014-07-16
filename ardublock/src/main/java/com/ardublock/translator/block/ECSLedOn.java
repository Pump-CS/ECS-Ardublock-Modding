package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.DigitalOutputBlock;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSLedOn extends TranslatorBlock
{
	public ECSLedOn(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock pinNumber = this.getRequiredTranslatorBlockAtSocket(0);
		
		translator.addSetupCommand("pinMode( " + pinNumber.toCode() + " , OUTPUT);");
		return "digitalWrite( " + pinNumber.toCode() + " , HIGH);\n";
	}
}