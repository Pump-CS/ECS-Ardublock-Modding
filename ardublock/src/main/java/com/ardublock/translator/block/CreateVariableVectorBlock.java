package com.ardublock.translator.block;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.block.exception.InvalidArrayVariableNameCreateException;

public class CreateVariableVectorBlock extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public CreateVariableVectorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}
	
	@Override
	public String toCode() throws ArdublockException
	{
		String varName;
		String internalVarName;
		String ret;

		TranslatorBlock name = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock size = this.getRequiredTranslatorBlockAtSocket(1);
		if (!(name instanceof VariableFakeBlock)) {
			throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.array_var_slot"));
		}
		if (!(size instanceof NumberBlock)) {
			throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.array_size_slot"));
		}

		varName = name.toCode();
		internalVarName = translator.buildVariableName(varName);
		internalVarName = "vec_" + internalVarName;

		// If we have not seen this array variable name before, then we are free to create it.
		// Otherwise throw an error for declaring two arrays with the same name.
		if (translator.getArrayVariable(varName) == null) {
			String s = size.toCode();
			translator.addArrayVariable(varName, internalVarName);
			translator.addDefinitionCommand("int " + internalVarName + "[" + s + "];");
			translator.addSetupCommand("for (int i = 0; i < " + s + "; i++) {" + internalVarName + "[i] = 0;}");
		} else {
			throw new InvalidArrayVariableNameCreateException(blockId, varName);
		}

		return "";
	}

}
