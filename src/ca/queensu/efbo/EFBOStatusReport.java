package ca.queensu.efbo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.sparql.*;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import ca.queensu.efbo.EFBOOntologyManager.OWLExpressionAxiom;

public class EFBOStatusReport 
{
	Set <OWLNamedIndividual> firstSystemEvent;
	Set <OWLNamedIndividual> secondSystemEvent;
	private OWLOntology efboMergedInferredOntology = null;
	EFBOOntologyManager efboStatusReportManager = null;
//	OWLOntologyDataSet dataset;
		
	
	private static final String EFBO_FRC_URI = "http://www.cs.queensu.ca/~imam/ontologies/efbo-frc.owl";
	private static final String EFBO_V_URI = "http://www.cs.queensu.ca/~imam/ontologies/efbo-v.owl";
	private static final String EFBO_CORE_URI = "http://www.cs.queensu.ca/~imam/ontologies/efbo.owl";
	
	
	public static void main(String[] args) throws Exception
	{
		String location =  System.getProperty("user.dir") + "/Resources/Ontologies/BlackJackProject/BlackJackMerged.owl";
		File f = new File(location);
		EFBOStatusReport esr = new EFBOStatusReport(f);
		esr.printMappingEvents();
		esr.printConsistentEvents();
		esr.printActionByAgents();
		esr.printDecisionPointEvents();
	}
	public EFBOStatusReport() throws Exception
	{
		EFBOStatusReport.main(null);
	}

	public EFBOStatusReport(File ontologyFile) throws Exception
	{
		this.efboStatusReportManager = new EFBOOntologyManager();
		this.efboStatusReportManager.loadOntology("EFBO-V Inferred", ontologyFile);
		this.efboMergedInferredOntology = efboStatusReportManager.getLoadedOntology();
		
		//this.setEntityBySystem();
	}
	
	public void printSystemsInfo()
	{
	
	}
	
		
//	private void setEntityBySystem()
//	{
//		firstSystemEntity = new HashSet<OWLNamedIndividual>();
//		secondSystemEntity = new HashSet<OWLNamedIndividual>();
//		
//		OWLClass firstSystemEvent = this.getOWLClass(EFBO_V_URI, "System-1_Event");
//		firstSystemEntity = efboStatusReportManager.getOWLNamedIndividuals(firstSystemEvent);
//		
//		OWLClass secondSystemEvent = this.getOWLClass(EFBO_V_URI, "System-2_Event");
//		secondSystemEntity = efboStatusReportManager.getOWLNamedIndividuals(secondSystemEvent);		
//	}
//	
	
	public void printMappingEvents() throws Exception
	{
		OWLObjectProperty hasMappingEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_V_URI, "hasMappingEvent");
		OWLClass firstSystemEvent = this.getOWLClass(EFBO_V_URI, "System-1_Event");
		OWLClass secondSystemEvent = this.getOWLClass(EFBO_V_URI, "System-2_Event");
		
