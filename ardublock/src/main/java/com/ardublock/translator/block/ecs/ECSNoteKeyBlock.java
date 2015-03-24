package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

/*
	This block represents the Note/Key "datatype". It behaves just like a message block does
	except that there is no socket on the right side of the block to which to concatenate additional
	messages. There is no input filtering to be done in this block. 
*/

public class ECSNoteKeyBlock extends TranslatorBlock
{
	public ECSNoteKeyBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		// enclose label in quotes to match behavior of MessageBlock.
		// ECSPlayNoteBlock and ECSPlayNotTimeBlock expect this behavior and 
		// we would like to be able to use message and note interchangeably.
		return "\"" + label + "\"";
	}
}
