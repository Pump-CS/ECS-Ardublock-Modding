package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidNumberVariableNameException extends ECSException
{
	private String varName;
	
	public InvalidNumberVariableNameException(Long blockId, String varName)
	{
		super(blockId);
		this.varName = varName;
	}

	public String getMessage() {
		return varName;
	}

}