		System.out.println("Asserted Mapping Events Between Systems.");
		this.printEntityBySystem(firstSystemEvent, hasMappingEvent, secondSystemEvent);
	}
	
	public void printConsistentEvents() throws Exception
	{
		OWLObjectProperty hasConsistentEventFlow = efboStatusReportManager.getOWLObjectProperty(EFBO_V_URI, "hasConsistentEventFlow");
		OWLObjectProperty hasConsistentNextEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_V_URI, "hasConsistentNextEvent");
		OWLObjectProperty hasConsistentPrevEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_V_URI, "hasConsistentPreviousEvent");
		OWLObjectProperty hasConsistentAltEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_V_URI, "hasConsistentAltEvent");
		
		OWLClass firstSystemEvent = this.getOWLClass(EFBO_V_URI, "System-1_Event");
		OWLClass secondSystemEvent = this.getOWLClass(EFBO_V_URI, "System-2_Event");
		
		System.out.println("\nEvents With Consistent Event Flow.");
		this.printEntityBySystem(firstSystemEvent, hasConsistentEventFlow, secondSystemEvent);
	//	printInconsistentEvents();
		
		System.out.println("\nEvents With Consistent Next Events.");
		this.printEntityBySystem(firstSystemEvent, hasConsistentNextEvent, secondSystemEvent);
		
		System.out.println("\nEvents With Consistent Previous Events.");
		this.printEntityBySystem(firstSystemEvent, hasConsistentPrevEvent, secondSystemEvent);
		
		System.out.println("\nEvents With Consistent Alternative Events.");
		this.printEntityBySystem(firstSystemEvent, hasConsistentAltEvent, secondSystemEvent);
		
	}
	
	public void printInconsistentEvents() throws Exception
	{
		OWLClass firstSystemEvent = efboStatusReportManager.getOWLClass(EFBO_V_URI, "System-1_Event");
		OWLClass eventWithConsistentFlow = this.getOWLClass(EFBO_V_URI, "EventWithConsistentFlow");
		
		Set <OWLNamedIndividual> inconEvents = efboStatusReportManager.getDifferentIndividuals(firstSystemEvent, eventWithConsistentFlow);
		
		for (OWLNamedIndividual e: inconEvents)
		{
 		    System.out.println(efboStatusReportManager.getLabel(e));
 		   
		}
		//efboStatusReportManager.setEntityNegation(eventWithConsistentFlow, firstSystemEvent);		
	}
	
	
