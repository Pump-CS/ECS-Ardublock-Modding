package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ECSReadDistanceBlock extends TranslatorBlock
{
	public ECSReadDistanceBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	private final static String ultraSonicFunction = "int getDistance()" +
							 "\n{" +
							 "\n  unsigned long start = micros();" +
							 "\n  int cycles = 4;" +
							 "\n  int delay = 20;" +
							 "\n  int timeout = 8000;" +
							 "\n  int T1OUT = 0x10;" +
							 "\n" +
							 "\n  for (int ii = 0; ii < 4; ii++) {" +
							 "\n    digitalWrite(TRANSMIT_PIN_1, HIGH);" +
							 "\n    digitalWrite(TRANSMIT_PIN_2, LOW);" +
							 "\n    delayMicroseconds(delay);" +
							 "\n    digitalWrite(TRANSMIT_PIN_1, LOW);" +
							 "\n    digitalWrite(TRANSMIT_PIN_2, HIGH);" +
							 "\n    delayMicroseconds(delay);" +
							 "\n  }" +
							 "\n" +
							 "\n  delayMicroseconds(200);" +
							 "\n  while((PIND & T1OUT) != 0 && timeout > 0) {" +
							 "\n    timeout--;" +
							 "\n    delayMicroseconds(1);" +
							 "\n  }" +
							 "\n  unsigned long end = micros();" +
							 "\n  unsigned long time;" +
							 "\n  if (end > start) {" +
							 "\n    time = end - start;" +
							 "\n  } else {" +
							 "\n    time = (end + ~start) - (start + ~start);" +
							 "\n  }" +
							 "\n  Serial.println(time, DEC);" +
							 "\n" +
							 "\n  delayMicroseconds(200000);" +
							 "\n  return ((int) time);" +
							 "\n}\n";
	
	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{	
		translator.addSetupCommand("Serial.begin(9600);" +
					   "\npinMode(" + TRANSMIT_PIN_2 + ", OUTPUT);" +
					   "\npinMode(" + RECEIVE_PIN + ", OUTPUT);\n");
		translator.addDefinitionCommand(ultraSonicFunction);
		String ret = " getDistance()";

		return codePrefix + ret + codeSuffix;
	}
	
}

