package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidBooleanVariableNameException extends ECSException
{
	private String varName;
	
	public InvalidBooleanVariableNameException(Long blockId, String varName)
	{
		super(blockId);
		this.varName = varName;
	}

	public String getMessage() {
		return varName;
	}

}
