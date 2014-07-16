package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class PlayFrequencyBlock extends TranslatorBlock
{
	public PlayFrequencyBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		//TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(1);
		int CONST_PIN = 8;
		String ret = "tone(" + CONST_PIN /*pinBlock.toCode()*/ + ", " + freqBlock.toCode() + ", " + timeBlock.toCode() + ");\n";
		return ret;
	}
}
