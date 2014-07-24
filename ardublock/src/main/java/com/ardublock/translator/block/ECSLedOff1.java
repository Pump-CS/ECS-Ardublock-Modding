package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.DigitalOutputBlock;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSLedOff1 extends TranslatorBlock
{
	public ECSLedOff1(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		int CONST_PIN = 10;

		// Add a line to the setup section that sets this pin as output
		translator.addSetupCommand("pinMode( " + CONST_PIN + " , OUTPUT);");
		
		return "digitalWrite( " + CONST_PIN + " , LOW);\n";
	}
}
