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
	public static boolean portOpen = false;

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
	private boolean[] keysDown = new boolean[36];
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
		//String portName = ECSKeyboardSetup.getSerialPortName();
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
			System.out.println("port opened successfully");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}


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
		updateKeyLabel();
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
			updateKeyLabel();
		}
	}

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

	private static Field getField(Class clazz, String fieldName)
        throws NoSuchFieldException {
    	try {
    	  return clazz.getDeclaredField(fieldName);
    	} catch (NoSuchFieldException e) {
   	   	Class superClass = clazz.getSuperclass();
   	   		if (superClass == null) {
     	  	 throw e;
     	 	} else {
     	  	 return getField(superClass, fieldName);
     	 	}
    	}
  	}

	public void run() 
	{
		pollPort();
		new ECSArdublockSerialGUI("ECS Keyboard");
	}

	/* Wait for the upload port to disappear and then reappear. */
	private void pollPort() 
	{
/*
		public static void main(String[] args) throws Exception{
    Object myObj = new SomeDerivedClass(1234);
    Class myClass = myObj.getClass();
    Field myField = getField(myClass, "value");
    myField.setAccessible(true); //required if field is not normally accessible
    System.out.println("value: " + myField.get(myObj));
  }

  private static Field getField(Class clazz, String fieldName)
        throws NoSuchFieldException {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      Class superClass = clazz.getSuperclass();
      if (superClass == null) {
        throw e;
      } else {
        return getField(superClass, fieldName);
      }
    }
  }
}
*/

		try {
			ArduBlockTool _abt = new ArduBlockTool();
			Class _abt_class = _abt.getClass();
			Field _abt_field = getField(_abt_class, "editor");
			_abt_field.setAccessible(true);

			Editor _editor = (Editor)_abt_field.get(_abt);
			Class _editor_class = _editor.getClass();
			Field _editor_field = getField(_editor_class, "uploading");
			_editor_field.setAccessible(true);
			System.out.println("========== " + _editor_field.get(_editor) + " ==========");
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

		/*


		SerialNativeInterface serialInterface = new SerialNativeInterface();
		boolean available = true;
		boolean triggered = false;
		String selectedPort = ECSKeyboardSetup.getSerialPortName();
		System.out.println("Polling port " + selectedPort);

		while (!(available && triggered)) 
		{

			boolean found = false;

			//String[] names = SerialPortList.getPortNames("/dev/", java.util.regex.Pattern.compile("cu."));
			String[] names = SerialPortList.getPortNames();
			//String[] names = serialInterface.getSerialPortNames();

			System.out.println("Found:");
			for (String name : names) {
				System.out.println("\t" + name);
				if (name.equals(selectedPort)) {
					available = true;
					found = true;
					break;
				}
			}
			System.out.println("---------\n");

			if ((!found) && available) {
				triggered = true;
				available = false;
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// ignore
			}
		
		}
		
		System.out.println("Polling complete.");
		*/
	}

}
