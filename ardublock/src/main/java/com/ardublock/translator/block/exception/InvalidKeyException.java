package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.SocketNullException;

public class InvalidKeyException extends SocketNullException
{

	/**
	 * 
	 */
	private Long blockId;
	
	
	public InvalidKeyException(Long blockId)
	{
		super(blockId);
	}
	
	public Long getBlockId()
	{
		return blockId;
	}

}
