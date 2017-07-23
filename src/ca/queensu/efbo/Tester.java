package ca.queensu.efbo;
import java.util.ArrayList;
import java.util.Set;


public class Tester 
{

	public static void main(String[] args) throws Exception
	{
		//new Console();
		EFBOAnnotationExtractionManager annot = new EFBOAnnotationExtractionManager();
		Set<EFBOAnnotation> annotations = annot.getExtractedAnnotations();
		
		if (annotations.size()!=0)
		{
			EFBOKnowledgeBaseManager efboKBGenerator1 = new EFBOKnowledgeBaseManager("SYSTEM-01", "LoginSystem");
			efboKBGenerator1.processExtractedAnnotations(annotations);
			efboKBGenerator1.getEFBOManager().printOntologyMetrics();
			efboKBGenerator1.getEFBOManager().printAllClasses();
			//efboKBGenerator1.getEFBOManager().printAllIndividuals();
			//efboKBGenerator1.getEFBOManager().printAllObjectProperties();
		}
		
		System.exit(1);
	
	}
}
