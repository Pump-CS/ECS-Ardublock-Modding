package com.ardublock.translator.block.exception;

import com.ardublock.translator.block.exception.ECSException;

public class InvalidNoteException extends ECSException
{
	public InvalidNoteException(Long blockId)
	{
		super(blockId);
	}
}
