package com.ardublock.translator.block;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class SetterVariableDigitalBlock extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public SetterVariableDigitalBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		VariableDigitalBlock db;
		String varName;
		String internalVarName;
		String ret;

		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		if (!(tb instanceof VariableDigitalBlock)) {
			throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.digital_var_slot"));
		}

		// Mark this variable digital block as being inside of a setter so that it doesn't complain
		// about not existing yet.
		db = (VariableDigitalBlock)tb;
		db.setIsInSetter(true);

		varName = db.toCode();
		internalVarName = translator.buildVariableName(varName);

		// If we haven't seen this variable yet, add it to the list of valid variable names
		if (translator.getBooleanVariable(varName) == null) {
			translator.addBooleanVariable(varName, internalVarName);
			translator.addDefinitionCommand("bool " + internalVarName + " = false;");
		}
		
		ret = internalVarName;
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret = ret + " = " + tb.toCode() + " ;\n";
		return ret;
	}

}
