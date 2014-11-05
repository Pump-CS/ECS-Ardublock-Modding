package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSNoteKeyBlock extends TranslatorBlock
{
	public ECSNoteKeyBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		// enclose label in quotes to match behavior of MessageBlock.
		// ECSPlayNoteBlock and ECSPlayNotTimeBlock expect this behavior and 
		// we would like to be able to use message and note interchangeably.
		return "\"" + label + "\"";
	}
}
