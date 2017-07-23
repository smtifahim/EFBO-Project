package ca.queensu.efbo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Fahim Imam.
 *
 */
public class EFBOAnnotationExtractionManager 
{
	private String systemName;
	private JFileChooser fileChooser;
	private File[] selectedFiles;
	private File extractedAnnotationsFile;
	private Set<EFBOAnnotation> annotations;
	private EFBOOntologyManager efboCoreManager;
	private OWLOntology efboCore;
	private String extractedAnnotationFilePath;
	
	private static final String defaultFilePath = System.getProperty("user.dir") 
								   				+ "/Resources/Annotated-Sources";
	
	private static final String 
	EFBO_CORE_URI = "http://www.cs.queensu.ca/~imam/ontologies/efbo.owl";
	
	public EFBOAnnotationExtractionManager() throws Exception
	{	
		systemName = null;
		fileChooser = null;
		selectedFiles = null;
	    extractedAnnotationsFile = null;
		annotations = null;
		efboCoreManager = null;
		efboCore = null;
		extractedAnnotationFilePath = null;
		
	} //End of method public AnnotationExtractor().
   	
	public void loadAnnotatedFiles() throws Exception
	{
		  fileChooser = new JFileChooser(new File(defaultFilePath));
		  fileChooser.setDialogTitle("Select Annotated Files");
		  fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		  fileChooser.setMultiSelectionEnabled(true);
		  fileChooser.showOpenDialog(null);
		  selectedFiles = fileChooser.getSelectedFiles();
		  
		  if (selectedFiles.length==0)
		  {
			  this.showNoFileSelectedMessage();
		  }
					  
		  else
		  {
			  this.loadEFBOCoreOntology();
			  annotations = new HashSet<EFBOAnnotation>();
			  this.processSelectedFiles(selectedFiles);
		  } 
		  		
	}
	
    // Load the core EFBO which will be used to check 
    // the validity of the property names 
    // of the source annotations.
	private void loadEFBOCoreOntology() throws OWLOntologyCreationException
	{
		this.efboCoreManager = new EFBOOntologyManager();
	    this.efboCoreManager.loadOntology("EFBO-CORE", EFBO_CORE_URI);
	    this.efboCore = efboCoreManager.getLoadedOntology();
	}
	
	public Set <EFBOAnnotation> getExtractedAnnotations()
	{
		return this.annotations;
	}
	
	private Set<EFBOAnnotation> extractAnnotations(File inputFile)
	 {
		//annotations = new HashSet<EFBOAnnotation>();
		
		try
		 {
	   		 String fileLocation = inputFile.getCanonicalPath();
	   		 FileInputStream fileInputStream = new FileInputStream(inputFile);
			 DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			 BufferedReader bufferReader = new BufferedReader(new InputStreamReader(dataInputStream));
			 
			 int lineNumber = 0;
			 String strLine;
			 boolean annotationFound = false;
			 
			 //Read file. 
			 while ((strLine = bufferReader.readLine()) != null)
			  	{
				  lineNumber += 1;
				  if (strLine.contains("@EFBO:"))
				  {
					    annotationFound = true;
					    String annotatedText = strLine.replaceFirst((".*.@EFBO.*:"), "").trim(); //Remove all the characters until @EFBO:
						annotatedText = annotatedText.replace(".", "").trim();				
						String[] parsedAnnotations = parseAnnotatedText(annotatedText);
					  	
						if (parsedAnnotations != null && parsedAnnotations.length==3)
					  	{
					  	 System.out.println("Annotation @Line#" + lineNumber + "> " + annotatedText);
					  	 EFBOAnnotation  currentAnnotation= new EFBOAnnotation(fileLocation, lineNumber, annotatedText);	
					  	 currentAnnotation.setSubject(parsedAnnotations[0]);
					  	 currentAnnotation.setPredicate(parsedAnnotations[1]);
					  	 currentAnnotation.setObject(parsedAnnotations[2]);
					  	 if  (propertyNameDoesExist(currentAnnotation))
					  		 annotations.add(currentAnnotation);				  	 
					    }
					  	else
					  	{
					  		showParsingErrorMessage(lineNumber, fileLocation);
					  		System.exit(1);
					    }
				   } //End of statement if (strLine.contains("@EFBO:")).
			  				
			  }//End of while loop.
			 	
			  if (!annotationFound)
				  System.out.println("No Annotation Found.");
			  
			  dataInputStream.close();
						
		 }//end of try.
		 	
		 	catch (Exception e)
		 		{
			    	System.err.println("Error: " + e.getMessage());
		 		}
	 
	   		return annotations;
	 }
	
