package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class NoteKeyTest {

	private static final String SUCCESS_KEY = "J";
	private static final String NOTEKEY_TOCODE = "\"" + SUCCESS_KEY + "\"";


	private ECSNoteKeyBlock blockToTest;
	private Translator translator;
	
	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void noteKeyTest_validInput_Success() {
		blockToTest = new ECSNoteKeyBlock(ECSTestUtil.TEST_ID, translator, "", "", SUCCESS_KEY);
		
		try {
			assertEquals(blockToTest.toCode(), NOTEKEY_TOCODE);
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		} 
	}

	/* Note: There is no invalid input to this block. It will always be used as an input
 * 			to another block and the input scrubbing will happen there. This block ought to be
 * 			flexible. 
 */
}
