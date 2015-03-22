package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.SocketNullException;

public class InvalidBooleanVariableNameException extends SocketNullException
{
	private Long blockId;
	private String varName;
	
	public InvalidBooleanVariableNameException(Long blockId, String varName)
	{
		super(blockId);
		this.varName = varName;
	}
	
	public Long getBlockId()
	{
		return blockId;
	}

	public String getMessage() {
		return varName;
	}

}
