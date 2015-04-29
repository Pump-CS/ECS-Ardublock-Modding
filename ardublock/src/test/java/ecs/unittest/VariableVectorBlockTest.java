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

public class VariableVectorBlockTest {

	private static final String INTERNAL_VAR_PREFIX = "_ABVAR_";
	private static final String VAR_NAME = "varName";
	private static final String NUMBER_VAR_NAME = "numVarName";
	private static final String CONST_SIZE = "10";
	private static final String CONST_POS = "5";

	private VariableVectorBlock blockToTest;
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
	public void variableVectorBlockTest_validArrayNameAccess_Success() throws ArdublockException {
		String TOCODE = getInternalVecVarName(1, VAR_NAME) + "[" + CONST_POS + " - 1]";
		
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POS);
		
		blockToTest = new VariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME, pos);

		createVar(translator, VAR_NAME, CONST_SIZE);

		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test
	public void variableVectorBlockTest_variableVarPosition_Success() throws ArdublockException {
		String TOCODE = getInternalVecVarName(1, VAR_NAME) + "[" + getInternalVarName(2, NUMBER_VAR_NAME) + " - 1]";

		VariableNumberBlock pos = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", NUMBER_VAR_NAME);
		blockToTest = new VariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME, pos);

		createVar(translator, VAR_NAME, CONST_SIZE);
		createNumberVar(translator, NUMBER_VAR_NAME);
		
		assertEquals(blockToTest.toCode().trim(), TOCODE);
	}

	@Test(expectedExceptions = InvalidArrayVariableNameAccessException.class)
	public void variableVectorBlockTest_invalidArrayName_invalidArrayVariableNameAccessException() throws ArdublockException {
		NumberBlock pos = new NumberBlock(ECSTestUtil.TEST_ID, translator, "", "", CONST_POS);
		blockToTest = new VariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME, pos);

		blockToTest.toCode();
	}

	// This test should come back if ever you decide to control the position argument more strictly.
/*
	@Test(expectedExceptions = BlockException.class)
	public void variableVectorBlockTest_nonConstNonNumberVariablePosition_blockException() throws ArdublockException {
		NumberDoubleBlock pos = new NumberDoubleBlock(ECSTestUtil.TEST_ID, translator, "", "", "3.14");
		blockToTest = new VariableVectorBlockStub(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME, pos);

		blockToTest.toCode();
	}
*/

/*
	//@Test
	//public void variableVectorBlockTest_invalidConstPosition_???
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

class VariableVectorBlockStub extends VariableVectorBlock {
        private TranslatorBlock socket;

        VariableVectorBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, TranslatorBlock sock0) {
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
