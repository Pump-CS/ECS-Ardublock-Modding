package com.ardublock.translator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.ardublock.translator.adaptor.BlockAdaptor;
import com.ardublock.translator.adaptor.OpenBlocksAdaptor;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.TranslatorBlockFactory;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNameDuplicatedException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;
import com.ardublock.core.exception.ArdublockException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.Workspace;

public class Translator
{
	private static final String variablePrefix = "_ABVAR_";
	private static final String funcPrefix = "_ABFUNC_";

	private Set<String> headerFileSet;
	private Set<String> definitionSet;
	private List<String> setupCommand;
	//private Set<String> functionNameSet;
	private Set<TranslatorBlock> bodyTranslatreFinishCallbackSet;
	private BlockAdaptor blockAdaptor;
	
	private ArrayList<String> inputPinSet;
	private ArrayList<String> outputPinSet;
	
	private Map<String, String> numberVariableSet;
	private Map<String, String> booleanVariableSet;
	private Map<String, String> arrayVariableSet;
	private Map<String, String> functionNameSet;
	
	private Workspace workspace;
	
	private int variableCnt;
	private int funcCount;
	
	public Translator(Workspace ws)
	{
		workspace = ws;
		reset();
	}
	
	public String genreateHeaderCommand()
	{
		StringBuilder headerCommand = new StringBuilder();
		
		if (!headerFileSet.isEmpty())
		{
			for (String file:headerFileSet)
			{
				headerCommand.append("#include <" + file + ">\n");
			}
			headerCommand.append("\n");
		}
		
		if (!definitionSet.isEmpty())
		{
			for (String command:definitionSet)
			{
				headerCommand.append(command + "\n");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSet.isEmpty())
		{
			for (String functionName:functionNameSet.keySet())
			{
				headerCommand.append("void " + functionNameSet.get(functionName) + "();\n");
			}
			headerCommand.append("\n");
		}
		
		headerCommand.append("void setup()\n{\n");
		
		if (!inputPinSet.isEmpty())
		{
			for (String pinNumber:inputPinSet)
			{
				headerCommand.append("pinMode( " + pinNumber + ", INPUT);\n");
			}
		}
		if (!outputPinSet.isEmpty())
		{
			for (String pinNumber:outputPinSet)
			{
				headerCommand.append("pinMode( " + pinNumber + ", OUTPUT);\n");
			}
		}
		
		if (!setupCommand.isEmpty())
		{
			for (String command:setupCommand)
			{
				headerCommand.append(command + "\n");
			}
		}
		
		headerCommand.append("}\n\n");
		return headerCommand.toString();
	}
	
	public String translate(Long blockId) throws ArdublockException
	{
		TranslatorBlockFactory translatorBlockFactory = new TranslatorBlockFactory();
		Block block = workspace.getEnv().getBlock(blockId);
		TranslatorBlock rootTranslatorBlock = translatorBlockFactory.buildTranslatorBlock(this, blockId, block.getGenusName(), "", "", block.getBlockLabel());
		return rootTranslatorBlock.toCode();
	}
	
	public BlockAdaptor getBlockAdaptor()
	{
		return blockAdaptor;
	}
	
	public void reset()
	{
		headerFileSet = new LinkedHashSet<String>();
		definitionSet = new LinkedHashSet<String>();
		setupCommand = new LinkedList<String>();
		//functionNameSet = new HashSet<String>();
		inputPinSet = new ArrayList<String>();
		outputPinSet = new ArrayList<String>();
		bodyTranslatreFinishCallbackSet = new HashSet<TranslatorBlock>();
		
		numberVariableSet = new HashMap<String, String>();
		booleanVariableSet = new HashMap<String, String>();
		arrayVariableSet = new HashMap<String, String>();
		functionNameSet = new HashMap<String, String>();		


		blockAdaptor = buildOpenBlocksAdaptor();
		
		variableCnt = 0;
		funcCount = 0;
	}
	
	private BlockAdaptor buildOpenBlocksAdaptor()
	{
		return new OpenBlocksAdaptor();
	}
	
	public void addHeaderFile(String headerFile)
	{
		if (!headerFileSet.contains(headerFile))
		{
			headerFileSet.add(headerFile);
		}
	}
	
	public void addSetupCommand(String command)
	{
		if (!setupCommand.contains(command))
		{
			setupCommand.add(command);
		}
	}
	
	public void addSetupCommandForced(String command)
	{
		setupCommand.add(command);
	}
	
	public void addDefinitionCommand(String command)
	{
		definitionSet.add(command);
	}
	
	public void addInputPin(String pinNumber)
	{
		inputPinSet.add(pinNumber);
	}
	
	public void addOutputPin(String pinNumber)
	{
		outputPinSet.add(pinNumber);
	}
	
	public String getNumberVariable(String userVarName)
	{
		return numberVariableSet.get(userVarName);
	}
	
	public String getBooleanVariable(String userVarName)
	{
		return booleanVariableSet.get(userVarName);
	}

	public String getArrayVariable(String userVarName)
	{
		return arrayVariableSet.get(userVarName);
	}
	
	public void addNumberVariable(String userVarName, String internalName)
	{
		numberVariableSet.put(userVarName, internalName);
	}
	
	public void addBooleanVariable(String userVarName, String internalName)
	{
		booleanVariableSet.put(userVarName, internalName);
	}
	
	public void addArrayVariable(String userVarName, String internalName)
	{
		arrayVariableSet.put(userVarName, internalName);
	}
	
	public void addFunctionName(Long blockId, String functionName) throws SubroutineNameDuplicatedException
	{
		if (functionName.equals("loop") ||functionName.equals("setup") || functionNameSet.containsKey(functionName))
		{
			throw new SubroutineNameDuplicatedException(blockId);
		}
		
		functionNameSet.put(functionName, buildFunctionName(functionName));
	}
	
	public boolean containFunctionName(String name)
	{
		//return functionNameSet.contains(name.trim());
		return functionNameSet.containsKey(name.trim());
	}

	public String getInternalFunctionName(String name) {
		return functionNameSet.get(name);
	}
	
	
	public String buildVariableName()
	{
		return buildVariableName("");
	}
	
	public String buildVariableName(String reference)
	{
		variableCnt = variableCnt + 1;
		String varName = variablePrefix + variableCnt + "_";
		int i;
		for (i=0; i<reference.length(); ++i)
		{
			char c = reference.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c) || (c == '_'))
			{
				varName = varName + c;
			}
		}
		return varName;
	}

	public String buildFunctionName(String name) {
		funcCount++;
		String funcName = funcPrefix + funcCount + "_";
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c) || (c == '_')) {
				funcName = funcName + c;
			}
		}
		return funcName;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public Block getBlock(Long blockId) {
		return workspace.getEnv().getBlock(blockId);
	}
	
	public void registerBodyTranslateFinishCallback(TranslatorBlock translatorBlock)
	{
		bodyTranslatreFinishCallbackSet.add(translatorBlock);
	}

	public void beforeGenerateHeader() {
		for (TranslatorBlock translatorBlock : bodyTranslatreFinishCallbackSet)
		{
			translatorBlock.onTranslateBodyFinished();
		}
		
	}
	
	public Set<RenderableBlock> findEntryBlocks()
	{
		Set<RenderableBlock> loopBlockSet = new HashSet<RenderableBlock>();
		Iterable<RenderableBlock> renderableBlocks = workspace.getRenderableBlocks();
		
		for (RenderableBlock renderableBlock:renderableBlocks)
		{
			Block block = renderableBlock.getBlock();
			
			if (!block.hasPlug() && (Block.NULL.equals(block.getBeforeBlockID())))
			{
				if(block.getGenusName().equals("loop"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop1"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop2"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop3"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("program"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("setup"))
				{
					loopBlockSet.add(renderableBlock);
				}
			}
		}
		
		return loopBlockSet;
	}
	
	public Set<RenderableBlock> findSubroutineBlocks() throws SubroutineNameDuplicatedException
	{
		Set<RenderableBlock> subroutineBlockSet = new HashSet<RenderableBlock>();
		Iterable<RenderableBlock> renderableBlocks = workspace.getRenderableBlocks();
		
		for (RenderableBlock renderableBlock:renderableBlocks)
		{
			Block block = renderableBlock.getBlock();
			
			if (!block.hasPlug() && (Block.NULL.equals(block.getBeforeBlockID())))
			{
				if (block.getGenusName().equals("subroutine"))
				{
					String functionName = block.getBlockLabel().trim();
					this.addFunctionName(block.getBlockID(), functionName);
					subroutineBlockSet.add(renderableBlock);
				}
				
			}
		}
		
		return subroutineBlockSet;
	}
	
	public String translate(Set<RenderableBlock> loopBlocks, Set<RenderableBlock> subroutineBlocks) throws ArdublockException
	{
		StringBuilder code = new StringBuilder();
		for (RenderableBlock renderableBlock : loopBlocks)
		{
			Block loopBlock = renderableBlock.getBlock();
			code.append(translate(loopBlock.getBlockID()));
		}
		
		for (RenderableBlock renderableBlock : subroutineBlocks)
		{
			Block subroutineBlock = renderableBlock.getBlock();
			code.append(translate(subroutineBlock.getBlockID()));
		}
		beforeGenerateHeader();
		code.insert(0, genreateHeaderCommand());
		
		return code.toString();
	}
}
