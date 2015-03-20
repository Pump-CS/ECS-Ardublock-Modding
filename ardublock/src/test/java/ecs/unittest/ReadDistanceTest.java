package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.*;

public class ReadDistanceTest {

	private static final String TRANSMIT_PIN_1 = TranslatorBlock.TRANSMIT_PIN_1;
	private static final String TRANSMIT_PIN_2 = TranslatorBlock.TRANSMIT_PIN_2;
	private static final String READDIST_TOCODE = "smoothDistance()";
	private static final String[] READDIST_SETUPCOMMANDS = {"Serial.begin(9600);", 
															"pinMode(" + TRANSMIT_PIN_2 + ", OUTPUT);",
															"pinMode(" + TRANSMIT_PIN_1 + ", OUTPUT);"};
	private static final String[] READDIST_DEFINITION_GETDISTANCE = {"int getDistance()",
															"{",
															"unsigned long start = micros();",
															"int CYCLES = 4;",
															"int DELAY_PING = 25;",
															"int timeout = 16000;",
															"int T1OUT = 0x10;",
															"for (int ii = 0; ii < CYCLES; ii++) {",
															"digitalWrite(" + TRANSMIT_PIN_1 + ", HIGH);",
															"digitalWrite(" + TRANSMIT_PIN_2 + ", LOW);",
															"delayMicroseconds(DELAY_PING);",
															"digitalWrite(" + TRANSMIT_PIN_1 + ", LOW);",
															"digitalWrite(" + TRANSMIT_PIN_2 + ", HIGH);",
															"delayMicroseconds(DELAY_PING);",
															"}",
															"delayMicroseconds(200);",
															"while((PIND & T1OUT) != 0 && timeout > 0) {",
															"timeout--;",
															"delayMicroseconds(1);",
															"}",
															"unsigned long end = micros();",
															"unsigned long time;",
															"if (end > start) {",
															"time = end - start;",
															"} else {",
															"time = (end + ~start) - (start + ~start);",
															"}",
															"delayMicroseconds(400000);",
															"return ((int) time);",
															"}",
															};

	private static final String[] READDIST_DEFINITION_SMOOTHDISTANCE = {"int smoothDistance()",
																		"{",
																		"int count = 0;",
																		"long avg = 0;",
																		"int dist = 0;",
																		"while (count < 4) {",
																		"dist = getDistance();",
																		"avg = (avg + dist)/2;",
																		"count = count + 1;",
																		"}",
																		"if (count == 4){",
																		"count = 0;",
																		"}",
																		"Serial.println(avg, DEC);",
																		"return (int)avg;",
																		"}"};
	
	private ECSReadDistanceBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void readDistanceTest_noInput_Success() {
		String headerCommands;
		String[] READDIST_DEFINITIONS = ECSTestUtil.concatArrays(READDIST_DEFINITION_SMOOTHDISTANCE, READDIST_DEFINITION_GETDISTANCE);		
		blockToTest = new ECSReadDistanceBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
		
		try {
			assertEquals(blockToTest.toCode().trim(), READDIST_TOCODE);

			headerCommands = translator.genreateHeaderCommand();
			assertTrue(ECSTestUtil.headersMatch(headerCommands, READDIST_DEFINITIONS, READDIST_SETUPCOMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		}
	}
}
