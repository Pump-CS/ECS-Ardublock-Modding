package com.ardublock.translator.block.exception;

//import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.block.exception.SocketNullException;

public class InvalidPinException extends SocketNullException//ArdublockException
{

	/**
	 * 
	 */
	private Long blockId;
	
	
	public InvalidPinException(Long blockId)
	{
		super(blockId);
//		this.blockId = blockId;
	}
	
	public Long getBlockId()
	{
		return blockId;
	}

}
