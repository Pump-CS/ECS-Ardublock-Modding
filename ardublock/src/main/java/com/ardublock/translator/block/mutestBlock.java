import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.TranslatorBlock;

public class mutestBlock extends TranslatorBlock {
	public mutestBlock(Long blockId, Translator translator)
	{
		super(blockId, translator);
	}

	@Override
	public String toCode() throws SocketNullException
	{
		setupWireEnvironment(translator);
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0, "mutest.print( ", " );\n");
		String ret = translatorBlock.toCode();
		return ret;
	}

	public static void setupWireEnvironment(Translator t)
	{
		t.addHeaderFile("LiquidCrystal.h");
		t.addDefinitionCommand("LiquidCrystal lcd(
	}
}
