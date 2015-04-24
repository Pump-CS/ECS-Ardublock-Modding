package com.ardublock.translator.block;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

import java.util.ArrayList;

public class SetterVariableNumberBlock extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public SetterVariableNumberBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		VariableNumberBlock nb;
		String varName;
		String internalVarName;
		String ret;
		String val;


		// Protect against the case of setting a variable equal to itself without having created the variable to begin with.
		// Essentially protect against: int x = x;
		// This is done by calling toCode() on the value socket before adding the variable name to the list of valid variable names.
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(1);
		val = tb.toCode();

		tb = this.getRequiredTranslatorBlockAtSocket(0);
		if (!(tb instanceof VariableNumberBlock)) {
			throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.number_var_slot"));
		}

		// Mark this variable number block as being inside of a setter so that it doesn't complain
		// about not existing yet.
		nb = (VariableNumberBlock)tb;
		nb.setIsInSetter(true);
		
		varName = nb.toCode();
		internalVarName = translator.buildVariableName(varName);
		
		// If we haven't seen this variable yet, add it to the list of valid variable names
		if (translator.getNumberVariable(varName) == null) {
			translator.addNumberVariable(varName, internalVarName);
			translator.addDefinitionCommand("int " + internalVarName + " = 0;");
		}

		ret = internalVarName;
		ret = ret + " = " + val + " ;\n";

		return ret;
	}

}
