package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.*;

import processing.app.Preferences;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import gnu.io.*;

public class ECSKeyboardSetup extends TranslatorBlock
{
	public static final String KEYS_ARRAY = "keysDown";

	public ECSArdublockSerialGUI serialGui = null;

	public ECSKeyboardSetup(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		// Start a thread that will wait until uploading to the Arduino is finished before opening the serial port
		(new ECSSerialPoll()).start();

		// Create array of booleans for keys
		translator.addDefinitionCommand("boolean " + KEYS_ARRAY + "[26];");
		return "";
	}
}

class ECSArdublockSerialGUI extends JFrame implements KeyListener, SerialPortEventListener
{

	// State table for the keys
	boolean[] keysDown = new boolean[26];

	private SerialPort serial;
	InputStream input;
  	OutputStream output;

	protected final int BAUD_RATE = 9600;
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

		serial.close();
		serial = null;
	}


	private void openSerialPort() {
		if (serial != null) return;

		// Make sure the port exists and is no currently owned by another process
		try 
		{
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portId = null;

			while (portList.hasMoreElements()) 
			{
				CommPortIdentifier tmp = (CommPortIdentifier)(portList.nextElement());
				if (tmp.getName().equals(Preferences.get("serial.port"))) 
				{
					if (tmp.isCurrentlyOwned()) 
					{
						System.out.println("Port found but is currently owned by: " + tmp.getCurrentOwner());
						break;
					}
					System.out.println("Port found");
					portId = tmp;
					break;
				}
			}

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

	}


	private void sendUpdate(int key) 
	{
		if (serial == null) return;
		try 
		{
			output.write(key);
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {/* Not used */}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = e.getKeyChar() - 'a';
		sendUpdate(keyCode);
		keysDown[keyCode] = false;
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyChar() - 'a';
	
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
		}
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
		System.out.println("Polling port " + Preferences.get("serial.port"));
		boolean available = true;
		boolean triggered = false;
		while (!(available && triggered)) 
		{

			boolean found = false;

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
			}
		}
		System.out.println("Polling complete.");
	}

}
