package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidPinException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ToneTimeBlock extends TranslatorBlock
{
	public ToneTimeBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(1);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(2);
		
		if (!(pinBlock.toCode().equals(SPEAKER_PIN)) && !(pinBlock.toCode().equals(FREE_PIN_1))
			&& !(pinBlock.toCode().equals(FREE_PIN_2)) && !(pinBlock.toCode().equals(FREE_PIN_3))
			&& !(pinBlock.toCode().equals(FREE_PIN_4)) && !(pinBlock.toCode().equals(FREE_PIN_5))
			&& !(pinBlock.toCode().equals(FREE_PIN_6))) {
			throw new InvalidPinException(blockId);
		}

		String ret = "tone(" + pinBlock.toCode() + ", " + freqBlock.toCode() + ", " + timeBlock.toCode() + ");\n";
		ret += "\tdelay(" + timeBlock.toCode() + ");\n"; 
		ret += "\tnoTone(" + pinBlock.toCode() + ");\n";
		return ret;
	}
}
