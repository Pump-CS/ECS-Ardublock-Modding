package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class PlayFrequencyTest {

	private static final String SUCCESS_FREQ = "200";

	private static final String PLAYFREQUENCY_TOCODE = "tone(" + TranslatorBlock.SPEAKER_PIN + ", " + SUCCESS_FREQ + ");\n";

	private ECSPlayFrequencyBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void playFrequencyTest_validInput_Success() {
		blockToTest = new ECSPlayFrequencyBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_FREQ);
		
		try {
			assertEquals(blockToTest.toCode(), PLAYFREQUENCY_TOCODE);
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		}
	}
}

class ECSPlayFrequencyBlockStub extends ECSPlayFrequencyBlock {
	String freq;
	
	ECSPlayFrequencyBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String freq) {
		super(blockId + 1, translator, codePrefix, codeSuffix, label);
		this.freq = freq;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException {
		if (i == 0) {
			return new NumberBlock(this.blockId, this.translator, this.codePrefix, this.codeSuffix, this.freq);
		} else {
			throw new SocketNullException(this.blockId);
		}
	}
}
