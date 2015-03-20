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
	private static final String READDIST_TOCODE = "getDistance()";
	private static final String[] READDIST_SETUPCOMMANDS = {"Serial.begin(9600);", 
															"pinMode(" + TRANSMIT_PIN_1 + ", OUTPUT);",
															"pinMode(" + TRANSMIT_PIN_2 + ", OUTPUT);"};
	private static final String[] READDIST_DEFINITIONS = 	{"int getDistance()",
															"{",
															"unsigned long start = micros();",
															"int cycles = 4;",
															"int delay = 20;",
															"int timeout = 8000;",
															"int T1OUT = 0x10;",
															"",
															"for (int ii = 0; ii < 4; ii++) {",
															"digitalWrite(" + TRANSMIT_PIN_1 + ", HIGH);",
															"digitalWrite(" + TRANSMIT_PIN_2 + ", LOW);",
															"delayMicroseconds(delay);",
															"digitalWrite(" + TRANSMIT_PIN_1 + ", LOW);",
															"digitalWrite(" + TRANSMIT_PIN_2 + ", HIGH);",
															"delayMicroseconds(delay);",
															"}",
															"",
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
															"Serial.println(time, DEC);",
															"",
															"delayMicroseconds(200000);",
															"return ((int) time);",
															"}",
															};
	
	private ECSReadDistanceBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void readDistanceTest_noInput_Success() {
		String headerCommands;
		blockToTest = new ECSReadDistanceBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
		
		try {
			assertEquals(blockToTest.toCode().trim(), READDIST_TOCODE);

			headerCommands = translator.genreateHeaderCommand();
			//assertTrue(ECSTestUtil.codeMatchesUnordered(READDIST_SETUPCOMMANDS, headerCommands),
			//			ECSTestUtil.setupCommandErrorMessage(headerCommands, READDIST_SETUPCOMMANDS));

			//assertTrue(ECSTestUtil.codeMatchesOrdered(READDIST_DEFINITIONS, headerCommands),
			//			ECSTestUtil.codeMatchesErrorMessage(headerCommands, READDIST_DEFINITIONS));
			
			assertTrue(ECSTestUtil.headersMatch(headerCommands, READDIST_DEFINITIONS, READDIST_SETUPCOMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		}
	}
}
