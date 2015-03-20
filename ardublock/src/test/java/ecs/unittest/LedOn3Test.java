package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;

import org.testng.annotations.*;

public class LedOn3Test
{

	private static final String LED_PIN_3 = TranslatorBlock.LED_PIN_3;
	private static final String LED3_TOCODE = "digitalWrite( " + LED_PIN_3 + " , HIGH);\n";
	private static final String[] LED3_SETUP_COMMANDS = {"pinMode( " + LED_PIN_3 + ", OUTPUT);\n"};
	private static final String[] LED3_DEFINITIONS = {};

	private ECSLedOn3 blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}

	
	@Test
	public void ledOn3Test_noInput_Success() {
		blockToTest = new ECSLedOn3(ECSTestUtil.TEST_ID, translator, "", "", "");
	
		try {	
			assertEquals(blockToTest.toCode(), LED3_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), LED3_DEFINITIONS, LED3_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
