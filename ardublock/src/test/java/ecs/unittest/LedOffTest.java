package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;

import org.testng.annotations.*;

public class LedOffTest
{
	private static final String SUCCESS_PIN = TranslatorBlock.FREE_PIN_1;
	private static final String FAIL_PIN	= TranslatorBlock.SPEAKER_PIN;
	private static final String LEDOFF_TOCODE = "digitalWrite( " + SUCCESS_PIN + " , LOW);\n";
	private static final String[] LEDOFF_SETUP_COMMANDS = {"pinMode( " + SUCCESS_PIN + ", OUTPUT);\n"};
	private static final String[] LEDOFF_DEFINITIONS = {};

	private ECSLedOff blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}

	
	@Test
	public void ledOffTest_validInput_Success() {
		blockToTest = new ECSLedOffStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_PIN);
	
		try {	
			assertEquals(blockToTest.toCode(), LEDOFF_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), LEDOFF_DEFINITIONS, LEDOFF_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void ledOffTest_invalidInput_invalidPinException() {
		blockToTest = new ECSLedOffStub(ECSTestUtil.TEST_ID, translator, "", "", "", FAIL_PIN);
		
		try {
			blockToTest.toCode();
		} catch (InvalidPinException invalidPin) {
			// Correct behavior
			return;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception triggered but was not InvalidPinException.");
		}
		
		fail("No exception triggered.");
	}

}

class ECSLedOffStub extends ECSLedOff {
	String pin;
	
	ECSLedOffStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String pin) {
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.pin = pin;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException {
		if (i == 0) {
			return new NumberBlock(this.blockId + 1, this.translator, this.codePrefix, this.codeSuffix, this.pin);
		} else {
			throw new SocketNullException(this.blockId);
		}
	}
}
