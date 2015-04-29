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

public class SetterVariableNumberBlockTest {

	private static final String VAR_NAME_1 = "var1";
	private static final String VAR_NAME_2 = "var2";
	private static final String INTERNAL_VAR_PREFIX = "_ABVAR_";
	private static final String CONST_VALUE = "5";


	private SetterVariableNumberBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	/* int x = 5 */
	@Test
	public void setterVariableNumberBlockTest_createNewVarWithConstantValue_Success() throws ArdublockException {
		String TOCODE = getInternalVarName(1, VAR_NAME_1) + " = " + CONST_VALUE + " ;\n";
		String[] DEFINITIONS = {"int " + getInternalVarName(1, VAR_NAME_1) + " = 0;"};
		String[] SETUP_COMMANDS = {};
		String headerCommands;
		
		VariableNumberBlock varBlock = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		NumberBlock numBlock = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		blockToTest = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, numBlock);


		assertEquals(blockToTest.toCode(), TOCODE);
		
		headerCommands = translator.genreateHeaderCommand();

		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));

	}
	
	// int x = y, where y does not exist 
	@Test(expectedExceptions = InvalidNumberVariableNameException.class)
	public void setterVariableNumberBlockTest_createNewVarWithInvalidVariableName_invalidNumberVariableNameException() throws ArdublockException{
		VariableNumberBlock varBlock = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberBlock valBlock = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		blockToTest = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);
		
		blockToTest.toCode();
	}


	// x = y, where both x and y exist 
	@Test
	public void setterVariableNumberBlockTest_assignValidVarToAnotherValidVar_Success() throws ArdublockException {
		String internalVar1 = getInternalVarName(1, VAR_NAME_1);
		String internalVar2 = getInternalVarName(2, VAR_NAME_2);

		String TOCODE = internalVar1 + " = " + internalVar2 + " ;";
		String[] DEFINITIONS = {"int " + internalVar1 + " = 0;",
					"int " + internalVar2 + " = 0;"};
		String[] SETUP_COMMANDS = {};		


		VariableNumberBlock varBlock1 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberBlock varBlock2 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		VariableNumberBlock varBlock3 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberBlock varBlock4 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		NumberBlock valBlock1 = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
			
		SetterVariableNumberBlock setterBlock = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		SetterVariableNumberBlock setterBlock2 = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock2, valBlock1);
		blockToTest = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock3, varBlock4);

		setterBlock.toCode();
		setterBlock2.toCode();
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	// x = y, where y does not exist 
	@Test(expectedExceptions = InvalidNumberVariableNameException.class)
	public void setterVariableNumberBlockTest_assignInvalidVarToValidVar_invalidNumberVariableNameException() throws ArdublockException {
		VariableNumberBlock varBlock1 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberBlock varBlock2 = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		NumberBlock valBlock1 = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		SetterVariableNumberBlock setterBlock = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		
		blockToTest = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, varBlock2);

		blockToTest.toCode();
		
	}
	// 5 = 6 
	@Test(expectedExceptions = BlockException.class)
	public void setterVariableNumberBlockTest_assignToNonNumberBlock_blockException() throws ArdublockException {
		NumberBlock varBlock = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		NumberBlock valBlock = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		blockToTest = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);

		blockToTest.toCode();
	}

	private String getInternalVarName(int n, String name) {
		return INTERNAL_VAR_PREFIX + n + "_" + name;
	}
}
class SetterVariableNumberBlockStub extends SetterVariableNumberBlock {
	private TranslatorBlock sockets[];
	
	SetterVariableNumberBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0, TranslatorBlock sock1) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.sockets = new TranslatorBlock[2];
		this.sockets[0] = sock0;
		this.sockets[1] = sock1;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
		if (sock > 2) {
			throw new SocketNullException(blockId);
		}
		return sockets[sock];
	}
}
