package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidKeyException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

/* This block calculates the appropriate index into the global
	boolean keysDown array and inserts code to access that element. 

	This block is used to check if a particular key is currently being pressed. */

public class ECSKeyPressed extends TranslatorBlock
{
	public ECSKeyPressed(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock key = this.getRequiredTranslatorBlockAtSocket(0);


		String message = key.toCode();
		message = message.replace("\"", ""); // Strip annoying quotes that are placed around the message

		// It does not make sense to check if the 'abc' key is being pressed. */
		if (message.length() != 1)
		{
			throw new InvalidKeyException(blockId);
		}

		char c = message.charAt(0);
		if (Character.isDigit(c))
		{
			return ECSKeyboardSetup.KEYS_ARRAY + "[" +  (int)(c - '0') + "]";
		}

		if (Character.isLetter(c) && Character.isLowerCase(c))
		{
			return ECSKeyboardSetup.KEYS_ARRAY + "[" + (int)((c - 'a') + 10) + "]";
		}

		// The given character was not a digit or a lowercase letter
		throw new InvalidKeyException(blockId);
	}
}
