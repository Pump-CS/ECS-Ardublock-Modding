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

public class SetterVariableNumberDoubleBlockTest {

	private static final String VAR_NAME_1 = "var1";
	private static final String VAR_NAME_2 = "var2";
	private static final String INTERNAL_VAR_PREFIX = "_ABVAR_";
	private static final String CONST_VALUE = "3.14";

	private SetterVariableNumberDoubleBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	/* double x = 3.14 */
	@Test
	public void setterVariableNumberDoubleBlockTest_createNewVarWithConstantValue_Success() throws ArdublockException {
		String TOCODE = getInternalVarName(1, VAR_NAME_1) + " = " + CONST_VALUE + " ;\n";
		String[] DEFINITIONS = {"double " + getInternalVarName(1, VAR_NAME_1) + " = 0.0;"};
		String[] SETUP_COMMANDS = {};
		String headerCommands;
		
		VariableNumberDoubleBlock varBlock = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		NumberDoubleBlock numBlock = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		blockToTest = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, numBlock);


		assertEquals(blockToTest.toCode(), TOCODE);
		
		headerCommands = translator.genreateHeaderCommand();

		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));

	}
	

	// double x = y, where y does not exist 
	@Test(expectedExceptions = InvalidNumberDoubleVariableNameException.class)
	public void setterVariableNumberDoubleBlockTest_createNewVarWithInvalidVariableName_invalidNumberVariableNameException() throws ArdublockException{
		VariableNumberDoubleBlock varBlock = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberDoubleBlock valBlock = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		blockToTest = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);
		
		blockToTest.toCode();
	}

	// x = y, where both x and y exist 
	@Test
	public void setterVariableNumberDoubleBlockTest_assignValidVarToAnotherValidVar_Success() throws ArdublockException {
		String internalVar1 = getInternalVarName(1, VAR_NAME_1);
		String internalVar2 = getInternalVarName(2, VAR_NAME_2);

		String TOCODE = internalVar1 + " = " + internalVar2 + " ;";
		String[] DEFINITIONS = {"double " + internalVar1 + " = 0.0;",
					"double " + internalVar2 + " = 0.0;"};
		String[] SETUP_COMMANDS = {};		


		VariableNumberDoubleBlock varBlock1 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberDoubleBlock varBlock2 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		VariableNumberDoubleBlock varBlock3 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberDoubleBlock varBlock4 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		NumberDoubleBlock valBlock1 = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
			
		SetterVariableNumberDoubleBlock setterBlock = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		SetterVariableNumberDoubleBlock setterBlock2 = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock2, valBlock1);
		blockToTest = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock3, varBlock4);

		setterBlock.toCode();
		setterBlock2.toCode();
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	// x = y, where y does not exist 
	@Test(expectedExceptions = InvalidNumberDoubleVariableNameException.class)
	public void setterVariableNumberDoubleBlockTest_assignInvalidVarToValidVar_invalidNumberVariableNameException() throws ArdublockException {
		VariableNumberDoubleBlock varBlock1 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableNumberDoubleBlock varBlock2 = new VariableNumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		NumberDoubleBlock valBlock1 = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		SetterVariableNumberDoubleBlock setterBlock = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		
		blockToTest = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, varBlock2);

		blockToTest.toCode();
		
	}


	// 5.5 = 6.6
	@Test(expectedExceptions = BlockException.class)
	public void setterVariableNumberDoubleBlockTest_assignToNonNumberDoubleBlock_blockException() throws ArdublockException {
		NumberDoubleBlock varBlock = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		NumberDoubleBlock valBlock = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		blockToTest = new SetterVariableNumberDoubleBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);

		blockToTest.toCode();
	}


	private String getInternalVarName(int n, String name) {
		return INTERNAL_VAR_PREFIX + n + "_" + name;
	}
}
class SetterVariableNumberDoubleBlockStub extends SetterVariableNumberDoubleBlock {
	private TranslatorBlock sockets[];
	
	SetterVariableNumberDoubleBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0, TranslatorBlock sock1) {
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
