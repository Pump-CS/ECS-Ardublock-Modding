package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.*;
import com.ardublock.core.exception.ArdublockException;

public class ECSUpdateKeyboard extends TranslatorBlock
{
	public ECSUpdateKeyboard(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		String ret = "";

		/* Declare required variables.
		*	int EVSindex: index into the keysDown array (received as byte from serial port)
		*	int ECSnumAvailable: number of bytes available on the serial port
		*	int ECSiteration: iteration variable used in for loop that reads available serial port data
		*/
		translator.addDefinitionCommand("int ECSindex;\nint ECSnumAvailable;\nint ECSiteration;\n");

		/* Get number of bytes available on the serial port */
		ret += "ECSnumAvailable = Serial.available();\n";

		/* Allocate an array to store ECSnumAvailable bytes */
		ret += "char buffer[ECSnumAvailable];";

		/* Read data from serial port */
		ret += "Serial.readBytes(buffer, ECSnumAvailable);";

		/* Loop through buffer and toggle the appropriate key in keysDown array */
		ret += "for (ECSiteration = 0; ECSiteration < ( ECSnumAvailable ); ++ECSiteration)\n";
		ret += "{\n\t";
		ret += 		"ECSindex = buffer[ECSiteration];";
		ret +=      "digitalWrite( 5 , HIGH);";
		ret += 		ECSKeyboardSetup.KEYS_ARRAY + "[ECSindex] = !" + ECSKeyboardSetup.KEYS_ARRAY + "[ECSindex];\n";
		ret += "}\n";
		return ret;
	}
}
