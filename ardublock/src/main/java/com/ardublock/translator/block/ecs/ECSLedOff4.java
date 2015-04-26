package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.DigitalOutputBlock;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ECSLedOff4 extends TranslatorBlock
{
	public ECSLedOff4(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		// Add a line to the setup section that sets this pin as output
		translator.addOutputPin(LED_PIN_4);
		
		return "digitalWrite( " + LED_PIN_4 + " , LOW);\n";
	}
}
