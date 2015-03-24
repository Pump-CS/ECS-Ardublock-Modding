package com.ardublock.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ardublock.core.Context;
import com.ardublock.translator.AutoFormat;
import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.*;
import com.ardublock.core.exception.ArdublockException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.Workspace;

public class GenerateCodeButtonListener implements ActionListener
{
	private JFrame parentFrame;
	private Context context;
	private Workspace workspace; 
	private ResourceBundle uiMessageBundle;
	
	public GenerateCodeButtonListener(JFrame frame, Context context)
	{
		this.parentFrame = frame;
		this.context = context;
		workspace = context.getWorkspaceController().getWorkspace();
		uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		boolean success;
		success = true;
		Translator translator = new Translator(workspace);
		translator.reset();
		
		Set<RenderableBlock> loopBlockSet = translator.findEntryBlocks();
		Set<RenderableBlock> subroutineBlockSet;
		try
		{
			subroutineBlockSet = translator.findSubroutineBlocks();
		}
		catch (SubroutineNameDuplicatedException e4)
		{
			Iterable<RenderableBlock> rbs = workspace.getRenderableBlocks();
			String subroutineName = null;
			
			//determine duplicated subroutine name
			for (RenderableBlock rb : rbs)
			{
				if (rb.getBlockID() .equals(e4.getBlockId()))
				{
					subroutineName = rb.getBlock().getBlockLabel().trim();
					break;
				}
			}
			
			//highlight duplicated blocks
			for (RenderableBlock rb : rbs)
			{
				if (rb.getBlock().getBlockLabel().trim().equals(subroutineName))
				{
					context.highlightBlock(rb);
				}
			}
			
			//context.highlightBlock(renderableBlock);
			//find the second subroutine whose name is defined, and make it highlight. though it cannot happen due to constraint of OpenBlocks -_-
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.subroutineNameDuplicated"), "Error", JOptionPane.ERROR_MESSAGE);
			//e4.printStackTrace();
			return ;
			
		}
		
		
		if (loopBlockSet.size() == 0) {
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.noLoopFound"), "Error", JOptionPane.ERROR_MESSAGE);
			return ;
		}
		if (loopBlockSet.size() > 1) {
			for (RenderableBlock rb : loopBlockSet)
			{
				context.highlightBlock(rb);
			}
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.multipleLoopFound"), "Error", JOptionPane.ERROR_MESSAGE);
			return ;
		}
		
		String code = "";

		try
		{
			code = translator.translate(loopBlockSet, subroutineBlockSet);
		} catch (ArdublockException ae) {
			success = false;
			if (ae instanceof SocketNullException) {
				highlightCodeOnException(((SocketNullException)ae).getBlockId());
				
			} else if (ae instanceof SubroutineNotDeclaredException) {
				highlightCodeOnException(((SubroutineNotDeclaredException)ae).getBlockId());
				showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.subroutineNotDeclared"), ae.getMessage());
			} else if (ae instanceof ECSException) {
				ECSException ecsException = (ECSException)ae;
				if (ecsException instanceof InvalidNoteException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidNote"), null);	
				} else if (ecsException instanceof InvalidPinException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidOPin"), null);	
				} else if (ecsException instanceof InvalidKeyException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidKey"), null);	
				} else if (ecsException instanceof InvalidButtonException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidButton"), null);	
				} else if (ecsException instanceof InvalidNumberVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidNumberVariableName"), ecsException.getMessage());
				} else if (ecsException instanceof InvalidNumberDoubleVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidNumberDoubleVariableName"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidBooleanVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidBooleanVariableName"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidArrayVariableNameAccessException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidArrayVariableNameAccess"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidArrayVariableNameCreateException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.InvalidArrayVariableNameCreate"), ecsException.getMessage());	
				} else {
					// Generic ECSException
					System.out.println("ECS Exception");
				}
			} else {
				showErrorPaneWithMessage(ae.getMessage(), null);
			}
		} catch (BlockException e2) {
			e2.printStackTrace();
			success = false;
			highlightCodeOnException(e2.getBlockId());
			
		}
/*
		catch (SocketNullException e1)
		{
			//e1.printStackTrace();
			success = false;
			Long blockId = e1.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock2 : blocks)
			{
				Block block2 = renderableBlock2.getBlock();
				if (block2.getBlockID().equals(blockId))
				{
					context.highlightBlock(renderableBlock2);
					break;
				}
			}
			if (e1 instanceof InvalidNoteException) {
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidNote"), "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidPinException) {
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidPin"), "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidKeyException) {
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidKey"), "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidButtonException) {
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidButton"), "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidNumberVariableNameException) {
				InvalidNumberVariableNameException nameEx = (InvalidNumberVariableNameException)e1;
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidNumberVariableName") + "\n(Tried to use: \"" + nameEx.getMessage() + "\")", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidBooleanVariableNameException) {
				InvalidBooleanVariableNameException nameEx = (InvalidBooleanVariableNameException)e1;
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidBooleanVariableName") + "\n(Tried to use: \"" + nameEx.getMessage() + "\")", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidNumberDoubleVariableNameException) {
				InvalidNumberDoubleVariableNameException nameEx = (InvalidNumberDoubleVariableNameException)e1;
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidNumberDoubleVariableName") + "\n(Tried to use: \"" + nameEx.getMessage() + "\")", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidArrayVariableNameAccessException) {
				InvalidArrayVariableNameAccessException nameEx = (InvalidArrayVariableNameAccessException)e1;
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidArrayVariableNameAccess") + "\n(Tried to use: \"" + nameEx.getMessage() + "\")", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (e1 instanceof InvalidArrayVariableNameCreateException) {
				InvalidArrayVariableNameCreateException nameEx = (InvalidArrayVariableNameCreateException)e1;
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.invalidArrayVariableNameCreate") + "\n(Tried to use: \"" + nameEx.getMessage() + "\")", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.socketNull"), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (BlockException e2)
		{
			e2.printStackTrace();
			success = false;
			Long blockId = e2.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock2 : blocks)
			{
				Block block2 = renderableBlock2.getBlock();
				if (block2.getBlockID().equals(blockId))
				{
					context.highlightBlock(renderableBlock2);
					break;
				}
			}
			JOptionPane.showMessageDialog(parentFrame, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (SubroutineNotDeclaredException e3)
		{
			//e3.printStackTrace();
			success = false;
			Long blockId = e3.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock3 : blocks)
			{
				Block block2 = renderableBlock3.getBlock();
				if (block2.getBlockID().equals(blockId))
				{
					context.highlightBlock(renderableBlock3);
					break;
				}
			}
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle.getString("ardublock.translator.exception.subroutineNotDeclared") + "(Tried: " + e3.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
*/		
		if (success)
		{
			AutoFormat formatter = new AutoFormat();
			String codeOut = code.toString();
			
			if (context.isNeedAutoFormat)
			{
				codeOut = formatter.format(codeOut);
			}
			
			if (!context.isInArduino())
			{
				System.out.println(codeOut);
			}		
			context.didGenerate(codeOut);
		}
	}

	private void highlightCodeOnException(Long id) {
		Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
		for (RenderableBlock renderableBlock : blocks) {
			Block b = renderableBlock.getBlock();
			if (b.getBlockID().equals(id)) {
				context.highlightBlock(renderableBlock);
				break;
			}
		}
	}

	private void showErrorPaneWithMessage(String message, String tried) {
		if (tried != null) {
			message += "(Tried: " + tried + ")";
		}
		JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
