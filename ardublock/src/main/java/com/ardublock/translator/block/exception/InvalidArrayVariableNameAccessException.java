package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidArrayVariableNameAccessException extends ECSException
{
	private String varName;
	
	public InvalidArrayVariableNameAccessException(Long blockId, String varName)
	{
		super(blockId);
		this.varName = varName;
	}
	
	public String getMessage() {
		return varName;
	}

}
