package com.ardublock.translator.block;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import java.util.ArrayList;

public class SetterVariableNumberBlock extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public SetterVariableNumberBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		if (!(tb instanceof VariableNumberBlock)) {
			throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.number_var_slot"));
		}

		VariableNumberBlock nb = (VariableNumberBlock)tb;
		nb.setIsInSetter(true);
		
		String varName = nb.toCode();
		System.out.println("in setter, varName: " + varName);
		String internalVarName = translator.buildVariableName(varName);
		// If we haven't seen this variable yet, add it to the list of valid variable names
		if (translator.getNumberVariable(varName) == null) {
			translator.addNumberVariable(varName, internalVarName);
			translator.addDefinitionCommand("int " + internalVarName + " = 0;");
			System.out.println("Added (" + varName + ":" + internalVarName + ") to list of valid vars.");
		}

		String ret = internalVarName;
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret = ret + " = " + tb.toCode() + " ;\n";

		return ret;
	}

}
