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

public class SetterVariableVectorBlockTest {

	private static final String INTERNAL_VAR_PREFIX = "_ABVAR_";
	private static final String VAR_NAME = "varName";
	private static final String CONST_POSITION = "10";
	private static final String CONST_VALUE = "-1";
	private static final String CONST_SIZE = "20";
	private static final String NUMBER_VAR_NAME = "numVarName";

	private SetterVariableVectorBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void setterVariableVectorBlockTest_validArrayNameValidIndex_Success() throws ArdublockException {
		String TOCODE = getInternalVecVarName(1, VAR_NAME) + "[" + CONST_POSITION + " - 1] = " + CONST_VALUE + ";";		

		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POSITION);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		createVar(translator, VAR_NAME, CONST_SIZE);
		
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test(expectedExceptions = InvalidArrayVariableNameAccessException.class)
	public void setterVariableVectorBlockTest_zeroIndexInvalidVariableName_invalidArrayVariableNameAccessException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", "invalidvarname");
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POSITION);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);
	
		blockToTest.toCode();
	}

	@Test(expectedExceptions = BlockException.class)
	public void setterVariableVectorBlockTest_nonFakeBlockVariableName_blockException() throws ArdublockException {
		NumberBlock badVarName = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POSITION);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);

		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", badVarName, pos, val);
		blockToTest.toCode();
	}

	@Test
	public void setterVariableVectorBlockTest_numberVariablePosition_Success() throws ArdublockException {
		String TOCODE = getInternalVecVarName(1, VAR_NAME) + "[" + getInternalVarName(2, NUMBER_VAR_NAME) + " - 1] = " + CONST_VALUE + ";";

		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		VariableNumberBlock pos = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", NUMBER_VAR_NAME);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		
		createVar(translator, VAR_NAME, CONST_SIZE);
		createNumberVar(translator, NUMBER_VAR_NAME); 
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);

		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test
	public void setterVariableVectorBlockTest_numberVariableValue_Success() throws ArdublockException {
		String TOCODE = getInternalVecVarName(1, VAR_NAME) + "[" + CONST_POSITION + " - 1] = " + getInternalVarName(2, NUMBER_VAR_NAME) + ";";

		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POSITION);
		VariableNumberBlock val = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", NUMBER_VAR_NAME);
		
		createVar(translator, VAR_NAME, CONST_SIZE);
		createNumberVar(translator, NUMBER_VAR_NAME);
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);
	
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	//@Test
	//public void setterVariableVectorBlockTest_invalidConstIndex_???

	// This test should come back if ever you decide to control the position argument more strictly. 
	// Right now, ardublock will not complain if you use a float as the position argument, but Arduino might complain.
	// We are ignoring this for now, but you probably will not want to in the future.
/*
	@Test(expectedExceptions = BlockException.class)
	public void setterVariableVectorBlockTest_nonNumberVarNonConstBlockPosition_blockException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberDoubleBlock badPos = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", "3.14");
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		
		createVar(translator, VAR_NAME, CONST_SIZE);
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, badPos, val);
		
		blockToTest.toCode();
	}
*/	

	// This test should come back if ever you decide to disallow floats to be assigned to int values. 
	// As of this moment, Arduino does not complain when you do this.
/*
	@Test(expectedExceptions = BlockException.class)
	public void setterVariableVectorBlockTest_nonNumberVarNonConstBlockValue_blockException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME);
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		NumberDoubleBlock val = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", NUMBER_VAR_NAME);
		
		createVar(translator, VAR_NAME, CONST_SIZE);
		createNumberVar(translator, NUMBER_VAR_NAME);
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);

		try {
			blockToTest.toCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/

	@Test(expectedExceptions = InvalidArrayVariableNameAccessException.class)
	public void setterVariableVectorBlockTest_invalidArrayName_invalidArrayVariableNameAccessException() throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", "badVarName");
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POSITION);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_VALUE);
		
		blockToTest = new SetterVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, pos, val);
		
		blockToTest.toCode();
	}
/*
	//@Test
	//public void setterVariableVectorBlockTest_outOfBoundsPosition_???
*/
	private String getInternalVecVarName(int n, String name) {
		return "vec_" + INTERNAL_VAR_PREFIX + n + "_" + name;
	}

	private String getInternalVarName(int n, String name) {
		return INTERNAL_VAR_PREFIX + n + "_" + name;
	}	

	private void createVar(Translator translator, String name, String size) throws ArdublockException {
		VariableFakeBlock varName = new VariableFakeBlock(ECSTestUtil.TEST_ID, translator, "", "", name);
		NumberBlock sizeBlock = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", size);

		CreateVariableVectorBlock createBlock = new CreateVariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, sizeBlock);
		createBlock.toCode();
	}

	private void createNumberVar(Translator translator, String name) throws ArdublockException {
		VariableNumberBlock varName = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", name);
		NumberBlock val = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", "0");
		
		SetterVariableNumberBlock setterBlock = new SetterVariableNumberBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", varName, val);
		setterBlock.toCode();
	}
}
class SetterVariableVectorBlockStub extends SetterVariableVectorBlock {
	private TranslatorBlock sockets[];
	
	SetterVariableVectorBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0, TranslatorBlock sock1, TranslatorBlock sock2) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.sockets = new TranslatorBlock[3];
		this.sockets[0] = sock0;
		this.sockets[1] = sock1;
		this.sockets[2] = sock2;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int sock) throws SocketNullException {
		if (sock > 2) {
			throw new SocketNullException(blockId);
		}
		return sockets[sock];
	}
}
