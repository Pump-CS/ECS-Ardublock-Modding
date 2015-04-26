package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.DigitalOutputBlock;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidPinException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ECSLedOff extends TranslatorBlock
{
	public ECSLedOff(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);

		if (!(pinBlock.toCode().equals(LED_PIN_1)) && !(pinBlock.toCode().equals(LED_PIN_2))
			&& !(pinBlock.toCode().equals(LED_PIN_3)) && !(pinBlock.toCode().equals(LED_PIN_4))
			&& !(pinBlock.toCode().equals(FREE_PIN_1)) && !(pinBlock.toCode().equals(FREE_PIN_2))
			&& !(pinBlock.toCode().equals(FREE_PIN_3)) && !(pinBlock.toCode().equals(FREE_PIN_4))
			&& !(pinBlock.toCode().equals(FREE_PIN_5)) && !(pinBlock.toCode().equals(FREE_PIN_6))) {
			throw new InvalidPinException(blockId);
		}

		// Add a line to the setup section that sets this pin as output
		//translator.addSetupCommand("pinMode( " + pinBlock.toCode() + " , OUTPUT);");
		translator.addOutputPin(pinBlock.toCode());		
		
		return "digitalWrite( " + pinBlock.toCode() + " , LOW);\n";
	}
}