/*
 * Select all the dps and then display them for each of the systems.
 * */
	public void printDecisionPointEvents() throws Exception
	{
		OWLClass firstSystemDPE = this.getOWLClass(EFBO_FRC_URI, "DecisionPointEvent");
		OWLClass nextOfDPE = this.getOWLClass(EFBO_V_URI, "System-1_Event");
		
		OWLClass secondSystemDPE = this.getOWLClass(EFBO_FRC_URI, "DecisionPointEvent");
		OWLClass nextOfDPE2 = this.getOWLClass(EFBO_V_URI, "System-2_Event");
		
		OWLObjectProperty hasNextEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, "hasNextEvent");
		OWLObjectProperty hasPrevEvent = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, "hasPreviousEvent");
		OWLObjectProperty isAltEventOf = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, "isAlternateEventOf");
		
		Set<OWLNamedIndividual> inds = efboStatusReportManager.getOWLNamedIndividuals(firstSystemDPE);
		
		String dpeName = "";
		for (OWLNamedIndividual i: inds)
		{
			dpeName += efboStatusReportManager.getLabel(i) + "; "; 
		}
		
		String g = "@startuml";
	    g += "\ntitle\n" + efboStatusReportManager.getLabel(firstSystemDPE)
          + "\n" + dpeName
          + "\nend title\n";
		g += getRelatedGraph("DecisionPointEvent", "System-1_Event", "hasNextEvent", "hasPreviousEvent", "isAlternateEventOf");
		g += getRelatedGraph("DecisionPointEvent", "System-2_Event", "hasNextEvent", "hasPreviousEvent", "isAlternateEventOf");
		g += "\n@enduml";
		System.out.println(g);
		
		System.out.println("\nDecesion Point Events");
		this.printEntityBySystem(firstSystemDPE, hasNextEvent, nextOfDPE);
				
		System.out.println("\nDecesion Point Events");
		this.printEntityBySystem(secondSystemDPE, hasNextEvent, nextOfDPE2);
		
	}
	
	public String getGraph(Set<OWLExpressionAxiom> ax)
	{
		String g = "";
		for (OWLExpressionAxiom a : ax)
		{
			String subject = efboStatusReportManager.getLabel(a.getSubject());
			String object = efboStatusReportManager.getLabel(a.getObject());
			String property = efboStatusReportManager.getLabel(a.getObjectProperty());
            						
			g += "\n" + "\"" + subject + "\"";
			
			if (property.equals("hasPreviousEvent"))
			    g+= "-up->[ " + property +" ]"; 
			else if (property.equals("isAlternateEventOf"))
				g+= "-right->[ " + property +" ]";
			else
				g+= "-->[ " + property +" ]";
			
            g += "\"" + object+ "\"";
		}
		return g;
	}
	
	public String getRelatedGraph (String targetClassName, String filterClassName, String... propertyList)
	{
		OWLClass targetClass = this.getOWLClass(EFBO_FRC_URI, targetClassName);
		OWLClass filterClass = this.getOWLClass(EFBO_V_URI, filterClassName);
	    String g = "";
	    
				
		for (String property: propertyList)
		{
			OWLObjectProperty owlProperty = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, property);			
				
			Set <OWLExpressionAxiom> a = efboStatusReportManager.getOWLNamedIndividuals(targetClass, owlProperty, filterClass);
		    g += getGraph(a);
		    if(property.equals("isAlternateEventOf"))
		    {
		    	Set <OWLExpressionAxiom> b = efboStatusReportManager.getOWLNamedIndividuals(filterClass, owlProperty, filterClass);
		        g += getGraph(b);
		    }    
		}
		return g;
	}
		
	public void printActionByAgents() throws Exception
	{
		OWLObjectProperty triggers = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, "triggers");
		OWLObjectProperty performs = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, "performs");
		
		OWLClass action = this.getOWLClass(EFBO_CORE_URI, "Action");
		OWLClass event = this.getOWLClass(EFBO_CORE_URI, "Event");
		OWLClass clientAgent = this.getOWLClass(EFBO_CORE_URI, "ClientAgent");
		OWLClass userAgent = this.getOWLClass(EFBO_CORE_URI, "UserAgent");
		OWLClass serverAgent = this.getOWLClass(EFBO_CORE_URI, "ServerAgent");
		
		System.out.println("\nEvents by Triggering Agents.");
		this.printEntityBySystem(userAgent, triggers, event);
		this.printEntityBySystem(clientAgent, triggers, event);
		this.printEntityBySystem(serverAgent, triggers, event);
		
		System.out.println("\nActions Performed by Agents.");
		this.printEntityBySystem(userAgent, performs, action);
		this.printEntityBySystem(clientAgent, performs, action);
		this.printEntityBySystem(serverAgent, performs, action);		
		
	}
	
	public void printActionByAgentProperty(String agentType, String propertyName) throws Exception
	{
		OWLObjectProperty agentProperty = efboStatusReportManager.getOWLObjectProperty(EFBO_CORE_URI, propertyName);
		OWLClass agent = this.getOWLClass(EFBO_CORE_URI, agentType);
		OWLClass action = this.getOWLClass(EFBO_CORE_URI, "Action");
		
		this.printEntityBySystem(agent, agentProperty, action);
		
	}
	
	
	public void printEntityBySystem(OWLClass domainEntity, 
									OWLObjectProperty objectProperty, 
									OWLClass rangeEntity) throws Exception
	{
		TableBuilder tb = new TableBuilder();
		Set <OWLExpressionAxiom> a = efboStatusReportManager.getOWLNamedIndividuals(domainEntity, 
																   objectProperty, rangeEntity);	
	    String firstColHeader =  efboStatusReportManager.getLabel(domainEntity);
	    String secondColHeader = efboStatusReportManager.getLabel(rangeEntity);		
		
		tb.addRow("-----------------------------------------", "+", "---------------------------", "+", "-----------------------------------------");
		tb.addRow(firstColHeader, "|", "Object Property" , "|", secondColHeader);
		tb.addRow("-----------------------------------------", "+", "---------------------------", "+", "-----------------------------------------");
		int rowCtr = 0;
		for (OWLExpressionAxiom i: a)// firstSystemEntity)
		{
			//String subjectLabel = i.getSubject().getIRI().getShortForm();
			String subjectLabel = efboStatusReportManager.getLabel(i.getSubject());
			String objectLabel = efboStatusReportManager.getLabel(i.getObject());
			String propertyLabel = efboStatusReportManager.getLabel(i.getObjectProperty());
			
			tb.addRow(subjectLabel, "|", propertyLabel , "|", objectLabel);
			rowCtr++;
		}
		tb.addRow("-----------------------------------------", "+", "---------------------------", "+", "-----------------------------------------");
		tb.addRow("Total Row Count: " + rowCtr);
		System.out.println(tb.toString());
		//System.out.println("Total Row Count: " + rowCtr);
		
		//efboStatusReportManager.printOntologyMetrics();		
	}
	
	
	
	
	public void setGraphRep(String systemID, OWLClass domainEntity, 
							OWLObjectProperty objectProperty, 
							OWLClass rangeEntity) throws Exception
	{
		Set <OWLExpressionAxiom> a = efboStatusReportManager.getOWLNamedIndividuals(domainEntity, 
				   					 objectProperty, rangeEntity);	
		String g = "";
		
		String subject = efboStatusReportManager.getLabel(domainEntity);
		String object = efboStatusReportManager.getLabel(rangeEntity);
		
		g += "object " + "\"" + subject + "\"" + " as " + systemID + "\n";
		
		int rowCtr = 0;
		int inc = 1;
		
		String prevSubject = "";
		
		for (OWLExpressionAxiom i: a)// firstSystemEntity)
		{
			//String subjectLabel = i.getSubject().getIRI().getShortForm();
			String subjectLabel = efboStatusReportManager.getLabel(i.getSubject());
			
			if (!subjectLabel.equals(prevSubject))
				g+= systemID + " : " + "\"" + subjectLabel + "\"" + "\n";
			
			String objectLabel = efboStatusReportManager.getLabel(i.getObject());
			g+= "object " + "\"" + object + "\"" + " as " + systemID + inc + "\n";
			g+= systemID + inc + " : " + "\"" + objectLabel + "\"" + "\n";
			
			String propertyLabel = efboStatusReportManager.getLabel(i.getObjectProperty());
			
			g += systemID + " --> " + systemID + inc +  " : " + propertyLabel + "\n";
			
			//g += subjectLabel +" -- " + propertyLabel + " --> " + objectLabel + "\n";
			//g += subjectLabel + " as " + i.getSubject().getIRI().getShortForm() + "\n";
			//g += objectLabel + " as " + i.getObject().getIRI().getShortForm() + "\n";
			//g += i.getSubject().getIRI().getShortForm() + "-->" + i.getObject().getIRI().getShortForm() + ": "+ propertyLabel + "\n";
			
			inc++;
			rowCtr++;
			prevSubject = subjectLabel;
		}
		
		System.out.println(g);
		
	}
	
	private OWLClass getOWLClass(String classURI, String className)
	{
		IRI classIRI = IRI.create(classURI + "#" + className);		
		OWLClass owlClass = efboMergedInferredOntology.getOWLOntologyManager()
													  .getOWLDataFactory().getOWLClass(classIRI);
		//System.out.println(owlClass);
		return owlClass;
	}
	
	

