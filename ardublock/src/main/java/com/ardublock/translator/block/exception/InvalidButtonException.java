package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidButtonException extends ECSException
{

	public InvalidButtonException(Long blockId)
	{
		super(blockId);
	}

}
