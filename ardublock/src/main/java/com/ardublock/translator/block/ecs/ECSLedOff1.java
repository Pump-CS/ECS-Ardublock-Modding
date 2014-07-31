package com.ardublock.translator.block.ecs;

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
		// Add a line to the setup section that sets this pin as output
		translator.addSetupCommand("pinMode( " + LED_PIN_1 + " , OUTPUT);");
		
		return "digitalWrite( " + LED_PIN_1 + " , LOW);\n";
	}
}