/*
 * Print formatting is based on the following Class. -Fahim
 * Source: https://www.ksmpartners.com/2013/08/nicely-formatted-tabular-output-in-java/
 * */
public class TableBuilder
{
    List<String[]> rows = new LinkedList<String[]>();
 
    // String... for defining the method with a variable number of arguments.
    public void addRow(String... cols)
    {
        rows.add(cols);
    }
 
    private int[] colWidths()
    {
        int cols = -1;
 
        for(String[] row : rows)
            cols = Math.max(cols, row.length);
 
        int[] widths = new int[cols];
 
        for(String[] row : rows) 
        {
            for(int colNum = 0; colNum < row.length; colNum++) 
            {
                widths[colNum] = Math.max(
                						    widths[colNum], 
                				 			StringUtils.length(row[colNum])
                				 		 );
            }
        }
 
        return widths;
    }
 
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
 
        int[] colWidths = colWidths();
 
        for(String[] row : rows) 
        {
            for(int colNum = 0; colNum < row.length; colNum++) 
            {
                buf.append(
                    StringUtils.rightPad(
                    						StringUtils.defaultString
                    						(row[colNum]), colWidths[colNum])
                						);
                buf.append(' ');
            }
 
            buf.append('\n');
        }
 
        return buf.toString();
    }
 
}



}