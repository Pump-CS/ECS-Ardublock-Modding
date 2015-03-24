package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class SubroutineRefBlock extends TranslatorBlock
{

	public SubroutineRefBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		String subroutineName = label.trim();
		String subroutineInternalName;
		if (!translator.containFunctionName(subroutineName))
		{
			throw new SubroutineNotDeclaredException(blockId, subroutineName);
		}
		subroutineInternalName = translator.getInternalFunctionName(subroutineName);
		return "\t"+ subroutineInternalName + "();\n";
	}

}
