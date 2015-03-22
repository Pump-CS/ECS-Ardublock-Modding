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
		// If this block has been marked as being in a variable setter, then we 
		// don't need to check that the variable is valid since we are creating it here
		if (this.isInSetter) {
			return codePrefix + label + codeSuffix;
		}

		String internalVariableName = translator.getNumberVariable(label);
		if (internalVariableName == null)
		{
			System.out.println("invalid var name");
			throw new InvalidNumberVariableNameException(blockId, label);
			//internalVariableName = translator.buildVariableName(label);
			//translator.addNumberVariable(label, internalVariableName);
			//translator.addDefinitionCommand("int " + internalVariableName + " = 0 ;");
//			translator.addSetupCommand(internalVariableName + " = 0;");
		}
		return codePrefix + internalVariableName + codeSuffix;
	}

	public void setIsInSetter(boolean val) {
		this.isInSetter = val;
	}

}
