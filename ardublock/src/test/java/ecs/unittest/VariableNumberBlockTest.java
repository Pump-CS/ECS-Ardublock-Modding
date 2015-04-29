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

public class VariableNumberBlockTest {

	private static String VAR_NAME_1 = "var1";
	private static String VAR_NAME_2 = "var2";
	private static String CONST_VALUE = "5";

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

	@Test
	public void setterVariableNumberBlockTest_useValidVariable_Succes() throws ArdublockException {
		String internal = translator.buildVariableName(VAR_NAME_1);
		translator.addNumberVariable(VAR_NAME_1, internal);
		VariableNumberBlock varBlock = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		
		assertEquals(varBlock.toCode(), internal);
	}

	@Test(expectedExceptions = InvalidNumberVariableNameException.class)
	public void setterVariableNumberBlockTest_useInvalidVariable_invalidNumberVariableNameException() throws ArdublockException {
		VariableNumberBlock varBlock = new VariableNumberBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		
		varBlock.toCode();
	}
}
