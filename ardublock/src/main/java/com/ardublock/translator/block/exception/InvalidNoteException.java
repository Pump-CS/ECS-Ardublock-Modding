package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.SocketNullException;

public class InvalidNoteException extends SocketNullException
{

	/**
	 * 
	 */
	private Long blockId;
	
	
	public InvalidNoteException(Long blockId)
	{
		super(blockId);
	}
	
	public Long getBlockId()
	{
		return blockId;
	}

}
