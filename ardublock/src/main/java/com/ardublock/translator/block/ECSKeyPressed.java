package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidKeyException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSKeyPressed extends TranslatorBlock
{
	public ECSKeyPressed(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		/* TODO: throw exception that Matt wrote for invalid keys */
		TranslatorBlock key = this.getRequiredTranslatorBlockAtSocket(0);

		String message = key.toCode();
		System.out.println(message);
		message = message.replace("\"", "");
		System.out.println(message);

		if (message.length() != 1)
		{
			System.out.println("length");
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

		throw new InvalidKeyException(blockId);
	}
}
