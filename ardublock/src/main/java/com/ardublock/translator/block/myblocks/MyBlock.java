import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.TranslatorBlock;

public class MyBlock extends TranslatorBlock
{
	public MyBlock(Long blockId, Translator translator)
	{
		super(blockId, translator);
	}

	@Override
	public String toCode() throws SocketNullException
	{
		// to define include and global var, see below
		//setupWireEnvironment(translator); 
		// recovery module parameters, here the message placed in row 0
		// So we write the code to generate
		//TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0, "// test\n");
		//this.getRequiredTranslatorBlockAtSocket(0, "lcd.print( ", " );\n");
		// creating the text of the code
		//String ret = translatorBlock.toCode();
		return "";
	}
	
	public static void setupWireEnvironment(Translator t)
	{
		// text for include
		//t.addHeaderFile("LiquidCrystal.h");
		// setup code
		//t.addDefinitionCommand("LiquidCrystal lcd(12, 11, 5, 4, 3, 2);");
	}
}