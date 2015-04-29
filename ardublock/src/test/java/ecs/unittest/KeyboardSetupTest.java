package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;
import com.ardublock.translator.block.exception.*;
import com.ardublock.core.exception.ArdublockException;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import static org.testng.Assert.*;

import org.testng.annotations.*;

public class KeyboardSetupTest
{
	private static final String KEYS_DOWN_ARRAY = "keysDown";
	private static final int KEYS_DOWN_ARRAY_SIZE = 36;
	private static final String VALID_KEY_1 = "j";
	private static final String VALID_KEY_2 = "2";
	private static final String INVALID_KEY_1 = "J";
	private static final String INVALID_KEY_2 = "+";
	private static final String VALID_KEY_INDEX_1 = "19";
	private static final String VALID_KEY_INDEX_2 = "2";

	private ECSKeyboardSetup blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp()
	{
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void reset() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void keyboardSetupTest_noInput_Success() throws ArdublockException {
		String TOCODE = "";
		String[] DEFINITIONS = {String.format("boolean %s[%d];", KEYS_DOWN_ARRAY, KEYS_DOWN_ARRAY_SIZE)};
		String[] SETUP_COMMANDS = {};
		String headerCommands;

		blockToTest = new ECSKeyboardSetupStub(ECSTestUtil.TEST_ID, translator, "", "", "");
		blockToTest.isInTest = true;
		assertEquals(blockToTest.toCode(), TOCODE);

		headerCommands = translator.genreateHeaderCommand();

		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));
	}
}

class ECSKeyboardSetupStub extends ECSKeyboardSetup {

        ECSKeyboardSetupStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
                super(blockId + 1, translator, codePrefix, codeSuffix, label);
        }

        public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
                throw new SocketNullException(blockId);
        }
}
 
