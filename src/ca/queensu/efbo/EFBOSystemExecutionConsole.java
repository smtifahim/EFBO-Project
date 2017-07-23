package ca.queensu.efbo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class EFBOSystemExecutionConsole extends WindowAdapter 
			implements WindowListener, ActionListener, Runnable
{
	private JFrame efboConsoleFrame;
	private JTextArea consoleTextArea;
	private Thread firstThread;
	private Thread secondThread;
	private JProgressBar progressBar;
	
	private boolean exitThread;
					
	private final PipedInputStream firstPipedInputStream = new PipedInputStream(); 
	private final PipedInputStream secondPipedInputStream = new PipedInputStream(); 

	public EFBOSystemExecutionConsole()
	{
		efboConsoleFrame = new JFrame("EFBO System Execution Console");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
		int x = (int)(frameSize.width/2);
		int y = (int)(frameSize.height/2);
		efboConsoleFrame.setBounds(x,y,frameSize.width,frameSize.height);
		
		consoleTextArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)consoleTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		consoleTextArea.setEditable(false);
		consoleTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		consoleTextArea.setMargin(new Insets(10, 10, 10, 10));
		consoleTextArea.setBackground(Color.BLACK);
		consoleTextArea.setForeground(Color.WHITE);
		
		progressBar = new JProgressBar();
		
			
		
		JButton btnClearConsole = new JButton("Clear Console");
		
		efboConsoleFrame.getContentPane().setLayout(new BorderLayout());
		efboConsoleFrame.getContentPane().add(new JScrollPane(consoleTextArea), BorderLayout.CENTER);
		efboConsoleFrame.getContentPane().add(progressBar,BorderLayout.NORTH);
		efboConsoleFrame.getContentPane().add(btnClearConsole,BorderLayout.SOUTH);
		
		efboConsoleFrame.setVisible(true);		
		
		efboConsoleFrame.addWindowListener(this);		
		btnClearConsole.addActionListener(this);
		
		try
		{
			PipedOutputStream pipedOutputStream = new PipedOutputStream(this.firstPipedInputStream);
			System.setOut(new PrintStream(pipedOutputStream,true)); 
		} 
		
		catch (java.io.IOException io)
		{
			consoleTextArea.append("The STDERR could not be redirected to this console\n" + io.getMessage());
		}
		
		catch (SecurityException se)
		{
			consoleTextArea.append("The STDERR could not be redirected to this console\n" + se.getMessage());
	    } 
		
		try 
		{
			PipedOutputStream pipedInputStream=new PipedOutputStream(this.secondPipedInputStream );
			System.setErr(new PrintStream(pipedInputStream,true));
		} 
		
		catch (java.io.IOException io)
		{
			consoleTextArea.append("The STDERR could not be redirected to this console\n" + io.getMessage());
		}
		
		catch (SecurityException se)
		{
			consoleTextArea.append("The STDERR could not be redirected to this console\n" + se.getMessage());
	    } 		
			
		exitThread = false; // signals the Threads that they should exit
				
		
		// Starting first thread to read from the PipedInputStreams				
		firstThread = new Thread(this);
		firstThread.setDaemon(true);	
		firstThread.start();	
		
		// Starting second thread to read from the PipedInputStreams
		secondThread = new Thread(this);	
		secondThread.setDaemon(true);	
		secondThread.start();	
	}
	
	public synchronized void windowClosed(WindowEvent evt)
	{
		exitThread=true;
		this.notifyAll(); // stop all threads
		try { firstThread.join(1000); firstPipedInputStream.close();   } catch (Exception e){}		
		try { secondThread.join(1000); secondPipedInputStream .close(); } catch (Exception e){}
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt)
	{
		efboConsoleFrame.setVisible(false);
		efboConsoleFrame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		consoleTextArea.setText("");
	}

	public synchronized void run()
	{
		 
		try
		{			
			while (Thread.currentThread()==firstThread)
			{
				
				try { this.wait(100);} catch(InterruptedException ie) {}
				if (firstPipedInputStream.available()!=0)
				{
					String input=this.readLine(firstPipedInputStream);
					consoleTextArea.append(input);
								
				}				
				if (exitThread) return;
			}
		
			while (Thread.currentThread()==secondThread)
			{
				
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (secondPipedInputStream .available()!=0)
				{
					String input=this.readLine(secondPipedInputStream );
					consoleTextArea.append(input);
				}
				if (exitThread) return;
			}
			
		} catch (Exception e)
		{
			consoleTextArea.append("\nConsole reports an Internal error.");
			consoleTextArea.append("The error is: " + e);			
		}
	}
	
	public synchronized String readLine(PipedInputStream pipedInputStream) throws IOException
	{
		String input="";
				
		do
		{
			int available = pipedInputStream.available();
			if (available==0) break;
			byte streamByte[] = new byte[available];
			pipedInputStream.read(streamByte);
			input += new String(streamByte, 0, streamByte.length);
			viewProgressBar(1);
		}
		while (!input.endsWith("\n") &&  !input.endsWith("\r\n") && !exitThread);
			
		return input;
	}
	
	public void viewProgressBar(int value) 
	{

		  progressBar.setStringPainted(true);
		  progressBar.setValue(value);

		  int timerDelay = 5;
		  new javax.swing.Timer(timerDelay, new ActionListener() 
		  {
		     private int index = 0;
		     private int maxIndex = 100;
		     public void actionPerformed(ActionEvent e) 
		     {
		        if (index < maxIndex)
		        {
		           progressBar.setValue(index);
		           index++;
		        } 
		        
		        else 
		        {
		           progressBar.setValue(maxIndex);
		           ((javax.swing.Timer)e.getSource()).stop(); // stop the timer
		        }
		     }
		  }).start();

		 // progressBar.setValue(progressBar.getMinimum());
	}
}