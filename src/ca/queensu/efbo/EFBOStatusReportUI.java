package ca.queensu.efbo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class EFBOStatusReportUI extends JFrame 
{

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					EFBOStatusReportUI frame = new EFBOStatusReportUI();
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EFBOStatusReportUI() 
	{
		setForeground(Color.WHITE);
		setTitle("EFBO Status Report Options");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 230);
		getContentPane().setLayout(new MigLayout("", "[][][][][][]", "[][][][][][][][][]"));
		
		JLabel lblSelectTheEfbo = new JLabel("Select the EFBO Functional Reasoning Categories");
		lblSelectTheEfbo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(lblSelectTheEfbo, "cell 4 1 2 1,alignx center");
		
		JCheckBox chckbxFlowOfEvents = new JCheckBox("Flow of Events");
		getContentPane().add(chckbxFlowOfEvents, "cell 4 3");
		
		
		JCheckBox chckbxActivitiesByAgents = new JCheckBox("Activities by Agents");
		getContentPane().add(chckbxActivitiesByAgents, "cell 5 3");
		
		JCheckBox chckbxActionByActions = new JCheckBox("Action by Actions Agents");
		getContentPane().add(chckbxActionByActions, "cell 4 4");
		
		JCheckBox chckbxEventsByEvent = new JCheckBox("Events by Event Interface");
		getContentPane().add(chckbxEventsByEvent, "cell 5 4");
		
		JCheckBox chckbxEventsByTriggering = new JCheckBox("Events by Triggering Agents");
		getContentPane().add(chckbxEventsByTriggering, "cell 4 5");
		
		JCheckBox chckbxDecesionPointActivities = new JCheckBox("Decesion Point Activities");
		getContentPane().add(chckbxDecesionPointActivities, "cell 5 5");
		
		JButton btnReportButton = new JButton("Generate Status Report");
		btnReportButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if (chckbxFlowOfEvents.isSelected())
				{
					System.out.println(chckbxFlowOfEvents.getLabel());
				}
			}
		});
		getContentPane().add(btnReportButton, "cell 4 7 2 1,alignx center,aligny center");
	}

}
