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

public class SetterVariableDigitalBlockTest {

	private static final String VAR_NAME_1 = "var1";
	private static final String VAR_NAME_2 = "var2";
	private static final String INTERNAL_VAR_PREFIX = "_ABVAR_";
	private static final String CONST_VALUE = "true";

	private SetterVariableDigitalBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	/* bool x = true */
	@Test
	public void setterVariableDigitalBlockTest_createNewVarWithConstantValue_Success() throws ArdublockException {
		String TOCODE = getInternalVarName(1, VAR_NAME_1) + " = " + CONST_VALUE + " ;\n";
		String[] DEFINITIONS = {"bool " + getInternalVarName(1, VAR_NAME_1) + " = false;"};
		String[] SETUP_COMMANDS = {};
		String headerCommands;
		
		VariableDigitalBlock varBlock = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		//DigitalBlock numBlock = new DigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		TrueBlock trueBlock = new TrueBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
		blockToTest = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, trueBlock);

		assertEquals(blockToTest.toCode(), TOCODE);
		
		headerCommands = translator.genreateHeaderCommand();

		assertTrue(ECSTestUtil.headersMatch(headerCommands, DEFINITIONS, SETUP_COMMANDS));

	}
	
	// int x = y, where y does not exist 
	@Test(expectedExceptions = InvalidBooleanVariableNameException.class)
	public void setterVariableDigitalBlockTest_createNewVarWithInvalidVariableName_invalidNumberVariableNameException() throws ArdublockException{
		VariableDigitalBlock varBlock = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableDigitalBlock valBlock = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		blockToTest = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);
		
		blockToTest.toCode();
	}

	// x = y, where both x and y exist 
	@Test
	public void setterVariableDigitalBlockTest_assignValidVarToAnotherValidVar_Success() throws ArdublockException {
		System.out.println("------------------------------");
		String internalVar1 = getInternalVarName(1, VAR_NAME_1);
		String internalVar2 = getInternalVarName(2, VAR_NAME_2);

		String TOCODE = internalVar1 + " = " + internalVar2 + " ;";
		String[] DEFINITIONS = {"bool " + internalVar1 + " = false;",
					"bool " + internalVar2 + " = false;"};
		String[] SETUP_COMMANDS = {};		


		VariableDigitalBlock varBlock1 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableDigitalBlock varBlock2 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		VariableDigitalBlock varBlock3 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableDigitalBlock varBlock4 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		TrueBlock valBlock1 = new TrueBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
			
		SetterVariableDigitalBlock setterBlock = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		SetterVariableDigitalBlock setterBlock2 = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock2, valBlock1);
		blockToTest = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock3, varBlock4);



		setterBlock.toCode();
		setterBlock2.toCode();
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	// x = y, where y does not exist 
	@Test(expectedExceptions = InvalidBooleanVariableNameException.class)
	public void setterVariableDigitalBlockTest_assignInvalidVarToValidVar_invalidNumberVariableNameException() throws ArdublockException {
		VariableDigitalBlock varBlock1 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		VariableDigitalBlock varBlock2 = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_2);
		TrueBlock valBlock1 = new TrueBlock(ECSTestUtil.TEST_ID, translator, "", "", "");

		SetterVariableDigitalBlock setterBlock = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, valBlock1);
		
		blockToTest = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock1, varBlock2);

		blockToTest.toCode();
		
	}


	// 5 = 6 
	@Test(expectedExceptions = BlockException.class)
	public void setterVariableDigitalBlockTest_assignToNonDigitalBlock_blockException() throws ArdublockException {
		TrueBlock varBlock = new TrueBlock(ECSTestUtil.TEST_ID, translator, "", "", "");
		TrueBlock valBlock = new TrueBlock(ECSTestUtil.TEST_ID, translator, "", "", "");

		blockToTest = new SetterVariableDigitalBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varBlock, valBlock);

		blockToTest.toCode();
	}

	private String getInternalVarName(int n, String name) {
		return INTERNAL_VAR_PREFIX + n + "_" + name;
	}
}
class SetterVariableDigitalBlockStub extends SetterVariableDigitalBlock {
	private TranslatorBlock sockets[];
	
	SetterVariableDigitalBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0, TranslatorBlock sock1) {
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
