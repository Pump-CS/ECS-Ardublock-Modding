package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

public class ECSReadDistanceBlock extends TranslatorBlock
{
	public ECSReadDistanceBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	private final static String smoothedUltraSonicFunction = "int smoothDistance()" +
							 "\n{" +
							 "\n int count = 0;" +
							 "\n long avg = 0;" +
							 "\n int dist = 0;" +
							 "\n  while (count < 4) {" +
							 "\n    dist = getDistance();" +
							 "\n    avg = (avg + dist)/2;" +
							 "\n    count = count + 1;" +
							 "\n  }" +
							 "\n  if (count == 4){" +
							 "\n    count = 0;" +
							 "\n  }" +
							 "\n  Serial.println(avg, DEC);" +
							 "\n  return (int)avg;" +
							 "\n}\n";
							

	private final static String ultraSonicFunction = "int getDistance()" +
							 "\n{" +
							 "\n  unsigned long start = micros();" +
							 "\n  int CYCLES = 4;" +
							 "\n  int DELAY_PING = 25;" +
							 "\n  int timeout = 16000;" +
							 "\n  int T1OUT = 0x10;" +
							 "\n" +
							 "\n  for (int ii = 0; ii < CYCLES; ii++) {" +
							 "\n    digitalWrite(" + TRANSMIT_PIN_1 + ", HIGH);" +
							 "\n    digitalWrite(" + TRANSMIT_PIN_2 + ", LOW);" +
							 "\n    delayMicroseconds(DELAY_PING);" +
							 "\n    digitalWrite(" + TRANSMIT_PIN_1 + ", LOW);" +
							 "\n    digitalWrite(" + TRANSMIT_PIN_2 + ", HIGH);" +
							 "\n    delayMicroseconds(DELAY_PING);" +
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
							 "\n" +
							 "\n  delayMicroseconds(400000);" +
							 "\n  return ((int) time);" +
							 "\n}\n";
	
	@Override
	public String toCode() throws ArdublockException
	{	
		translator.addSetupCommand("Serial.begin(9600);");
		translator.addSetupCommand("pinMode(" + TRANSMIT_PIN_2 + ", OUTPUT);");
		translator.addSetupCommand("pinMode(" + TRANSMIT_PIN_1 + ", OUTPUT);");
		translator.addDefinitionCommand(smoothedUltraSonicFunction);
		translator.addDefinitionCommand(ultraSonicFunction);
		String ret = " smoothDistance()";

		return codePrefix + ret + codeSuffix;
	}
	
}

