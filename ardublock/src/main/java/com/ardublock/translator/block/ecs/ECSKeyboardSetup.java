package com.ardublock.translator.block.ecs;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.*;
import com.ardublock.core.exception.ArdublockException;
import processing.app.Preferences;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
//import gnu.io.*;


import jssc.*;

/* TODO
	- Make this WAY more robust. The port should be closed in every conceivable case of the program being closed.
	- digits 0-9 should map to indices 0-9 and a-z should map from indices 10-35.
	- Make the window that pops up for them to type into much better looking and less janky.
*/

public class ECSKeyboardSetup extends TranslatorBlock
{
	/* Name of key-state array in Arduino code output */
	public static final String KEYS_ARRAY = "keysDown";

	public ECSArdublockSerialGUI serialGui = null;

	public ECSKeyboardSetup(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws ArdublockException
	{
		// Start a thread that will wait until uploading to the Arduino is finished before opening the serial port
		(new ECSSerialPoll()).start();

		// Create array of booleans for keys with room for 36 keys (10 for digits and 26 for lowercase alpha).
		translator.addDefinitionCommand("boolean " + KEYS_ARRAY + "[36];");

		// We don't have any actual Arduino code to output for this block.
		return "";
	}

	public static String getSerialPortName() {
		String name = Preferences.get("serial.port");
		SerialNativeInterface serialInterface = new SerialNativeInterface();

		if (serialInterface.getOsType() == SerialNativeInterface.OS_MAC_OS_X) {
			name = name.replace("cu.", "tty.");
		}

		return name;
	}
}


class ECSArdublockSerialGUI extends JFrame implements KeyListener, SerialPortEventListener
{

	// State table for the keys
	boolean[] keysDown = new boolean[36];

	private SerialPort serial;
	InputStream input;
  	OutputStream output;

	protected final int BAUD_RATE = SerialPort.BAUDRATE_9600;
	protected final int DATA_BITS = SerialPort.DATABITS_8;
	protected final int STOP_BITS = SerialPort.STOPBITS_1;
	protected final int PARITY    = SerialPort.PARITY_NONE;

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
		text.addKeyListener(this);
		this.getContentPane().add(text, BorderLayout.CENTER);
		setVisible(true);
	}

	private void closeSerialPort() {
		if (serial == null) return;

		try {
			serial.closePort();
			serial = null;
		}
		catch (SerialPortException e) {
			e.printStackTrace();
		}
	}


	private void openSerialPort() {
		if (serial != null) return;

		System.out.println("Attempting to open serial port.");
		//String portName = ECSKeyboardSetup.getSerialPortName();
		String portName = Preferences.get("serial.port");

		serial = new SerialPort(portName);

		// Make sure the port exists and is no currently owned by another process
		if (serial.isOpened()) {
			System.out.println("Serial port already opened. Unable to establish connection.");
			return;
		}

		try {
			serial.openPort();
			serial.setParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
			serial.addEventListener(this);
			System.out.println("port opened successfully");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

/*
			if (portId == null) 
			{
				System.out.println("Serial port not found");
				return;
			}


			// The serial port has been found and is free to claim. Open the port and set params.
			System.out.println("Opening port " + Preferences.get("serial.port"));
			serial = (SerialPort) (portId.open(this.getClass().getName(), 2000));
			serial.setSerialPortParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
			serial.addEventListener(this);

			// Only notify when data is available
			serial.notifyOnDataAvailable(true);

			input = serial.getInputStream();
			output = serial.getOutputStream();
		} catch (PortInUseException e) 
		{
			System.out.println("Port already in use.");
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		*/

	}


	private void sendUpdate(int key) 
	{
		if (serial == null) return;
		try 
		{
			System.out.println("sending update");
			serial.writeByte((byte)0);
			//output.write(key);
		} catch (SerialPortException ex) 
		{
			ex.printStackTrace();
		}
	}

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

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = getIndex(e.getKeyChar());
		if (keyCode == -1) return;
		sendUpdate(keyCode);
		keysDown[keyCode] = false;
	}

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
		}
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
		/*
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
		{
			try 
			{
				while (input.available() > 0) 
				{
					System.out.print((char)input.read());
				}
			} catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}*/
	}
}

class ECSSerialPoll extends Thread 
{

	public void run() 
	{
		pollPort();
		new ECSArdublockSerialGUI("ECS Keyboard");
	}

	/* Wait for the upload port to disappear and then reappear. */
	private void pollPort() 
	{

		SerialNativeInterface serialInterface = new SerialNativeInterface();
		System.out.println("Polling port " + Preferences.get("serial.port"));
		boolean available = true;
		boolean triggered = false;
		String selectedPort = ECSKeyboardSetup.getSerialPortName();

		while (!(available && triggered)) 
		{

			boolean found = false;

			//String[] names = SerialPortList.getPortNames("/dev/", java.util.regex.Pattern.compile("cu."));
			String[] names = SerialPortList.getPortNames();
			//String[] names = serialInterface.getSerialPortNames();

			System.out.println("Ports found: " + names.length);

			for (String name : names) {
				if (name.equals(selectedPort)) {
					System.out.println("Available and found");
					available = true;
					found = true;
					break;
				}
			}

			if (found) {
				System.out.println("Found");
			} else {
				System.out.println("Not found");
			}

			if ((!found) && available) {
				System.out.println("Not found and available.");
				triggered = true;
				available = false;
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// ignore
			}
/*
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) 
			{
				CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) 
				{
					if (portId.getName().equals(Preferences.get("serial.port"))) 
					{
						available = true;
						found = true;
						break;
					}
				}
			}
			if ((!found) && available) 
			{
				triggered = true;
				available = false;
			}

			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}*/
		
		}
		System.out.println("Polling complete.");
	}

}
