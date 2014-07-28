package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import java.util.Hashtable;

public class ECSPlayNoteTimeBlock extends TranslatorBlock
{

	private Hashtable<String, Integer> notes = new Hashtable<String, Integer>();

	public ECSPlayNoteTimeBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);

		notes.put("B0",   31);

		notes.put("C1",   33);
		notes.put("C1#",  35);
		notes.put("D1",   37);
		notes.put("D1#",  39);
		notes.put("E1",   41);
		notes.put("F1",   44);
		notes.put("F1#",  46);
		notes.put("G1",   49);
		notes.put("G1#",  52);
		notes.put("A1",   55);
		notes.put("A1#",  58);
		notes.put("B1",   62);

		notes.put("C2",   65);
		notes.put("C2#",  69);
		notes.put("D2",   73);
		notes.put("D2#",  78);
		notes.put("E2",   82);
		notes.put("F2",   87);
		notes.put("F2#",  93);
		notes.put("G2",   98);
		notes.put("G2#", 104);
		notes.put("A2",  110);
		notes.put("A2#", 117);
		notes.put("B2",  123);

		notes.put("C3",  131);
		notes.put("C3#", 139);
		notes.put("D3",  147);
		notes.put("D3#", 156);
		notes.put("E3",  165);
		notes.put("F3",  175);
		notes.put("F3#", 185);
		notes.put("G3",  196);
		notes.put("G3#", 208);
		notes.put("A3",  220);
		notes.put("A3#", 233);
		notes.put("B3",  247);

		notes.put("C",   262);
		notes.put("C#",  277);
		notes.put("D",   294);
		notes.put("D#",  311);
		notes.put("E",   330);
		notes.put("F",   349);
		notes.put("F#",  370);
		notes.put("G",   392);
		notes.put("G#",  415);
		notes.put("A",   440);
		notes.put("A#",  466);
		notes.put("B",   494);
		notes.put("C4",  262);
		notes.put("C4#", 277);
		notes.put("D4",  294);
		notes.put("D4#", 311);
		notes.put("E4",  330);
		notes.put("F4",  349);
		notes.put("F4#", 370);
		notes.put("G4",  392);
		notes.put("G4#", 415);
		notes.put("A4",  440);
		notes.put("A4#", 466);
		notes.put("B4",  494);

		notes.put("C5",  523);
		notes.put("C5#", 554);
		notes.put("D5",  587);
		notes.put("D5#", 622);
		notes.put("E5",  659);
		notes.put("F5",  698);
		notes.put("F5#", 740);
		notes.put("G5",  784);
		notes.put("G5#", 831);
		notes.put("A5",  880);
		notes.put("A5#", 932);
		notes.put("B5",  988);

		notes.put("C6",  1047);
		notes.put("C6#", 1109);
		notes.put("D6",  1175);
		notes.put("D6#", 1245);
		notes.put("E6",  1319);
		notes.put("F6",  1397);
		notes.put("F6#", 1480);
		notes.put("G6",  1568);
		notes.put("G6#", 1661);
		notes.put("A6",  1760);
		notes.put("A6#", 1865);
		notes.put("B6",  1976);

		notes.put("C7",  2093);
		notes.put("C7#", 2217);
		notes.put("D7",  2349);
		notes.put("D7#", 2489);
		notes.put("E7",  2637);
		notes.put("F7",  2794);
		notes.put("F7#", 2960);
		notes.put("G7",  3136);
		notes.put("G7#", 3322);
		notes.put("A7",  3520);
		notes.put("A7#", 3729);
		notes.put("B7",  3951);

		notes.put("C8",  4186);
		notes.put("C8#", 4435);
		notes.put("D8",  4699);
		notes.put("D8#", 4978);
	}

	@Override
	public String toCode() throws SocketNullException , SubroutineNotDeclaredException
	{
		TranslatorBlock freqBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock timeBlock = this.getRequiredTranslatorBlockAtSocket(1);

		int CONST_PIN = 23;
		String note = freqBlock.toCode();
		note = note.substring(1, note.length() - 1);
		Integer freq = notes.get(note);

		if (freq == null) {
			System.out.println("ERROR: Unrecognized note used for Play Note Time - " + note + "\n"
				+ "Please ask your teacher for a list of available note names.\n");
			return "// ERROR: Unrecognized note used for Play Note Time - " + note + "\n"
				+ "//\tPlease ask your teacher for a list of available note names.\n";
		}

		String ret = "tone(" + CONST_PIN + ", " + freq + ", " + timeBlock.toCode() + ");\n";
		ret += "\tdelay(" + timeBlock.toCode() + ");\n";
		ret += "\tnoTone(" + CONST_PIN + ");\n";
		return ret;
	}
}
