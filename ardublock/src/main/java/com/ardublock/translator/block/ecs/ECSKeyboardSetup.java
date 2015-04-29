package com.ardublock.translator.block.ecs;

import com.ardublock.core.exception.ArdublockException;
import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import jssc.*;
import processing.app.Preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.lang.reflect.*;
import com.ardublock.ui.*;
import processing.app.*;
import com.ardublock.*;

public class ECSKeyboardSetup extends TranslatorBlock
{
	/* Name of key-state array in Arduino code output */
	public static final String KEYS_ARRAY = "keysDown";
	public static final int KEYS_ARRAY_SIZE = 36; // 26 alpha, 10 digits
	public static boolean portOpen = false;

	public boolean isInTest;

	public ECSKeyboardSetup(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.isInTest = false;
	}

	@Override
	public String toCode() throws ArdublockException
	{
		// Start a thread that will wait until uploading to the Arduino is finished before opening the serial port
		if (!isInTest) {
			(new ECSSerialPoll()).start();
		}

		// Create array of booleans for keys with room for 36 keys (10 for digits and 26 for lowercase alpha).
		translator.addDefinitionCommand(String.format("boolean %s[%d];", KEYS_ARRAY, KEYS_ARRAY_SIZE));

		// We don't have any actual Arduino code to output for this block.
		return "";
	}
}


class ECSArdublockSerialGUI extends JFrame implements KeyListener, SerialPortEventListener
{

	// State table for the keys
	private boolean[] keysDown = new boolean[ECSKeyboardSetup.KEYS_ARRAY_SIZE];
	private SerialPort serial;
	private final int BAUD_RATE = SerialPort.BAUDRATE_9600;
	private final int DATA_BITS = SerialPort.DATABITS_8;
	private final int STOP_BITS = SerialPort.STOPBITS_1;
	private final int PARITY    = SerialPort.PARITY_NONE;

	JLabel label;

	ECSArdublockSerialGUI(String title) 
	{
		super(title);

		// Make sure the port is closed when the window is closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing port " + Preferences.get("serial.port"));
				closeSerialPort();
			}
		});


		try {Thread.sleep(1000);} catch (Exception e) {}
		openSerialPort();


		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 300);
		JTextArea text = new JTextArea("");
		label = new JLabel();
		label.setFont(label.getFont().deriveFont(64.0f));

		//text.addKeyListener(this);
		this.addKeyListener(this);
		//this.getContentPane().add(text, BorderLayout.CENTER);
		this.getContentPane().add(label, BorderLayout.CENTER);
		setVisible(true);
	}

	private void closeSerialPort() {
		if (serial == null) return;

		try {
			ECSKeyboardSetup.portOpen = !serial.closePort();
			serial = null;
		}
		catch (SerialPortException e) {
			e.printStackTrace();
		}
	}


	private void openSerialPort() {
		if (serial != null) return;

		System.out.println("Attempting to open serial port.");
		String portName = Preferences.get("serial.port");

		serial = new SerialPort(portName);

		// Make sure the port exists and is no currently owned by another process
		if (serial.isOpened()) {
			System.out.println("Serial port already opened. Unable to establish connection.");
			return;
		}

		try {
			ECSKeyboardSetup.portOpen = serial.openPort();
			serial.setParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
			serial.addEventListener(this);
			System.out.println("Port opened successfully");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

	// Send 'key' down the serial port
	private void sendUpdate(int key) 
	{
		if (serial == null) return;
		try 
		{
			serial.writeInt(key);
		} catch (SerialPortException ex) 
		{
			ex.printStackTrace();
		}
	}

	// Get the index of the keysDown array that correspondes to the given character.
	// 0-9 map to indices 0-9
	// a-z map to indices 10-35
	private int getIndex(char c) 
	{
		if (Character.isDigit(c))
		{
			return (int)(c - '0');
		}

		if (Character.isLetter(c) && Character.isLowerCase(c))
		{
			return (int)((c - 'a') + 10);
		}

		return -1;
	}

	@Override
	public void keyTyped(KeyEvent e) {/* Not used */}

	// Notify the Arduino that this key is no longer being held down
	@Override
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = getIndex(e.getKeyChar());
		if (keyCode == -1) return;
		sendUpdate(keyCode);
		keysDown[keyCode] = false;
		updateKeyLabel();
	}

	// Notify the Arduino that this key is being held down, but only once.
	// Don't send repetitive information down the serial port each time the OS registers a key press
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = getIndex(e.getKeyChar());
		if (keyCode == -1) return;
		// Only send update if this is key was just pressed down.
		// This ignores all calls to keyPressed until keyReleased is called.
		if (!keysDown[keyCode]) 
		{
			sendUpdate(keyCode);
			keysDown[keyCode] = true;
			updateKeyLabel();
		}
	}

	// Update the label in the ECSKeyboard GUI that shows which keys are being pressed
	private void updateKeyLabel() {
		String title = "";
		for (int i = 0; i < keysDown.length; i++) {
			if (keysDown[i]) {
				if (i < 10) {
					title += ", " + (char)(i + '0');
				} else {
					title += ", " + (char)((i - 10) + 'a');
				}
			}
		}
		if (title.length() > 0) {
			title = title.substring(1);
		}
		this.label.setText(title);
	}

	// Print output received from Arduino to stdout.
	@Override
	public void serialEvent(SerialPortEvent e) 
	{
		try {
			if (e.isRXCHAR()) {
				String in = serial.readString();
				System.out.print(in);
			}
		} catch (SerialPortException ex) {
			ex.printStackTrace();
		}
	}
}

class ECSSerialPoll extends Thread 
{

	// Wait for our code to finish being uploaded to the Arduino
	// and then spawn the ECSArdublockSerialGUI window that will establish
	// and manage serial communication with the Arduino
	public void run() 
	{
		waitForUpload();
		new ECSArdublockSerialGUI("ECS Keyboard");
	}
	
	private void waitForUpload() 
	{

		try {
			// Gain access to ArduBlockTool.editor
			ArduBlockTool _abt = new ArduBlockTool();
			Class _abt_class = _abt.getClass();
			Field _abt_field = _abt_class.getDeclaredField("editor");
			_abt_field.setAccessible(true);

			// Gain access to ArduBlockTool.editor.uploading
			Editor _editor = (Editor)_abt_field.get(_abt);
			Class _editor_class = _editor.getClass();
			Field _editor_field = _editor_class.getDeclaredField("uploading");
			_editor_field.setAccessible(true);


			// Wait for ArduBlockTool.editor.uploading to go from false, to true, and then back to false
			boolean triggered = false;
			Boolean uploading = (Boolean)(_editor_field.get(_editor));

			while (!(triggered && !uploading)) {
				if (!triggered && uploading) {
					triggered = true;
				}
				uploading = (Boolean)_editor_field.get(_editor);
			}

			System.out.println("Polling complete.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
