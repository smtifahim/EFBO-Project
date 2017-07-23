package ca.queensu.efbo;


/**
 * Annotation class definition.
 * @author Fahim Imam
 */

public class EFBOAnnotation 
{
	private String annotatedText;
	private String fileLocation;
	private int lineNumber; // The line where the annotation is declared.
	private String subject; // Corresponds to RDF subject.
	private String predicate; // Corresponds to RDF predicate.
	private String object; // Corresponds to RDF object.

	/**
	 * Default Constructor 
	 */
	public EFBOAnnotation()
	{
		annotatedText = "";
		fileLocation = "";
		subject = "";
		predicate = "";
		object = "";
		lineNumber = 0;
	}
	
	/**
	 * @param fileLocation
	 * @param lineNumber
	 * @param annotatedText
	 */
	public EFBOAnnotation(String fileLocation, int lineNumber, String annotatedText) 
	{
		this.setFileLocation(fileLocation);
		this.setLineNumber(lineNumber);
		this.setAnnotatedText(annotatedText);
	}

	/**
	 * @return
	 */
	public String getAnnotatedText() 
	{
		return annotatedText;
	}

	/**
	 * @param annotatedText
	 */
	public void setAnnotatedText(String annotatedText) 
	{
		this.annotatedText = annotatedText;
	}

	public int getLineNumber() 
	{
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) 
	{
		this.lineNumber = lineNumber;
	}

	public String getFileLocation()
	{
		return fileLocation;
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}
	
	public String toString()
	{
		String fileLocation = "\nLocation> " + this.getFileLocation();
		String annotation = "\n@line#" + this.getLineNumber()
							+ "> " + this.getAnnotatedText(); 
		return fileLocation + annotation;
		
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	public void setPredicate(String predicate)
	{
		this.predicate = predicate;
	}
	
	public void setObject(String object)
	{
		this.object = object;
	}
	
	public String getSubject() 
	{
		return subject;
	}
	
	public String getPredicate()
	{
		return predicate;
	}
	
	public String getObject()
	{
		return object;
	}
	
}