	private static String[] parseAnnotatedText(String annotatedText)
	{
		  annotatedText = annotatedText.replaceAll("\"", " \" ").trim();
		  String[] fragments = annotatedText.split("\\s+");

		  int current = 0;
		  boolean matched = fragments[current].matches("[^\"]*");
		  if (matched) current++;

		  for (int i = 1; i < fragments.length; i++)
		  {
		    if (!matched)
		      fragments[current] = fragments[current] + " " + fragments[i];

		    if (!fragments[current].matches("(\"[^\"]*\"|[^\"]*)"))
		      matched = false;
		    
		    else 
		    {
		      matched = true;

		      if (fragments[current].matches("\"[^\"]*\""))
		        fragments[current] = fragments[current].substring(1, fragments[current].length() - 1).trim();

		      if (fragments[current].length() != 0)
		        current++;

		      if (i + 1 < fragments.length)
		        fragments[current] = fragments[i + 1];
		    }
		  } //End of for loop.

		  if (matched) 
		  { 
		    return Arrays.copyOf(fragments, current);
		  }

		  return null; // if double-quotes did not match properly.
		}

	
	// To check if the property name in the annotation does correspond to the
	// property name within the EFBO core ontology.
	private boolean propertyNameDoesExist(EFBOAnnotation annotation)
	{
		String propertyName = annotation.getPredicate();
		
		if (propertyName.equals("hasTimePoint"))
			return true;
		
		IRI propertyIRI = IRI.create(EFBO_CORE_URI + "#" + propertyName);
		
		if (efboCore.containsObjectPropertyInSignature(propertyIRI))
			return true;
		
		else
		{
			String message = "\nERROR: Non-existing property name: " + propertyName
					       + "\nPlease check your annotation @Line: " + annotation.getLineNumber()
					       + "\nLocation: " + annotation.getFileLocation();
			
			System.out.println(message);
			JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(1);		    
		}
		return false;
	}
	
	private void showParsingErrorMessage(int lineNumber, String fileLocation)
	{
	  	 String messageParsingFailed = "\nERROR! Parsing Annotation Failed."
					+ "\nCheck Your Annoatation @Line#" 
	  			    + lineNumber + " of the Following File."
					+ "\nFile Location> " + fileLocation;
	  						  		
	  	 System.out.println(messageParsingFailed);
	  	 JOptionPane.showMessageDialog(null, messageParsingFailed, 
	  			 					   "ERROR", JOptionPane.ERROR_MESSAGE);
  	 
	} // End of method showParsingErrorMessage().
	
	public File getExtractedAnnotationsFile()
	{
		return extractedAnnotationsFile;
	}

