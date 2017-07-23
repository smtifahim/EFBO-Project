package ca.queensu.efbo;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.JProgressBar;

import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.SystemColor;

public class EFBOUserInterfaceManager 
{
	public JFrame efboSystemFrame;
	private EFBOValidationManager efboCompManager = new EFBOValidationManager();
	private EFBOStatusReport efboStatusReport;
	
	private JButton btnStepI   = new JButton("STEP   I. LOAD FIRST System's Knowledge");
	private JButton btnStepII  = new JButton("STEP  II. LOAD SECOND System's Knowledge");
	private JButton btnStepIII = new JButton("STEP III. LOAD EFBO-V & Import SYS-1 and SYS-2");
	private JButton btnStepIV  = new JButton("STEP  IV. IDENTIFY the Mapping Events");
	private JButton btnStepV   = new JButton("STEP   V. SAVE Resulting Ontology + Knowledge");
	private JButton btnStepVI  = new JButton("STEP  VI. GENERATE Status Report.");
	
	private Font textFont = new Font(Font.MONOSPACED, Font.BOLD, 14);
	
	public static JProgressBar progressBar; 
	private final JPanel firstPanel = new JPanel();
	private final JPanel secondPanel = new JPanel();
	
	/**
	 * Create the application.
	 */
	public EFBOUserInterfaceManager() throws Exception
	{
		this.initializeGUIElements();
		this.setActionListeners();
	}

	private void setActionListeners()
	{
		//EFBOUserInterfaceManager.progressBar.setValue(25);
		viewProgressBar();
		
		btnStepI.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				progressBar.setValue(5);
				try 
				{
					System.out.println(btnStepI.getText());
					efboCompManager.loadFirstSystem();
					btnStepI.setEnabled(false);
					btnStepII.setEnabled(true);
					
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		});

		btnStepII.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				progressBar.setValue(5);
				try 
				{
					System.out.println(btnStepII.getText());
					efboCompManager.loadSecondSystem();
					btnStepII.setEnabled(false);
					btnStepIII.setEnabled(true);
				} 
				
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
		btnStepIII.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					System.out.println(btnStepIII.getText());
					efboCompManager.loadEFBOValidatorOntology();
					efboCompManager.importLoadedKBases();
					btnStepIII.setEnabled(false);
					btnStepIV.setEnabled(true);
				} 
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
		btnStepIV.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					System.out.println(btnStepIV.getText());
					efboCompManager.setEFBOMappingEvents();
					btnStepIV.setEnabled(false);
					btnStepV.setEnabled(true);
				} 
				
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnStepV.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(btnStepV.getText());
				
				try 
				{
					efboCompManager.saveEFBOValidationOntology();
					efboCompManager.setEFBOInferredOntology();
					efboCompManager.saveEFBOInferredOntology();
					
					efboCompManager.setInconsistentEvents();
					efboCompManager.setEFBOInferredOntology();
					efboCompManager.saveEFBOInferredOntology();
					
					btnStepV.setEnabled(false);
					btnStepVI.setEnabled(true);
				} 
				
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		
		btnStepVI.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(btnStepVI.getText());
				
				try 
				{
				    efboCompManager.saveMergedOntology();
					
					EFBOStatusReport.main(null);
					btnStepV.setEnabled(false);
										
				} 
				
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
	}
	
	
	public void viewProgressBar() 
	{
		  progressBar.setValue(0);

		  int timerDelay = 10;
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

		  progressBar.setValue(progressBar.getMinimum());
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeGUIElements() 
	{
		efboSystemFrame = new JFrame("The EFBO System Interface");
		efboSystemFrame.setBackground(Color.BLACK);
		efboSystemFrame.getContentPane().setBackground(UIManager.getColor("Button.highlight"));
		efboSystemFrame.setBounds(100, 100, 532, 404);
		efboSystemFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnStepII.setEnabled(false); 
		btnStepIII.setEnabled(false);
		btnStepIV.setEnabled(false);
		btnStepV.setEnabled(false);
		btnStepVI.setEnabled(false);
		
		btnStepI.setBounds(43, 54, 432, 39);
		btnStepI.setBackground(SystemColor.menu);
		btnStepI.setFont(textFont);		
		
		btnStepII.setBounds(43, 93, 432, 39);
		btnStepII.setBackground(SystemColor.menu);
		btnStepII.setFont(textFont);
		
		btnStepIII.setBounds(43, 132, 432, 39);
		btnStepIII.setBackground(SystemColor.menu);
		btnStepIII.setFont(textFont);
		
		btnStepIV.setBounds(43, 171, 432, 39);
		btnStepIV.setBackground(SystemColor.menu);
		btnStepIV.setFont(textFont);
		
		btnStepV.setBounds(43, 210, 432, 39);
		btnStepV.setBackground(SystemColor.menu);
		btnStepV.setFont(textFont);
		
		btnStepVI.setBounds(43, 249, 432, 39);
		btnStepVI.setBackground(SystemColor.menu);
		btnStepVI.setFont(textFont);
		
		
		btnStepI.setHorizontalAlignment(SwingConstants.LEFT);
		btnStepII.setHorizontalAlignment(SwingConstants.LEFT);
		btnStepIII.setHorizontalAlignment(SwingConstants.LEFT);
		btnStepIV.setHorizontalAlignment(SwingConstants.LEFT);
		btnStepV.setHorizontalAlignment(SwingConstants.LEFT);
		btnStepVI.setHorizontalAlignment(SwingConstants.LEFT);
		efboSystemFrame.getContentPane().setLayout(null);
		efboSystemFrame.getContentPane().add(btnStepI);
		efboSystemFrame.getContentPane().add(btnStepII);
		efboSystemFrame.getContentPane().add(btnStepIII);
		efboSystemFrame.getContentPane().add(btnStepIV);
		efboSystemFrame.getContentPane().add(btnStepV);
		efboSystemFrame.getContentPane().add(btnStepVI);
		FlowLayout flowLayout = (FlowLayout) secondPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		secondPanel.setBounds(43, 287, 432, 39);
		secondPanel.setBackground(Color.WHITE);
		secondPanel.setBorder(null);
		
		efboSystemFrame.getContentPane().add(secondPanel);
		firstPanel.setBorder(null);
		secondPanel.add(firstPanel);
		firstPanel.setBackground(Color.WHITE);
		progressBar = new JProgressBar();
		progressBar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		progressBar.setStringPainted(true);
		firstPanel.add(progressBar);
		progressBar.setForeground(Color.GREEN);
		progressBar.setBackground(Color.BLACK);
		
		JLabel lblMmxviiFahim = new JLabel("MMXVII \u00A9 Fahim T. Imam. All Rights Reserved.");
		lblMmxviiFahim.setBounds(43, 326, 432, 39);
		lblMmxviiFahim.setBackground(SystemColor.menu);
		lblMmxviiFahim.setHorizontalAlignment(SwingConstants.CENTER);
		lblMmxviiFahim.setForeground(Color.GRAY);
		lblMmxviiFahim.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		efboSystemFrame.getContentPane().add(lblMmxviiFahim);
	}
	
	
} // End of Class EFBOUserInterfaceManager.
