package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSPlayFrequencyBlock extends TranslatorBlock
{
	public ECSPlayFrequencyBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);

		String ret = "tone(" + SPEAKER_PIN + ", " + freqBlock.toCode() + ");\n";
		return ret;
	}
}
