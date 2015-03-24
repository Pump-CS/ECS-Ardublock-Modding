package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidNoteException;
import com.ardublock.translator.block.exception.InvalidPinException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

import java.util.Hashtable;

public class ECSToneNoteBlock extends TranslatorBlock
{

	public ECSToneNoteBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(1);

		String note = freqBlock.toCode();
		note = note.substring(1, note.length() - 1);
		Integer freq = ECSPlayNoteBlock.notemap.get(note);

		if (freq == null) {
			throw new InvalidNoteException(blockId);
		}	
		if (!(pinBlock.toCode().equals(SPEAKER_PIN)) && !(pinBlock.toCode().equals(FREE_PIN_1))
			&& !(pinBlock.toCode().equals(FREE_PIN_2)) && !(pinBlock.toCode().equals(FREE_PIN_3))
			&& !(pinBlock.toCode().equals(FREE_PIN_4)) && !(pinBlock.toCode().equals(FREE_PIN_5))
			&& !(pinBlock.toCode().equals(FREE_PIN_6))) {
			throw new InvalidPinException(blockId);
		}

		String ret = "tone(" + pinBlock.toCode() + ", " + freq + ");\n";
		return ret;
	}
}
