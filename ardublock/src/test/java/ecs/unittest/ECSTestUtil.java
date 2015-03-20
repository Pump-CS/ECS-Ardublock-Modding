package ecs.unittest;

import com.ardublock.translator.Translator;
import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

public class ECSTestUtil {

	public static final Long TEST_ID = new Long(101);
	
	private static final String SETUP_METHOD_HEADER = "void setup()\n{\n";
	private static final String NEWLINE_REGEX = "\n";
	private static final String SETUP_SECTION = "Setup";
	private static final String DEFINITIONS_SECTION = "Definitions";

	public static Translator getNewTranslator() {
		return new Translator((new WorkspaceController()).getWorkspace());
	}

	public static boolean headersMatch(String actualHeader, String[] expectedDefs, String[] expectedSetup) {
		String[] list = getSetupCode(actualHeader);
	
		String[] actualSetup = getSetupCode(actualHeader);
		String[] actualDefs = getDefinitions(actualHeader);
		expectedSetup = removeEmptyLines(expectedSetup);
		expectedDefs = removeEmptyLines(expectedDefs);

		if (actualSetup.length != expectedSetup.length) {
			return false;
		}
		if (actualDefs.length != expectedDefs.length) {
			return false;
		}

		for (int i = 0; i < expectedSetup.length; i++) {
			if (!actualSetup[i].trim().equals(expectedSetup[i].trim())) {
				System.out.println(errorMessage(actualSetup[i].trim(), expectedSetup[i].trim(), SETUP_SECTION));
				return false;
			}
		}	

		for (int i = 0; i < expectedDefs.length; i++) {
			if (!actualDefs[i].trim().equals(expectedDefs[i].trim())) {
				System.out.println(errorMessage(actualDefs[i].trim(), expectedDefs[i].trim(), DEFINITIONS_SECTION));
				return false;
			}
		}

		return true;
	}

	public static String errorMessage(String actual, String expected, String section) {
		String[] actualCommands = splitStatements(actual);
        String ret = "Expected and actual lines in " + section + " section do not match:\n";
        ret += "\tExpected: " + expected  + "\n";
        ret += "\tActual: " + actual + "\n";
        return ret;
    }

	public static String[] concatArrays(String[] a, String[] b) {
		int aLength = a.length;
		int bLength = b.length;
		String[] ret = new String[aLength + bLength];
		System.arraycopy(a, 0, ret, 0, aLength);
		System.arraycopy(b, 0, ret, aLength, bLength);
		return ret;
	}

	/**
 	*	Split the given string at each new line.
 	*/ 
    private static String[] splitStatements(String code) {
		String[] split = code.split(NEWLINE_REGEX);
        return removeEmptyLines(split);
    }

	private static String[] removeEmptyLines(String[] in) {
		int toRemove = 0;
		String[] ret;
		for (int i = 0; i < in.length; i++) {
			if (in[i].trim().equals("")) toRemove++;
		}
		ret = new String[in.length - toRemove];
		for (int i = 0, j = 0; i < in.length; i++) {
			if (!in[i].trim().equals("")) {
				ret[j++] = in[i];
			}
		}
		return ret;
	}

	/**
 	*	Get the lines of code inside of void setup() {...}
 	*/ 
	private static String[] getSetupCode(String header) {
		if (!header.contains(SETUP_METHOD_HEADER)) return new String[0];

		String code = header.substring(header.indexOf(SETUP_METHOD_HEADER) + SETUP_METHOD_HEADER.length());
		code = code.substring(0, code.length() - 3);

		return splitStatements(code);
	}
	
	private static String[] getDefinitions(String header) {
		return splitStatements(header.substring(0, header.indexOf(SETUP_METHOD_HEADER)));
	}
}
