package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.*;

public class ECSUpdateKeyboard extends TranslatorBlock
{
	public ECSUpdateKeyboard(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret = "";
		translator.addDefinitionCommand("int ECSval;\nint ECSnumAvailable;\nint ECSiteration;\n");
		ret += "ECSnumAvailable = Serial.available();\n";
		ret += "char buffer[ECSnumAvailable];";
		ret += "Serial.readBytes(buffer, ECSnumAvailable);";
		ret += "for (ECSiteration = 0; ECSiteration < ( ECSnumAvailable ); ++ECSiteration)\n";
		ret += "{\n\t";
		ret += "ECSval = buffer[ECSiteration];";
		ret += "keysDown[ECSval] = !keysDown[ECSval];\n";
		ret += "Serial.print(ECSval);";
		ret += "Serial.println();";
		ret += "}\n";
		return ret;
	}
}
