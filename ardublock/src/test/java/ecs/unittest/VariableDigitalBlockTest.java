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

public class VariableDigitalBlockTest {

	private static String VAR_NAME_1 = "var1";

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

	@Test
	public void setterVariableDigitalBlockTest_useValidVariable_Succes() throws ArdublockException {
		String internal = translator.buildVariableName(VAR_NAME_1);
		translator.addBooleanVariable(VAR_NAME_1, internal);
		VariableDigitalBlock varBlock = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		
		assertEquals(varBlock.toCode(), internal);
	}

	@Test(expectedExceptions = InvalidBooleanVariableNameException.class)
	public void setterVariableDigitalBlockTest_useInvalidVariable_invalidNumberVariableNameException() throws ArdublockException {
		VariableDigitalBlock varBlock = new VariableDigitalBlock(ECSTestUtil.TEST_ID, translator, "", "", VAR_NAME_1);
		
		varBlock.toCode();
	}
}
