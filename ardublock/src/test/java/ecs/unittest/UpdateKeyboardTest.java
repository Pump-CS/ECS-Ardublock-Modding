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

public class UpdateKeyboardTest
{
	private static final String KEYS_DOWN_ARRAY = "keysDown";
	private static final int KEYS_DOWN_ARRAY_SIZE = 36;
	private static final String TOCODE = "ECSnumAvailable = Serial.available();\n" + 
						"char buffer[ECSnumAvailable];" +
						"Serial.readBytes(buffer, ECSnumAvailable);" +
						"for (ECSiteration = 0; ECSiteration < ( ECSnumAvailable ); ++ECSiteration)\n" +
						"{\n\t" +
						"ECSindex = buffer[ECSiteration];" +
						KEYS_DOWN_ARRAY + "[ECSindex] = !" + KEYS_DOWN_ARRAY + "[ECSindex];\n" +
						"}";
/*
ret += "ECSnumAvailable = Serial.available();\n";

                // Allocate an array to store ECSnumAvailable bytes 
                ret += "char buffer[ECSnumAvailable];";

                // Read data from serial port 
                ret += "Serial.readBytes(buffer, ECSnumAvailable);";

                // Loop through buffer and toggle the appropriate key in keysDown array 
                ret += "for (ECSiteration = 0; ECSiteration < ( ECSnumAvailable ); ++ECSiteration)\n";
                ret += "{\n\t";
                ret +=          "ECSindex = buffer[ECSiteration];";
                ret +=          ECSKeyboardSetup.KEYS_ARRAY + "[ECSindex] = !" + ECSKeyboardSetup.KEYS_ARRAY + "[ECSindex];\n";
                ret += "}\n";
*/
	private ECSUpdateKeyboard blockToTest;
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
	public void updateKeyboardTest_noInput_Success() throws ArdublockException {
		String[] DEFINITIONS = {"int ECSindex;", "int ECSnumAvailable;", "int ECSiteration;"};
		String[] SETUP_COMMANDS = {};
		String headerCommands;

		blockToTest = new ECSUpdateKeyboardStub(ECSTestUtil.TEST_ID, translator, "", "", "");
	
		assertEquals(blockToTest.toCode().trim(), TOCODE);

		headerCommands = translator.genreateHeaderCommand();

		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));
	}
}

class ECSUpdateKeyboardStub extends ECSUpdateKeyboard {

        ECSUpdateKeyboardStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
                super(blockId + 1, translator, codePrefix, codeSuffix, label);
        }

        public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
                throw new SocketNullException(blockId);
        }
}
 
