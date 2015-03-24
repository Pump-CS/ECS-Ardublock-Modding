package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.block.exception.SubroutineNameDuplicatedException;

public class SubroutineBlock extends TranslatorBlock
{

	public SubroutineBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		String subroutineName = label.trim();
		String ret;
		String internalName;

		// throws SubroutineNameDuplicatedException
		try {
			translator.addFunctionName(blockId, subroutineName);
		} catch (SubroutineNameDuplicatedException e) {
			// This should be handled somewhere else. It shouldn't let you actually
			// declare a function with a name that's already used
			System.out.println("subroutine name duplicated.");
		}
		internalName = translator.getInternalFunctionName(subroutineName);

		ret = "void " + internalName + "()\n{\n";
		TranslatorBlock translatorBlock = getTranslatorBlockAtSocket(0);
		while (translatorBlock != null)
		{
			ret = ret + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		ret = ret + "}\n\n";
		return ret;
	}
}
