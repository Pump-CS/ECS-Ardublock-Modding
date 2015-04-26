package com.ardublock.translator.block.exception;

//import com.ardublock.translator.block.exception.ECSException;
import com.ardublock.translator.block.exception.ECSException;

public class InvalidPinException extends ECSException//ArdublockException
{

	public InvalidPinException(Long blockId)
	{
		super(blockId);
	}
}
