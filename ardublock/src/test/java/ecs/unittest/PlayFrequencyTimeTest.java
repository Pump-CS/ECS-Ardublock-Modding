package ecs.unittest;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.*;
import com.ardublock.translator.block.ecs.*;

import com.ardublock.translator.block.exception.*;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.Workspace;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class PlayFrequencyTimeTest {

	private static final String SUCCESS_FREQ = "200";
	private static final String SUCCESS_TIME = "2000";
	private static final String PLAYFREQUENCYTIME_TOCODE = "tone(" +
															TranslatorBlock.SPEAKER_PIN + 
															", " +
															SUCCESS_FREQ + 
															", " +
															SUCCESS_TIME + 
															");\n" + 
															"delay(" +
															SUCCESS_TIME +
															");\n" +
															"noTone(" + 
															TranslatorBlock.SPEAKER_PIN +
															");\n";

	private ECSPlayFrequencyTimeBlock blockToTest;
	private Translator translator;

	@BeforeClass
	public void setUp() {
		translator = ECSTestUtil.getNewTranslator();
	}

	@Test
	public void playFrequencyTimeTest_validInput_Success() {
		blockToTest = new ECSPlayFrequencyTimeBlockStub(ECSTestUtil.TEST_ID, translator, "", "", "", SUCCESS_FREQ, SUCCESS_TIME);
		
		try {
			assertEquals(blockToTest.toCode(), PLAYFREQUENCYTIME_TOCODE);
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		}
	}
}

class ECSPlayFrequencyTimeBlockStub extends ECSPlayFrequencyTimeBlock {
	String freq;
	String time;
	
	ECSPlayFrequencyTimeBlockStub(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String freq, String time) {
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.freq = freq;
		this.time = time;
	}

	public TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException {
		if (i == 0) {
			return new NumberBlock(this.blockId + 1, this.translator, this.codePrefix, this.codeSuffix, this.freq);
		} else if (i == 1) {
			return new NumberBlock(this.blockId + 2, this.translator, this.codePrefix, this.codeSuffix, this.time);
		} else {
			throw new SocketNullException(this.blockId);
		}
	}
}
