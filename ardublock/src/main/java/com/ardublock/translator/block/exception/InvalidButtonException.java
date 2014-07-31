package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.SocketNullException;

public class InvalidButtonException extends SocketNullException
{

	/**
	 * 
	 */
	private Long blockId;
	
	
	public InvalidButtonException(Long blockId)
	{
		super(blockId);
	}
	
	public Long getBlockId()
	{
		return blockId;
	}

}
