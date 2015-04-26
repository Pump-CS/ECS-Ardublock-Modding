package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class DelayMicrosecondsBlock extends TranslatorBlock
{
	public DelayMicrosecondsBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		String ret = "\tdelayMicroseconds( " + tb.toCode() + " );\n";
		return ret;
	}

}
