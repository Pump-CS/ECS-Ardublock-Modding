package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidNoteException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import java.util.Hashtable;

public class ECSPlayNoteTimeBlock extends TranslatorBlock
{

	public ECSPlayNoteTimeBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(1);

		String note = freqBlock.toCode();
		note = note.substring(1, note.length() - 1);
		Integer freq = ECSPlayNoteBlock.notemap.get(note);

		if (freq == null) {
			throw new InvalidNoteException(blockId);
		}

		String ret = "tone(" + SPEAKER_PIN + ", " + freq + ", " + timeBlock.toCode() + ");\n";
		ret += "\tdelay(" + timeBlock.toCode() + ");\n";
		ret += "\tnoTone(" + SPEAKER_PIN + ");\n";
		return ret;
	}
}