	public void saveExtractedAnnotations()
	{
		// parent component of the dialog
		JFrame fileSaveFrame = new JFrame();
		//String defaultFilePath = System.getProperty("user.dir") + "/Resources/Extracted-Annotations"; 
		String fileSavePath = this.getExtractedAnnotationFilePath(); 
		JFileChooser fileChooser = new JFileChooser(new File(fileSavePath));
		fileChooser.setDialogTitle("Specify the file to save your annotations");
		fileChooser.setSelectedFile(new File(this.getSystemName().trim() + "_annotations.txt"));
		 
		int userSelection = fileChooser.showSaveDialog(fileSaveFrame);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) 
		{
		    File fileToSave = fileChooser.getSelectedFile();
		    System.out.println("\nSave Annotations Location> " + fileToSave.getAbsolutePath());
		    
		    try 
			{
				FileWriter fileWriter = new FileWriter(fileToSave.getAbsolutePath());
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				EFBOUserInterfaceManager.progressBar.setValue(50);
				String fileLocation = "", prevLocation = "";
				String messageText="\n\nFollowing is the list of annotations:";
				
				for (EFBOAnnotation annotation : annotations)
				 {	
					fileLocation = annotation.getFileLocation();
					if (fileLocation != prevLocation)
					{
						bufferedWriter.write("\nLocation:" + fileLocation + "\n");
						messageText += "\nLocation:" + fileLocation + "\n";						
					}
					
					bufferedWriter.write("Line:" + annotation.getLineNumber() + "\t");
					bufferedWriter.write(annotation.getAnnotatedText() + "\n");
					
					messageText += "Annotation @Line#" + annotation.getLineNumber() + "> "
							    + annotation.getAnnotatedText() + "\n";
					
					prevLocation = fileLocation;
				 }
				
				this.extractedAnnotationsFile=fileToSave;
				bufferedWriter.close();
				
				EFBOUserInterfaceManager.progressBar.setValue(75);
				
				String messageSavedSuccess = "Annotations Saved Successfully!\n"
							    			 + "Save Location> " 
							    			 + fileToSave.getAbsolutePath();
				System.out.println(messageSavedSuccess);				
							
			    JTextArea textArea = new JTextArea(25, 90);
			    textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			    textArea.append(messageSavedSuccess + messageText);
			    textArea.setCaretPosition(0);
			    textArea.setMargin(new Insets(10, 10, 10, 10));
			   	textArea.setEditable(false);
		        textArea.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), 
		        		           "Extracted Anotations", TitledBorder.CENTER,
		        		           TitledBorder.TOP, null, new Color(0, 0, 0)));
		        JOptionPane.showMessageDialog(fileSaveFrame, new JScrollPane(textArea), "Success!", 
						  JOptionPane.INFORMATION_MESSAGE);
				
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
			}//End of catch 
			
		} //End of if (userSelection == JFileChooser.APPROVE_OPTION) 
	} //End of saveExtractedAnnotations method.
	
	private void processSelectedFiles(File[] selectedFiles) throws Exception
	{
		for (int i = 0; i <selectedFiles.length; i++)
		  {
			 try 
			  {
				System.out.println( "\nSelected File#" + (i+1));
				System.out.println ("File Location> " + selectedFiles[i].getCanonicalPath());
				this.annotations = extractAnnotations(selectedFiles[i]);
			  } 
			  
			 catch (IOException e) 
			  {
				e.printStackTrace();
			  }
		  }
		  
		  if (!annotations.isEmpty())
		  	{
			  EFBOUserInterfaceManager.progressBar.setValue(25);

			  String messageExtractSuccess = "Annotations Extracted Successfully!\n"
										   + "Press OK to Save Your Annotations.";
			  
			  JOptionPane.showMessageDialog(null, messageExtractSuccess, "Success!",
					  						JOptionPane.INFORMATION_MESSAGE);
			  
			  this.saveExtractedAnnotations();
		  	}
		  
		  else
		  	{
			  this.showNoAnnotationFoundMessage();						  		  
		  	}
	}
	
	private void showNoAnnotationFoundMessage() throws Exception
	{
		  String messageNoAnnotationFound = "No Annotation Found within Selected Source(s)."
				  + "\nPlease Try Again.";
		  System.out.println(messageNoAnnotationFound);
		  
		  int OKOption = JOptionPane.showConfirmDialog(null, messageNoAnnotationFound, 
								"Please Try Again", 
								JOptionPane.OK_CANCEL_OPTION);
		  if (OKOption == JOptionPane.OK_OPTION)
			{
			  System.out.println("> Try Again Request Accepted.");
			  new EFBOAnnotationExtractionManager();
			}
		  else
				System.out.println("> Try Again Request Rejected.");
	} //End of method showNoAnnotationFoundMessage().
	
	
	private void showNoFileSelectedMessage() throws Exception
	{
		 String messageFileNotSelected = "No File(s) Were Selected."
					+ "\nPlease Try Again.";
		 System.out.println(messageFileNotSelected);
		 int OKOption = JOptionPane.showConfirmDialog(null, messageFileNotSelected, 
					"Please Try Again", 
					JOptionPane.OK_CANCEL_OPTION);
			if (OKOption == JOptionPane.OK_OPTION)
			{
				System.out.println("> Try Again Request Accepted.");
				//new AnnotationExtractor();
			}
			else
			{
				System.out.println("> Try Again Request Rejected.");
				//System.exit(1);
			}
	} //End of method showNoFileSelectedMessage().

	
	/**
	 * @return the systemName
	 */
	public String getSystemName() 
	{
		return systemName;
	}


	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName)
	{
		this.systemName = systemName;
	}


	/**
	 * @return the extractedAnnotationFilePath
	 */
	public String getExtractedAnnotationFilePath()
	{
		return extractedAnnotationFilePath;
	}


	/**
	 * @param extractedAnnotationFilePath the extractedAnnotationFilePath to set
	 */
	public void setExtractedAnnotationFilePath(String extractedAnnotationFilePath) 
	{
		this.extractedAnnotationFilePath = extractedAnnotationFilePath;
	}

}//End of class.