package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class ButtonPressedPinTest
{
	// TODO: Test entire list of valid pins
	private static final String SUCCESS_PIN = TranslatorBlock.FREE_PIN_1;
	// TODO: Test all other pins (expect invalid)
	private static final String FAIL_PIN = TranslatorBlock.SPEAKER_PIN;
	private static final String BUTTONPRESSED_TOCODE = "digitalRead(" + SUCCESS_PIN + ")";
	private static final String[] BUTTONPRESSED_SETUP_COMMANDS = {"pinMode( " + SUCCESS_PIN + ", INPUT);\n"};
	private static final String[] BUTTONPRESSED_DEFINITIONS = {};

	private ECSButtonPressedPinBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}
	
	@Test
	public void buttonPressedPinTest_validInput_Success() {
		blockToTest = new ECSButtonPressedPinBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_PIN);

		try {
			assertEquals(blockToTest.toCode(), BUTTONPRESSED_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), BUTTONPRESSED_DEFINITIONS, BUTTONPRESSED_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptionerino");
		}
	}

	@Test
	public void buttonPressedPinTest_invalidInput_invalidPinException() {
		blockToTest = new ECSButtonPressedPinBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", FAIL_PIN);
		try {
			blockToTest.toCode();	
		} catch (InvalidPinException invalidpin) {
			return;
		} catch (Exception e) {
			fail("");
		}

		fail("Exception Not Triggered. Expected InvalidPinException.");
	}

}

class ECSButtonPressedPinBlockStub extends ECSButtonPressedPinBlock {
	String pin;
	ECSButtonPressedPinBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String pin) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.pin = pin;
	}
	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException {
		if (i == 0) {
			return new NumberBlock(this.blockId, this.translator, this.codePrefix, this.codeSuffix, this.pin);
		} else {
			throw new SocketNullException(this.blockId);
		}
	}
}
