package com.ardublock.translator.block;


import java.util.ResourceBundle;
import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.block.exception.InvalidArrayVariableNameAccessException;

public class SetterVariableVectorBlock extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public SetterVariableVectorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
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
		if (!(name instanceof VariableFakeBlock)) {
		      throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.array_var_slot"));
		}

		varName = name.toCode();
		internalVarName = translator.getArrayVariable(varName);

		if (internalVarName == null) {
			throw new InvalidArrayVariableNameAccessException(blockId, varName);
		}

		TranslatorBlock position = this.getRequiredTranslatorBlockAtSocket(1);

		/*if (!((position instanceof NumberBlock) || (position instanceof VariableNumberBlock))) {
			throw new BlockException(blockId, "message");
		}*/

		TranslatorBlock value = this.getRequiredTranslatorBlockAtSocket(2);

		ret = internalVarName + "[" + position.toCode() + " - 1]";
		ret = ret + " = " + value.toCode() + ";\n";
		return ret;
	}

}
