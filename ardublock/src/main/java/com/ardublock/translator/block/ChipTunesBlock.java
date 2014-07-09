package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import java.util.Hashtable;

public class ChipTunesBlock extends TranslatorBlock
{

	private Hashtable<String, Integer> notes = new Hashtable<String, Integer>();

	public ChipTunesBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);

		notes.put("C3", 131);
		notes.put("C3#", 139);
		notes.put("D3", 147);
		notes.put("D3#", 156);
		notes.put("E3", 165);
		notes.put("F3", 175);
		notes.put("F3#", 185);
		notes.put("G3", 196);
		notes.put("G3#", 208);
		notes.put("A3", 220);
		notes.put("A3#", 233);
		notes.put("B3", 247);


		notes.put("C", 262);
		notes.put("C#", 277);
		notes.put("D", 294);
		notes.put("D#", 311);
		notes.put("E", 330);
		notes.put("F", 349);
		notes.put("F#", 370);
		notes.put("G", 392);
		notes.put("G#", 415);
		notes.put("A", 440);
		notes.put("A#", 466);
		notes.put("B", 494);


		notes.put("C4", 262);
		notes.put("C4#", 277);
		notes.put("D4", 294);
		notes.put("D4#", 311);
		notes.put("E4", 330);
		notes.put("F4", 349);
		notes.put("F4#", 370);
		notes.put("G4", 392);
		notes.put("G4#", 415);
		notes.put("A4", 440);
		notes.put("A4#", 466);
		notes.put("B4", 494);


		notes.put("C5", 523);
		notes.put("C5#", 554);
		notes.put("D5", 587);
		notes.put("D5#", 622);
		notes.put("E5", 659);
		notes.put("F5", 698);
		notes.put("F5#", 740);
		notes.put("G5", 784);
		notes.put("G5#", 831);
		notes.put("A5", 880);
		notes.put("A5#", 932);
		notes.put("B5", 988);

/*
		notes.put("C", 262);
		notes.put("C#", 277);
		notes.put("D", 294);
		notes.put("D#", 311);
		notes.put("E", 330);
		notes.put("F", 349);
		notes.put("F#", 370);
		notes.put("G", 392);
		notes.put("G#", 415);
		notes.put("A", 440);
		notes.put("A#", 466);
		notes.put("B", 494);*/
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(1);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(2);

		String note = freqBlock.toCode();
		note = note.substring(1, note.length() - 1);
		Integer freq = notes.get(note);
		if (freq == null) {
			// something
		}

		String ret = "tone(" + pinBlock.toCode() + ", " + freq + ", " + timeBlock.toCode() + ");\n";
		ret += "\tdelay(" + timeBlock.toCode() + ");\n";
		return ret;
	}
}