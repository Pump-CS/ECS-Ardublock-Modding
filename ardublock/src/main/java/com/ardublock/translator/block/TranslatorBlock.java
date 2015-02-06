package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.adaptor.BlockAdaptor;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

abstract public class TranslatorBlock
{
	abstract public String toCode() throws SocketNullException, SubroutineNotDeclaredException;
	
	protected Long blockId;
	
	private BlockAdaptor blockAdaptor;
	
	protected Translator translator;
	
	protected String label;
	protected String comment;
	
	protected String codePrefix;
	protected String codeSuffix;

	public static final String SERIAL_PIN_1 =   "0";
	public static final String SERIAL_PIN_2 =   "1";
	public static final String TRANSMIT_PIN_1 = "2";
	public static final String TRANSMIT_PIN_2 = "3";
	public static final String RECEIVE_PIN =    "4";
	public static final String LED_PIN_1 =      "5";
	public static final String BUTTON_PIN_1 =   "6";
	public static final String LED_PIN_2 =      "7";
	public static final String BUTTON_PIN_2 =   "8";
	public static final String LED_PIN_3 =      "9";
	public static final String BUTTON_PIN_3 =  "10";
	public static final String SPEAKER_PIN =   "11";
	public static final String BUTTON_PIN_4 =  "12";
	public static final String LED_PIN_4 =     "13";

	public static final String FREE_PIN_1 =    "18";
	public static final String FREE_PIN_2 =    "19";
	public static final String FREE_PIN_3 =    "20";
	public static final String FREE_PIN_4 =    "21";
	public static final String FREE_PIN_5 =    "22";
	public static final String FREE_PIN_6 =    "23";
	
	protected TranslatorBlock(Long blockId, Translator translator)
	{
		this.blockId = blockId;
		this.translator = translator;
		this.blockAdaptor = translator.getBlockAdaptor();
		this.codePrefix = "";
		this.codeSuffix = "";
		this.label = "";
	}
	
	protected TranslatorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix)
	{
		this.blockId = blockId;
		this.translator = translator;
		this.blockAdaptor = translator.getBlockAdaptor();
		this.codePrefix = codePrefix;
		this.codeSuffix = codeSuffix;
		this.label = "";
	}
	
	public TranslatorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		this.blockId = blockId;
		this.translator = translator;
		this.blockAdaptor = translator.getBlockAdaptor();
		this.codePrefix = codePrefix;
		this.codeSuffix = codeSuffix;
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	protected Translator getTranslator()
	{
		return translator;
	}
	
	public TranslatorBlock nextTranslatorBlock()
	{
		return this.nextTranslatorBlock("", "");
	}
	
	protected TranslatorBlock nextTranslatorBlock(String codePrefix, String codeSuffix)
	{
		return blockAdaptor.nextTranslatorBlock(this.translator, blockId, codePrefix, codeSuffix);
	}
	
	protected TranslatorBlock getTranslatorBlockAtSocket(int i)
	{
		return this.getTranslatorBlockAtSocket(i, "", "");
	}
	
	protected TranslatorBlock getTranslatorBlockAtSocket(int i, String codePrefix, String codeSuffix)
	{
		return blockAdaptor.getTranslatorBlockAtSocket(this.translator, blockId, i, codePrefix, codeSuffix);
	}
	
	protected TranslatorBlock getRequiredTranslatorBlockAtSocket(int i) throws SocketNullException
	{
		return this.getRequiredTranslatorBlockAtSocket(i, "", "");
	}
	
	protected TranslatorBlock getRequiredTranslatorBlockAtSocket(int i, String codePrefix, String codeSuffix) throws SocketNullException
	{
		TranslatorBlock translatorBlock = blockAdaptor.getTranslatorBlockAtSocket(this.translator, blockId, i, codePrefix, codeSuffix);
		if (translatorBlock == null)
		{
			throw new SocketNullException(blockId);
		}
		return translatorBlock;
	}
	
	protected void setComment(String comment)
	{
		this.comment = comment;
	}
	
	protected String getComment()
	{
		return this.comment;
	}
	
	public void onTranslateBodyFinished(){}
	
}
