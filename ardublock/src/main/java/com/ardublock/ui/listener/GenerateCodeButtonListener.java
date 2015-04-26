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
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidNote"), null);	
				} else if (ecsException instanceof InvalidPinException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidOPin"), null);	
				} else if (ecsException instanceof InvalidKeyException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidKey"), null);	
				} else if (ecsException instanceof InvalidButtonException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidButton"), null);	
				} else if (ecsException instanceof InvalidNumberVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidNumberVariableName"), ecsException.getMessage());
				} else if (ecsException instanceof InvalidNumberDoubleVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidNumberDoubleVariableName"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidBooleanVariableNameException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidBooleanVariableName"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidArrayVariableNameAccessException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidArrayVariableNameAccess"), ecsException.getMessage());	
				} else if (ecsException instanceof InvalidArrayVariableNameCreateException) {
					highlightCodeOnException(ecsException.getBlockId());
					showErrorPaneWithMessage(uiMessageBundle.getString("ardublock.translator.exception.invalidArrayVariableNameCreate"), ecsException.getMessage());	
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
			message += "\n\n(Tried: " + tried + ")";
		}
		JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
