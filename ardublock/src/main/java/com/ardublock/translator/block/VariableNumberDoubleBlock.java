package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.InvalidNumberDoubleVariableNameException;

public class VariableNumberDoubleBlock extends TranslatorBlock
{

	private boolean isInSetter;

  	public VariableNumberDoubleBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
  	{
   	 	super(blockId, translator, codePrefix, codeSuffix, label);
  	}

  	@Override
 	public String toCode() throws InvalidNumberDoubleVariableNameException
	{
		String internalVariableName;

		// If this block has been marked as being in a variable setter, then we 
		// don't need to check that the variable is valid since we are creating it here.
		if (this.isInSetter) {
			return codePrefix + label + codeSuffix;
		}

		internalVariableName = translator.getNumberVariable(label);
		if (internalVariableName == null) {
			throw new InvalidNumberDoubleVariableNameException(blockId, label);
		}

		return codePrefix + internalVariableName + codeSuffix;
    }

	public void setIsInSetter(boolean val) {
		this.isInSetter = val;
	}
}
