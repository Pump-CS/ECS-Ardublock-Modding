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

public class CreateVariableVectorBlockTest {

	private static final String INTERNAL_VAR_PREFIX = "vec__ABVAR_";
	private static final String VAR_NAME = "vecVar";
	private static final String CONST_SIZE = "10";

	private CreateVariableVectorBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	// createVariableVectorBlockTest
	
	@Test
	public void createVariableVectorBlockTest_validVariableCreate_Success() throws ArdublockException{
		String TOCODE = "";
		String[] DEFINITIONS = {"int " + getInternalVarName(1, VAR_NAME) + "[" + CONST_SIZE + "];"};
		String[] SETUP_COMMANDS = {"for (int i = 0; i < " + CONST_SIZE + "; i++) {" + getInternalVarName(1, VAR_NAME)  + "[i] = 0;}"};
		String headerCommands;
		
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberBlock size = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_SIZE);

		blockToTest = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);

		assertEquals(blockToTest.toCode().trim(), TOCODE);
		headerCommands = translator.genreateHeaderCommand();
		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));
	}

	// Should not actually be possible in Ardublock (enforced by openblocks, not ardublock), but will test anyway
	@Test
	public void createVariableVectorBlockTest_emptyVariableName_Success() throws ArdublockException {
		String TOCODE = "";
		String[] DEFINITIONS = {"int " + getInternalVarName(1, "") + "[" + CONST_SIZE + "];"};
		String[] SETUP_COMMANDS = {"for (int i = 0; i < " + CONST_SIZE + "; i++) {" + getInternalVarName(1, "")  + "[i] = 0;}"};
		String headerCommands;

		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
		NumberBlock size = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_SIZE);
		
		blockToTest = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);
		
		assertEquals(blockToTest.toCode().trim(), TOCODE);
		headerCommands = translator.genreateHeaderCommand();
		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));
	}

	@Test(expectedExceptions = BlockException.class)
	public void createVariableVectorBlockTest_nonFakeBlockVarName_blockException() throws ArdublockException {
		NumberBlock varName = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_SIZE);
		NumberBlock size = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_SIZE);

		blockToTest = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);
		blockToTest.toCode();
	}

	@Test(expectedExceptions = BlockException.class)
	public void createVariableVectorBlockTest_nonNumberBlockSize_blockException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberDoubleBlock size = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", "3.14");
	
		blockToTest = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);
		blockToTest.toCode();
	}
/*
	@Test(expectedExceptions = InvalidArraySizeException.class)
	public void createVariableVectorBlockTest_invalidSize_invalidArraySizeException() throws ArdublockException {

	}
*/

	@Test(expectedExceptions = InvalidArrayVariableNameCreateException.class)
	public void createVariableVectorBlockTest_duplicateVarName_invalidArrayVariableNameCreateException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberBlock size = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_SIZE);
		
		CreateVariableVectorBlock createBlock = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);
		blockToTest = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, size);

		createBlock.toCode();
		blockToTest.toCode();
	}

	private String getInternalVarName(int n, String name) {
		return INTERNAL_VAR_PREFIX + n + "_" + name;
	}
}
class CreateVariableVectorBlockStub extends CreateVariableVectorBlock {
	private TranslatorBlock sockets[];
	
	CreateVariableVectorBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0, TranslatorBlock sock1) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.sockets = new TranslatorBlock[2];
		this.sockets[0] = sock0;
		this.sockets[1] = sock1;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
		if (sock > 1) {
			throw new SocketNullException(blockId);
		}
		return sockets[sock];
	}
}
