package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.InvalidBooleanVariableNameException;

public class VariableDigitalBlock extends TranslatorBlock
{

	private boolean isInSetter;
	
	public VariableDigitalBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.isInSetter = false;
	}

	@Override
	public String toCode() throws InvalidBooleanVariableNameException
	{
		String internalVariableName;

		// If this block has been marked as bring in a variable setter, then we
		// don't need to check that the variable is valid since we are creating it here
		if (this.isInSetter) {
			return codePrefix + label + codeSuffix;
		}

		internalVariableName = translator.getBooleanVariable(label);
		if (internalVariableName == null)
		{
			throw new InvalidBooleanVariableNameException(blockId, label);
		}
		
		return codePrefix + internalVariableName + codeSuffix;
	}

	public void setIsInSetter(boolean val) {
		this.isInSetter = val;
	}

}
