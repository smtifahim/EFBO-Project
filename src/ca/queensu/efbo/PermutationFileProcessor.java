package ca.queensu.efbo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;

public class PermutationFileProcessor 
{
    static String output="";
	public static void main(String[] args) 
	{
		  final String defaultFilePath = System.getProperty("user.dir") 
				   + "/Resources/Annotated-Sources/Permutation-Test";
		  JFileChooser fileChooser = new JFileChooser(new File(defaultFilePath));
		  fileChooser.setDialogTitle("Select a File");
		  fileChooser.setMultiSelectionEnabled(false);
		  fileChooser.showOpenDialog(null);
		  File fileName = fileChooser.getSelectedFile();
		 // String line;
		  
		  // This will reference one line at a time
	        String line = null;
	        
	        //String seq = "Seq";

	        try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = 
	                new FileReader(fileName);

	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);

	            int seq = 1;
	            while((line = bufferedReader.readLine()) != null) 
	            {
	                System.out.println(line);
	                line = line.replaceAll("\\{", "");
	                line = line.replaceAll("\\}", "");
	                StringTokenizer st = new StringTokenizer(line, ",");
	                String efbo = "\\\\@EFBO: ";
	        		int i = 0;
	        		while (st.hasMoreElements()) 
	        		{
	        			String event="";
	        			
	        			String currentString = (String) st.nextElement();
	        			
	        			if (i==0)
	        				event += "\n" + efbo + "seq-" + seq + " hasInitialEvent " + currentString + "-" + seq;
	        			
	        			 event += "\n"+ efbo + currentString + "-" + seq + " hasTimePoint " + i;
	        			
	        		    
	        			 if (i==5)
		        			event += "\n" + efbo + "seq-" + seq + " hasFinalEvent " + currentString + "-" + seq;
	        			
	        			 
	        		    System.out.println( event);
	        		    output += event;
	        			i++;
	        		} 
	        		seq++;
	            }

	            writeFile(output);
	            // Always close files.
	            bufferedReader.close();         
	        
	            }
	        catch(FileNotFoundException ex)
	        {
	            System.out.println
	            (
	                "Unable to open file '" + 
	                fileName + "'"
	            );                
	        }
	        
	        catch(IOException ex)
	        {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }
		  
		  

	}
	
	static void writeFile(String outputText)
	{
		 final String defaultFilePath = System.getProperty("user.dir") 
				   + "/Resources/Annotated-Sources/Permutation-Test/permutation-out.txt";
		   // The name of the file to open.
        String fileName = defaultFilePath;

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

           bufferedWriter.write(outputText);

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) 
        {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}

}
