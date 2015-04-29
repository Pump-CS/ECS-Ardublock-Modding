package com.ardublock.translator.block;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class SetterVariableNumberDoubleBlock extends TranslatorBlock
{
  private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
  
  public SetterVariableNumberDoubleBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
  {
    super(blockId, translator, codePrefix, codeSuffix, label);
  }

  @Override
  public String toCode() throws ArdublockException
  {
	VariableNumberDoubleBlock db;
	String varName;
	String internalVarName;
	String ret;	

    TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
    if (!(tb instanceof VariableNumberDoubleBlock)) {
      throw new BlockException(blockId, uiMessageBundle.getString("ardublock.error_msg.double_number_var_slot"));
    }

	// Mark this variable number block as being inside of a setter so that it doesn't complain
	// about not existing yet.
	db = (VariableNumberDoubleBlock)tb;
	db.setIsInSetter(true);

	varName = db.toCode();

	// If we haven't seen this variable yet, add it to the list of valid variable names
	internalVarName = translator.getNumberVariable(varName);
	if (translator.getNumberVariable(varName) == null) {
		internalVarName = translator.buildVariableName(varName);
		translator.addNumberVariable(varName, internalVarName);
		translator.addDefinitionCommand("double " + internalVarName + " = 0.0;");
	}

	ret = internalVarName;
    tb = this.getRequiredTranslatorBlockAtSocket(1);
    ret = ret + " = " + tb.toCode() + " ;\n";
    return ret;
  }

}
