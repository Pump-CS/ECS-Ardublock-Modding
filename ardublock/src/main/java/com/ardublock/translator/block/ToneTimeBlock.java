package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ToneTimeBlock extends TranslatorBlock
{
	public ToneTimeBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(1);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(2);

		if (!(pinBlock.toCode().equals("23")) && !(pinBlock.toCode().equals("18"))
			&& !(pinBlock.toCode().equals("19")) && !(pinBlock.toCode().equals("20"))
			&& !(pinBlock.toCode().equals("21")) && !(pinBlock.toCode().equals("22"))) {;
			System.out.println("ERROR: Invalid pin used for Play Frequency Time - " + pinBlock.toCode() + "\n"
				+ "\tFor built-in speaker, please use ECS blocks.\n"
				+ "\tFor second speaker, available pins are 18-22.\n");
			return "// ERROR: Invalid pin used for Play Frequency Time - " + pinBlock.toCode() + "\n"
				+ "//\tFor built-in speaker, please use ECS blocks.\n"
				+ "//\tFor second speaker, available pins are 18-22.\n";
		}

		String ret = "tone(" + pinBlock.toCode() + ", " + freqBlock.toCode() + ", " + timeBlock.toCode() + ");\n";
		ret += "\tdelay(" + timeBlock.toCode() + ");\n"; 
		ret += "\tnoTone(" + pinBlock.toCode() + ");\n";
		return ret;
	}
}
