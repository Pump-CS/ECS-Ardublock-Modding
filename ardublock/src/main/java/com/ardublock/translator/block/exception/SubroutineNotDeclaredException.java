package com.ardublock.translator.block.exception;

import com.ardublock.core.exception.ArdublockException;

public class SubroutineNotDeclaredException extends ArdublockException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2621233841585294257L;
	
	private Long blockId;
	private String funcName;
	
	public SubroutineNotDeclaredException(Long blockId, String funcName)
	{
		this.blockId = blockId;
		this.funcName = funcName;
	}
	
	public Long getBlockId()
	{
		return this.blockId;
	}

	public String getMessage() {
		return funcName;
	}
}
