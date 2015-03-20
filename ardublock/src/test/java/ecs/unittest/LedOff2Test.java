package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;

import org.testng.annotations.*;

public class LedOff2Test
{

	private static final String LED_PIN_2 = TranslatorBlock.LED_PIN_2;
	private static final String LED2_TOCODE = "digitalWrite( " + LED_PIN_2 + " , LOW);\n";
	private static final String[] LED2_SETUP_COMMANDS = {"pinMode( " + LED_PIN_2 + ", OUTPUT);\n"};
	private static final String[] LED2_DEFINITIONS = {};

	private ECSLedOff2 blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}

	
	@Test
	public void ledOff2Test_noInput_Success() {
		blockToTest = new ECSLedOff2(ECSTestUtil.TEST_ID, translator, "", "", "");
	
		try {	
			assertEquals(blockToTest.toCode(), LED2_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), LED2_DEFINITIONS, LED2_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
