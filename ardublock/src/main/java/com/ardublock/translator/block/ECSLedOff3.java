package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.DigitalOutputBlock;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSLedOff3 extends TranslatorBlock
{
	public ECSLedOff3(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		// Add a line to the setup section that sets this pin as output
		translator.addSetupCommand("pinMode( " + LED_PIN_3 + " , OUTPUT);");
		
		return "digitalWrite( " + LED_PIN_3 + " , LOW);\n";
	}
}
