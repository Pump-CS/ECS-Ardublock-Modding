package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ECSPlayFrequencyTimeBlock extends TranslatorBlock
{
	public ECSPlayFrequencyTimeBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(1);

		String ret = "tone(" + SPEAKER_PIN + ", " + freqBlock.toCode() + ", " + timeBlock.toCode() + ");\n";
		ret += "delay(" + timeBlock.toCode() + ");\n";
		ret += "noTone(" + SPEAKER_PIN + ");\n";
		return ret;
	}
}
