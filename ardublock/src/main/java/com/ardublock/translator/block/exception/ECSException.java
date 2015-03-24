package com.ardublock.translator.block.exception;

import com.ardublock.core.exception.ArdublockException;

public class ECSException extends ArdublockException
{
	private Long blockId;
	
	public ECSException(Long blockId) {
		this.blockId = blockId;
	}

	public Long getBlockId() {
		return blockId;
	}
}
