package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.*;

public class ECSKeyPressed extends TranslatorBlock
{
	public ECSKeyPressed(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		/* TODO: throw exception that Matt wrote for invalid keys */
		TranslatorBlock key = this.getRequiredTranslatorBlockAtSocket(0);
		return ECSKeyboardSetup.KEYS_ARRAY + "[" +  key.toCode() + "]";
	}
}
