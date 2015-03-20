package ecs.unittest;

import com.ardublock.translator.Translator;
import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

public class ECSTestUtil {

	public static final Long TEST_ID = new Long(101);
	
	private static final String SETUP_METHOD_HEADER = "void setup()\n{\n";
	private static final String NEWLINE_REGEX = "\n";

	public static Translator getNewTranslator() {
		return new Translator((new WorkspaceController()).getWorkspace());
	}

	public static boolean headersMatch(String actualHeader, String[] expectedDefs, String[] expectedSetup) {
		String[] list = getSetupCode(actualHeader);
	
		String[] actualSetup = getSetupCode(actualHeader);
		String[] actualDefs = getDefinitions(actualHeader);
		expectedSetup = removeEmptyLines(expectedSetup);
		expectedDefs = removeEmptyLines(expectedDefs);

		//System.out.println(actualSetup.length + ", " + expectedSetup.length);
		//System.out.println(actualDefs.length + ", " + expectedDefs.length);

		if (actualSetup.length != expectedSetup.length) return false;
		if (actualDefs.length != expectedDefs.length) return false;

		for (int i = 0; i < expectedSetup.length; i++) {
			if (!actualSetup[i].trim().equals(expectedSetup[i].trim())) {
				//System.out.println(actualSetup[i] + " === " + expectedSetup[i]);
				return false;
			}
		}	

		for (int i = 0; i < expectedDefs.length; i++) {
			if (!actualDefs[i].trim().equals(expectedDefs[i].trim())) {
				//System.out.println(actualDefs[i] + " === " + expectedDefs[i]);
				return false;
			}
		}

		return true;
	}

	public static String setupCommandErrorMessage(String actual, String[] expected) {
		//String[] actualCommands = splitStatements(stripSetupCode(actual));
		String[] actualCommands = splitStatements(actual);
        String ret = "Expected and actual set up commands do not match:\n";
        ret += "\tExpected:\n";
        for (int i = 0; i < expected.length; i++) {
            ret += "\t\t" + expected[i];
            ret += "\n";   
        }                  
        ret += "\tActual:\n";
        for (int i = 0; i < actualCommands.length; i++) {
            ret += "\t\t" + actualCommands[i];
            ret += "\n";
        }
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

	private static String createHeader(String[] defs, String[] setup) {
		return null;
	}
}
