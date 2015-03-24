package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.MessageBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.InvalidNoteException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

import java.util.Hashtable;

public class ECSPlayNoteBlock extends TranslatorBlock
{

	public static final Hashtable<String, Integer> notemap = new Hashtable<String, Integer>() {{
		put("C1",   33);
		put("C1#",  35);
		put("D1b",  35);
		put("D1",   37);
		put("D1#",  39);
		put("E1b",  39);
		put("E1",   41);
		put("F1",   44);
		put("F1#",  46);
		put("G1b",  46);
		put("G1",   49);
		put("G1#",  52);
		put("A1b",  52);
		put("A1",   55);
		put("A1#",  58);
		put("B1b",  58);
		put("B1",   62);

		put("C2",   65);
		put("C2#",  69);
		put("D2b",  69);
		put("D2",   73);
		put("D2#",  78);
		put("E2b",  78);
		put("E2",   82);
		put("F2",   87);
		put("F2#",  93);
		put("G2b",  93);
		put("G2",   98);
		put("G2#", 104);
		put("G2b", 104);
		put("A2",  110);
		put("A2#", 117);
		put("B2b", 117);
		put("B2",  123);

		put("C3",  131);
		put("C3#", 139);
		put("D3b", 139);
		put("D3",  147);
		put("D3#", 156);
		put("E3b", 156);
		put("E3",  165);
		put("F3",  175);
		put("F3#", 185);
		put("G3b", 185);
		put("G3",  196);
		put("G3#", 208);
		put("A3b", 208);
		put("A3",  220);
		put("A3#", 233);
		put("B3b", 233);
		put("B3",  247);

		put("C",   262);
		put("C#",  277);
		put("Db",  277);
		put("D",   294);
		put("D#",  311);
		put("Eb",  311);
		put("E",   330);
		put("F",   349);
		put("F#",  370);
		put("Gb",  370);
		put("G",   392);
		put("G#",  415);
		put("Ab",  415);
		put("A",   440);
		put("A#",  466);
		put("Bb",  466);
		put("B",   494);
		put("C4",  262);
		put("C4#", 277);
		put("D4b", 277);
		put("D4",  294);
		put("D4#", 311);
		put("E4b", 311);
		put("E4",  330);
		put("F4",  349);
		put("F4#", 370);
		put("G4b", 370);
		put("G4",  392);
		put("G4#", 415);
		put("A4b", 415);
		put("A4",  440);
		put("A4#", 466);
		put("B4b", 466);
		put("B4",  494);

		put("C5",  523);
		put("C5#", 554);
		put("D5b", 554);
		put("D5",  587);
		put("D5#", 622);
		put("E5b", 622);
		put("E5",  659);
		put("F5",  698);
		put("F5#", 740);
		put("G5b", 740);
		put("G5",  784);
		put("G5#", 831);
		put("A5b", 831);
		put("A5",  880);
		put("A5#", 932);
		put("B5b", 932);
		put("B5",  988);

		put("C6",  1047);
		put("C6#", 1109);
		put("D6b", 1109);
		put("D6",  1175);
		put("D6#", 1245);
		put("E6b", 1245);
		put("E6",  1319);
		put("F6",  1397);
		put("F6#", 1480);
		put("G6b", 1480);
		put("G6",  1568);
		put("G6#", 1661);
		put("A6b", 1661);
		put("A6",  1760);
		put("A6#", 1865);
		put("B6b", 1865);
		put("B6",  1976);

		put("C7",  2093);
		put("C7#", 2217);
		put("D7b", 2217);
		put("D7",  2349);
		put("D7#", 2489);
		put("E7b", 2489);
		put("E7",  2637);
		put("F7",  2794);
		put("F7#", 2960);
		put("G7b", 2960);
		put("G7",  3136);
		put("G7#", 3322);
		put("A7b", 3322);
		put("A7",  3520);
		put("A7#", 3729);
		put("B7b", 3729);
		put("B7",  3951);

		put("C8",  4186);
		put("C8#", 4435);
		put("D8b", 4435);
		put("D8",  4699);
		put("D8#", 4978);
		put("E8b", 4978);

	}};

	public ECSPlayNoteBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);

		String note = freqBlock.toCode();
		note = note.substring(1, note.length() - 1);
		Integer freq = notemap.get(note);

		if (freq == null) {
			throw new InvalidNoteException(blockId);
		}

		String ret = "tone(" + SPEAKER_PIN + ", " + freq + ");\n";
		return ret;
	}
}
