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

public class KeyPressedTest
{
	private static final String KEYS_DOWN_ARRAY = "keysDown";
	private static final String VALID_KEY_1 = "j";
	private static final String VALID_KEY_2 = "2";
	private static final String INVALID_KEY_1 = "J";
	private static final String INVALID_KEY_2 = "+";
	private static final String VALID_KEY_INDEX_1 = "19";
	private static final String VALID_KEY_INDEX_2 = "2";

	private ECSKeyPressed blockToTest;
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
	public void keyPressedTest_validInputAlpha_Success() throws ArdublockException {
		String TOCODE = KEYS_DOWN_ARRAY + "[" + VALID_KEY_INDEX_1 + "]";
		MessageBlock key = new MessageBlock(ECSTestUtil.TEST_ID, translator, "", "", VALID_KEY_1);
		
		blockToTest = new ECSKeyPressedStub(ECSTestUtil.TEST_ID, translator, "", "", "", key);
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test
	public void keyPressedTest_validInputDigit_Succes() throws ArdublockException {
		String TOCODE = KEYS_DOWN_ARRAY + "[" + VALID_KEY_INDEX_2 + "]";
		MessageBlock key = new MessageBlock(ECSTestUtil.TEST_ID, translator, "", "", VALID_KEY_2);
		
		blockToTest = new ECSKeyPressedStub(ECSTestUtil.TEST_ID, translator, "", "", "", key);
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test(expectedExceptions = InvalidKeyException.class)
	public void keyPressedTest_invalidInputCapAlpha_invalidKeyException() throws ArdublockException {
		MessageBlock key = new MessageBlock(ECSTestUtil.TEST_ID, translator, "", "", INVALID_KEY_1);
		blockToTest = new ECSKeyPressedStub(ECSTestUtil.TEST_ID, translator, "", "", "", key);
		blockToTest.toCode();
	}

	@Test(expectedExceptions = InvalidKeyException.class)
	public void keyPressedTest_invalidInputSpecialChar_invalidKeyException() throws ArdublockException {
		MessageBlock key = new MessageBlock(ECSTestUtil.TEST_ID, translator, "", "", INVALID_KEY_2);
		blockToTest = new ECSKeyPressedStub(ECSTestUtil.TEST_ID, translator, "", "", "", key);
		blockToTest.toCode();
	}
}

class ECSKeyPressedStub extends ECSKeyPressed {
        private TranslatorBlock socket;

        ECSKeyPressedStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0) {
                super(blockId + 1, translator, codePrefix, codeSuffix, label);
                this.socket = sock0;
        }

        public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
                if (sock > 0) {
                        throw new SocketNullException(blockId);
                }
                return socket;
        }
}
 
