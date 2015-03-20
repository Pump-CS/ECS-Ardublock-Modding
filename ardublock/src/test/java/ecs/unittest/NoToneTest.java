package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;
import org.testng.annotations.*;

public class NoToneTest {
	private static final String SPEAKER_PIN = TranslatorBlock.SPEAKER_PIN;
	private static final String NOTONE_TOCODE = "noTone(" + SPEAKER_PIN + ");\n";

	private ECSNoToneBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void noToneTest_noInput_Success() {
		blockToTest = new ECSNoToneBlock(ECSTestUtil.TEST_ID, translator, "", "", "");

		try {
			assertEquals(blockToTest.toCode(), NOTONE_TOCODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
