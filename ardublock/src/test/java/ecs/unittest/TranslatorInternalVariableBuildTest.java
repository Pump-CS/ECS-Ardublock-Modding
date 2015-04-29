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

public class TranslatorInternalVariableBuildTest {

	private Translator translator;

	private String errorString(String expected, String output) {
		return String.format("Expected: %s\nActual: %s\n", expected, output);
	}	

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@BeforeMethod
	public void beforeTest() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void translatorInternalVariableBuildTest_alphaVarNameNoSpace_Success() {
		String input = "variableName";
		String expected = "_ABVAR_1_variableName";
		
		String output = translator.buildVariableName(input);
		
		assertEquals(expected, output, errorString(expected, output));
	}
	
	@Test
	public void translatorInternalVariableBuildTest_alphaVarNameSpaces_Success() {
		String input = "variable Name ECS";
		String expected = "_ABVAR_1_variableNameECS";
		
		String output = translator.buildVariableName(input);
	
		assertEquals(expected, output, errorString(expected, output));
	}

	@Test
	public void translatorInternalVariableBuildTest_alphaNumericVarNameNoSpace_Success() {
		String input = "0variable1Name2";
                String expected = "_ABVAR_1_0variable1Name2";

                String output = translator.buildVariableName(input);

                assertEquals(expected, output, errorString(expected, output));
	}

	@Test
	public void translatorInternalVariableBuildTest_alphaNumericVarNameSpaces_Success() {
		String input = "0 variable 1 name 2";
                String expected = "_ABVAR_1_0variable1name2";

                String output = translator.buildVariableName(input);

                assertEquals(expected, output, errorString(expected, output));
	}

	@Test
	public void translatorInternalVariableBuildTest_allInvalidCharacters_Success() {
		String input = "!!?@#$^#$&*#**(^&";
                String expected = "_ABVAR_1_";

                String output = translator.buildVariableName(input);

                assertEquals(expected, output, errorString(expected, output));
	}

	@Test
	public void translatorInternalVariableBuildTest_leadingAndTrailingWhitespace_Success() {
		String input = "     variable     ";
                String expected = "_ABVAR_1_variable";

                String output = translator.buildVariableName(input);

                assertEquals(expected, output, errorString(expected, output));
	}
}
