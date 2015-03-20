package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class PlayNoteTest
{
	private static final String SPEAKER_PIN = TranslatorBlock.SPEAKER_PIN;
	private static final String SUCCESS_NOTE = "A";
	private static final String FAIL_NOTE = "M";
	private static final String PLAYNOTE_TOCODE = "tone(" + SPEAKER_PIN + ", " + ECSPlayNoteBlock.notemap.get(SUCCESS_NOTE) + ");\n";
	
	private ECSPlayNoteBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}
	
	@Test
	public void playNoteTest_validInput_Success() {
		blockToTest = new ECSPlayNoteBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_NOTE);
		try {
			assertEquals(blockToTest.toCode(), PLAYNOTE_TOCODE);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptionerino");
		}
	}

	@Test
	public void playNoteTest_invalidInput_invalidNoteException() {
		blockToTest = new ECSPlayNoteBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", FAIL_NOTE);
		try {
			blockToTest.toCode();	
		} catch (InvalidNoteException invalidnote) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Expected InvalidNoteException.");
		}

		fail("Exception Not Triggered. Expected InvalidNoteException.");
	}

}

class ECSPlayNoteBlockStub extends ECSPlayNoteBlock {
	String note;
	ECSPlayNoteBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String note) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.note = note;
	}
	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException {
		if (i == 0) {
			return new MessageBlock(this.blockId, this.translator, this.codePrefix, this.codeSuffix, this.note);
		} else {
			throw new SocketNullException(this.blockId);
		}
	}
}
