package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;

import org.testng.annotations.*;

public class LedOn1Test
{

	private static final String LED_PIN_1 = TranslatorBlock.LED_PIN_1;
	private static final String LED1_TOCODE = "digitalWrite( " + LED_PIN_1 + " , HIGH);\n";
	private static final String[] LED1_SETUP_COMMANDS = {"pinMode( " + LED_PIN_1 + ", OUTPUT);\n"};
	private static final String[] LED1_DEFINITION = {};

	private ECSLedOn1 blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}

	
	@Test
	public void ledOn1Test_noInput_Success() {
		blockToTest = new ECSLedOn1(ECSTestUtil.TEST_ID, translator, "", "", "");
	
		try {	
			assertEquals(blockToTest.toCode(), LED1_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), LED1_DEFINITION, LED1_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
