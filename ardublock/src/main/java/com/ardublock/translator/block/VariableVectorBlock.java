package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.block.exception.InvalidArrayVariableNameAccessException;

public class VariableVectorBlock extends TranslatorBlock
{
	public VariableVectorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		String internalVarName;
		String ret;
        TranslatorBlock position = this.getRequiredTranslatorBlockAtSocket(0);

		internalVarName = translator.getArrayVariable(label);

		if (internalVarName == null) {
			throw new InvalidArrayVariableNameAccessException(blockId, label);
		}

		ret = internalVarName + "[" + position.toCode() + " - 1]";
		return codePrefix + ret + codeSuffix;
		
	}

}
