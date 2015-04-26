package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.InvalidNumberVariableNameException;

public class VariableNumberBlock extends TranslatorBlock
{
	private boolean isInSetter;

	public VariableNumberBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.isInSetter = false;
	}

	@Override
	public String toCode() throws InvalidNumberVariableNameException
	{
		String internalVariableName;
	
		// If this block has been marked as being in a variable setter, then we 
		// don't need to check that the variable is valid since we are creating it here
		if (this.isInSetter) {
			return codePrefix + label + codeSuffix;
		}

		internalVariableName = translator.getNumberVariable(label);
		if (internalVariableName == null)
		{
			throw new InvalidNumberVariableNameException(blockId, label);
		}

		return codePrefix + internalVariableName + codeSuffix;
	}

	public void setIsInSetter(boolean val) {
		this.isInSetter = val;
	}

}
