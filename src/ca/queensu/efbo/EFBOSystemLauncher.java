package ca.queensu.efbo;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JOptionPane;

public class EFBOSystemLauncher 
{
	public static String PROJECT_NAME = null;
	public static String EXTRACTED_KBASES_LOCATION = null;
	public static String EXTRACTED_ANNOTATIONS_LOCATION = null;
	public static String EXTRACTED_ONTOLOGY_LOCATION = null;
	
	/**
	 * Launch the EFBO application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{ 
					this.setProjectName();
					new EFBOSystemExecutionConsole();
					EFBOUserInterfaceManager window = new EFBOUserInterfaceManager();
					window.efboSystemFrame.setVisible(true);
				} 
				
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}

			private void setProjectName() 
			{
				PROJECT_NAME = JOptionPane.showInputDialog (null, "Enter a Name for the Project\n", 
						                                    "EFBO Project Name", 
						                                    JOptionPane.INFORMATION_MESSAGE);
				
				if (PROJECT_NAME==null || (PROJECT_NAME != null && ("".equals(PROJECT_NAME))))
				{
					System.out.println("No Project Name Supplied.");
					System.exit(0);
				}
				
				else
					{
					 		EXTRACTED_ANNOTATIONS_LOCATION = System.getProperty("user.dir") 
					 									   + "/Resources/Extracted-Annotations/" + PROJECT_NAME;
							new File(EXTRACTED_ANNOTATIONS_LOCATION).mkdir();
						
							EXTRACTED_ONTOLOGY_LOCATION = System.getProperty("user.dir") 
									                    + "/Resources/Ontologies/" + PROJECT_NAME + "/";
							new File(EXTRACTED_ONTOLOGY_LOCATION).mkdir();					
					}
						
			}
		});

	}


}
