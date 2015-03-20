package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.*;

public class UpdateKeyboardTest {

	private static final String KEYS_ARRAY_VAR_NAME = ECSKeyboardSetup.KEYS_ARRAY;
	private static final String[] UPDATEKEYBOARD_DEFINITIONS = {"int ECSindex;",
																"int ECSnumAvailable;",
																"int ECSiteration;"};
	private ECSUpdateKeyboard blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void updateKeyboardTest_noInput_Success() {
		
	}
}
