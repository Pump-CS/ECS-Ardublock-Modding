package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidKeyException extends ECSException
{
	public InvalidKeyException(Long blockId)
	{
		super(blockId);
	}
}
