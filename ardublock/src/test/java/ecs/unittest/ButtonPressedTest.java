package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class ButtonPressedTest
{
	private static final String SUCCESS_PIN = "1";
	// Pin input '1' is adjusted to pin BUTTON_PIN_1 by ECSButtonPressedBlock
	private static final String SUCCESS_ADJUSTED_PIN = TranslatorBlock.BUTTON_PIN_1;
	private static final String FAIL_PIN = TranslatorBlock.SPEAKER_PIN;
	private static final String BUTTONPRESSED_TOCODE = "digitalRead(" + SUCCESS_ADJUSTED_PIN + ")";
	private static final String[] BUTTONPRESSED_SETUP_COMMANDS = {
									"pinMode( " + TranslatorBlock.BUTTON_PIN_1 + ", INPUT);\n",
									"pinMode( " + TranslatorBlock.BUTTON_PIN_2 + ", INPUT);\n",
									"pinMode( " + TranslatorBlock.BUTTON_PIN_3 + ", INPUT);\n",
									"pinMode( " + TranslatorBlock.BUTTON_PIN_4 + ", INPUT);\n"};
	private static final String[] BUTTONPRESSED_DEFINITIONS = {};

	private ECSButtonPressedBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}
	
	@Test
	public void buttonPressed_validInput_Success() {
		blockToTest = new ECSButtonPressedBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_PIN);

		try {
			assertEquals(blockToTest.toCode(), BUTTONPRESSED_TOCODE);
			assertTrue(ECSTestUtil.headersMatch(translator.genreateHeaderCommand(), BUTTONPRESSED_DEFINITIONS, BUTTONPRESSED_SETUP_COMMANDS));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptionerino");
		}
	}

	@Test
	public void buttonPressedTest_invalidInput_invalidButtonException() {
		blockToTest = new ECSButtonPressedBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", FAIL_PIN);
		try {
			blockToTest.toCode();	
		} catch (InvalidButtonException invalidpin) {
			return;
		} catch (Exception e) {
			fail("");
		}

		fail("Exception Not Triggered. Expected InvalidNoteException.");
	}

}

class ECSButtonPressedBlockStub extends ECSButtonPressedBlock {
	String pin;
	ECSButtonPressedBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String pin) {
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
