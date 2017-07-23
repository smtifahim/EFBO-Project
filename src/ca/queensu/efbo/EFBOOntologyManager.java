/**
 * @author Fahim Imam
 */
package ca.queensu.efbo;


import java.io.File; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.Map; 
import java.util.Set;

import org.mindswap.pellet.jena.PelletReasonerFactory;
//import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI; 
import org.semanticweb.owlapi.model.OWLAnnotation; 
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression; 
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
//import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException; 
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary; 
 
public class EFBOOntologyManager 
{ 
 private String ontologyURI = null; 
 private String ontologyName = null; 
 private File ontologyFile = null; 
 private OWLDataFactory factory = null; 
 private OWLOntology loadedOntology = null;
 private OWLOntology newOntology = null;
 private OWLOntology inferredOntology = null;
 private OWLOntology mergedOntology = null;
 
 private OWLOntologyManager manager = null; 
  
 private Map<String, OWLClass> owlClassIdMap = new HashMap<String, OWLClass>();
 private  Set<OWLNamedIndividual> owlIndividuals = new HashSet<OWLNamedIndividual>(); 
 
 public EFBOOntologyManager()
 {
	 this.manager = OWLManager.createOWLOntologyManager();
	 this.factory = manager.getOWLDataFactory();
 }
 
 public EFBOOntologyManager(OWLOntologyManager manager, OWLDataFactory factory) 
 { 
  this.manager = manager; 
  this.factory = factory; 
 }
 
 public OWLOntologyManager getOWLManager()
 {
	 return manager;
 }
 public EFBOOntologyManager(String ontologyName, File ontologyFile) throws OWLOntologyCreationException 
 { 
  this.ontologyFile = ontologyFile; 
  this.manager = OWLManager.createOWLOntologyManager(); 
  this.factory = manager.getOWLDataFactory(); 
  this.ontologyName = ontologyName; 
  this.loadedOntology = manager.loadOntologyFromOntologyDocument(ontologyFile); 
  this.ontologyURI = loadedOntology.getOntologyID().getOntologyIRI().toString(); 
 } 
 
 public void loadOntology(String ontologyName, File ontologyFile) throws OWLOntologyCreationException 
 { 
  this.ontologyName = ontologyName; 
  this.ontologyFile = ontologyFile; 
  this.loadedOntology = manager.loadOntologyFromOntologyDocument(ontologyFile); 
  this.ontologyURI = loadedOntology.getOntologyID().getOntologyIRI().toString(); 
 } 
 
 public void loadOntology(String ontologyName, 
		 				  String ontologyURI) 
		 			      throws OWLOntologyCreationException 
 { 
  this.ontologyName = ontologyName; 
  this.ontologyURI = ontologyURI;
  IRI documentIRI = IRI.create(ontologyURI);
  this.loadedOntology = manager.loadOntology(documentIRI); 
 } 
 
 public void loadOntology(String ontologyName, 
		  OWLOntology ontology) 
	      throws OWLOntologyCreationException 
	{ 
		String ontologyURI = manager.getOntologyDocumentIRI(ontology).toString();
	    this.ontologyName = ontologyName; 
		this.ontologyURI = ontologyURI;
		IRI documentIRI = IRI.create(ontologyURI);
		this.loadedOntology = manager.loadOntology(documentIRI); 
	} 
 
 public void createNewOntology(String fileLocation) throws Exception
 {
	 IRI ontologyIRI = IRI.create(fileLocation);
	 manager.createOntology(ontologyIRI);
	 this.newOntology = manager.loadOntology(ontologyIRI);
 }
 
 public OWLOntology getNewOntology()
 {
	 return newOntology;
 }
 
 //Import a local ontology into this ontology.
 public void importOWLOntology(OWLOntology owlOntology, String importLocation) throws Exception
 {
	// IRI importIRI = owlOntology.getOWLOntologyManager().getOntologyDocumentIRI(owlOntology);
	 IRI localIRI = IRI.create("file:///" + importLocation.replace("\\", "/"));

	 OWLImportsDeclaration importDeclaration = manager.getOWLDataFactory()
			 								 .getOWLImportsDeclaration(localIRI);

	 AddImport addImport = new AddImport(loadedOntology, importDeclaration);
	 manager.applyChange(addImport);
 }
 
 // to declare owl import statement for the owlOntology
 public void addImportDeclaration(OWLOntology owlOntology, String importOntologyURI)
 {
	 OWLImportsDeclaration importDeclaration = owlOntology.getOWLOntologyManager().getOWLDataFactory()
             								.getOWLImportsDeclaration(IRI.create(importOntologyURI));
	 
	 owlOntology.getOWLOntologyManager().applyChange(new AddImport (owlOntology, importDeclaration));
 }
 
 //Import ontology from an ontology URI.
 public void importOWLOntology(String ontologyURI) throws Exception
 {
	 IRI importIRI = IRI.create(ontologyURI);
	 OWLImportsDeclaration importDeclaration = manager.getOWLDataFactory()
			 								 .getOWLImportsDeclaration(importIRI);

	 AddImport addImport = new AddImport(loadedOntology, importDeclaration);
	 manager.applyChange(addImport);
 }
 
 public void setMergedOntology (OWLOntologyManager owlOntologyManager, String URI) throws Exception
 {
	 OWLOntologyManager ontologyManager = owlOntologyManager;
	 OWLOntologyMerger ontologyMerger = new OWLOntologyMerger(ontologyManager);
	 IRI mergedOntologyIRI = IRI.create(URI);
	 this.mergedOntology = ontologyMerger.createMergedOntology(ontologyManager, mergedOntologyIRI);
	 
 }
 
 public OWLOntology getMergedOntology()
 {
	 return mergedOntology;
 }
 
 
 public void preProcessing() 
 { 
  for (OWLClass cls : loadedOntology.getClassesInSignature()) 
  { 
   owlClassIdMap.put(getLabel(cls).trim().toLowerCase(), cls); 
  } 
 } 
 
// To add an OWL individual from an individual's id, label, and URI strings to the kbase ontology.
// The method also returns the added individual as OWLNamedIndividual object to its caller.  
public OWLNamedIndividual addOWLNamedIndividual(String individualURI, 
								  String individualID,
								  String individualLabel)
 {
	IRI individualIRI = IRI.create(individualURI + "#" + individualID);
	OWLNamedIndividual namedIndividual = factory.getOWLNamedIndividual(individualIRI);
	
	OWLAnnotationProperty rdfsLabelProperty;
	rdfsLabelProperty = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
	
	OWLLiteral owlLiteral = factory.getOWLLiteral(individualLabel);	
	OWLAnnotation label = factory.getOWLAnnotation(rdfsLabelProperty, owlLiteral);
	
	OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(namedIndividual.getIRI(), label);
	AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
	manager.applyChange(addAxiom);
	owlIndividuals.add(namedIndividual);
	
	return namedIndividual;
 }

//To add a named individual to a named class.
public void assertOWLNamedIndividual(OWLNamedIndividual namedIndividual, OWLClass namedClass)
{
	OWLAxiom axiom = factory.getOWLClassAssertionAxiom(namedClass, namedIndividual);
	AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
	manager.applyChange(addAxiom);
}

//Returns an OWL annotation property from an existing ontology.
public OWLAnnotationProperty getOWLAnnotationProperty (String propertyURI, String propertyName)
{
	 IRI propertyIRI = IRI.create(propertyURI + "#" + propertyName);
	 return factory.getOWLAnnotationProperty(propertyIRI);
}

public void addOWLAnnotationPropertyAxiom(OWLNamedIndividual subjectIndividual, 
		    							  OWLAnnotationProperty property, 
		    							  OWLAnnotationValue annotationValue)
{
	 OWLAnnotationSubject subject = subjectIndividual.getIRI();
	 OWLAnnotationAssertionAxiom axiom = null;
	 axiom = factory.getOWLAnnotationAssertionAxiom(property, subject, annotationValue);
	 AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
	 manager.applyChange(addAxiom);
}

 
//Returns an OWL object property from an existing ontology.
public OWLObjectProperty getOWLObjectProperty (String propertyURI, String propertyName)
{
	 IRI propertyIRI = IRI.create(propertyURI + "#" + propertyName);
	 return factory.getOWLObjectProperty(propertyIRI);
}

//Assert an OWL object property axiom between two OWL named individuals.
public void addOWLObjectPropertyAxiom(OWLNamedIndividual subjectIndividual, 
		 							  OWLObjectProperty objectProperty, 
		 						      OWLNamedIndividual objectIndividual)
 {
	 OWLObjectPropertyAssertionAxiom axiom = null;
	 axiom = factory.getOWLObjectPropertyAssertionAxiom
			(objectProperty, subjectIndividual, objectIndividual);
	 AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
	 manager.applyChange(addAxiom);
 }

//Returns an OWL data property from an existing ontology.
public OWLDataProperty getOWLDataProperty (String propertyURI, String propertyName)
{
	 IRI propertyIRI = IRI.create(propertyURI + "#" + propertyName);
	 return factory.getOWLDataProperty(propertyIRI);
}

//Assert an OWL data property axiom between an OWL named individual and a data value.
public void addOWLDataPropertyAxiom(OWLNamedIndividual subjectIndividual, 
		 						    OWLDataProperty dataProperty, 
		 						    OWLLiteral dataValue)
{
	 OWLDataPropertyAssertionAxiom axiom = null;
	 axiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, subjectIndividual, dataValue);
		 
	 AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
	 manager.applyChange(addAxiom);
}

public OWLOntology getLoadedOntology()
 {
	 return loadedOntology;
 }
 
public Set<OWLAnnotationAssertionAxiom> getAllAnnotationAxiom(OWLClass cls) 
 { 
  Set<OWLAnnotationAssertionAxiom> axioms = new HashSet<OWLAnnotationAssertionAxiom>(); 
  for (OWLAnnotationProperty annotation : cls.getAnnotationPropertiesInSignature()) 
  { 
   axioms.add(factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), (OWLAnnotation) annotation)); 
  } 
  return axioms; 
 } 
 
 public Set<OWLClass> getRootClasses() 
 { 
  Set<OWLClass> listOfTopClasses = new HashSet<OWLClass>(); 
  for (OWLClass cls : loadedOntology.getClassesInSignature()) 
  { 
   if (loadedOntology.getSubClassAxiomsForSubClass(cls).size() == 0 
     && loadedOntology.getEquivalentClassesAxioms(cls).size() == 0) listOfTopClasses.add(cls); 
  } 
  return listOfTopClasses; 
 } 
 
 public OWLClass getTopClass() 
 { 
  return factory.getOWLThing(); 
 } 
 
public Set<OWLClass> getChildClasses(OWLClass cls) 
 { 
  Set<OWLClass> listOfClasses = new HashSet<OWLClass>(); 
  for (OWLSubClassOfAxiom axiom : loadedOntology.getSubClassAxiomsForSuperClass(cls)) 
  { 
   OWLClassExpression expression = axiom.getSubClass(); 
   if (!expression.isAnonymous()) 
   { 
    OWLClass asOWLClass = expression.asOWLClass(); 
    listOfClasses.add(asOWLClass); 
   } 
  } 
  return listOfClasses; 
 } 
 
 // TODO: what if the ontology terms have multiple IDs? 
 public String getId(OWLClass entity) 
 { 
  for (OWLAnnotationProperty owlObjectProperty : loadedOntology.getAnnotationPropertiesInSignature()) 
  { 
   if (ifExistsAnnotation(owlObjectProperty.toString(), "id")) 
   { 
    for (String annotation : getAnnotation(entity, owlObjectProperty.getIRI().toString())) 
    { 
     return annotation; 
    } 
   } 
  } 
  return ""; 
 } 
 
 private boolean ifExistsAnnotation(String propertyUrl, String keyword) 
 { 
  String pattern = "[\\W_]*" + keyword + "[\\W_]*"; 
  // Use # as the separator 
  String[] urlFragments = propertyUrl.split("[#/]"); 
  if (urlFragments.length > 1) 
  { 
   String label = urlFragments[urlFragments.length - 1].replaceAll("[\\W]", "_"); 
   for (String token : label.split("_")) 
   { 
    if (token.matches(pattern)) return true; 
   } 
  } 
  return false; 
 } 
 
public String getLabel(OWLEntity entity) 
 { 
  for (String annotation : getAnnotation(entity, OWLRDFVocabulary.RDFS_LABEL.toString())) 
  { 
   return annotation; 
  } 
  return extractOWLClassId(entity); 
 } 
 
 private Set<String> getAnnotation(OWLEntity entity, String property) 
 { 
  Set<String> annotations = new HashSet<String>(); 
  try 
  { 
   OWLAnnotationProperty owlAnnotationProperty = factory.getOWLAnnotationProperty(IRI.create(property)); 
   
   for(OWLAnnotation a : EntitySearcher.getAnnotations(entity, loadedOntology, owlAnnotationProperty)) 
   	{
	    OWLAnnotationValue value = a.getValue();
	    if(value instanceof OWLLiteral) 
	    {
	       annotations.add(((OWLLiteral) value).getLiteral());	    	   
	    }
   	}
  } 
  
  catch (Exception e) 
  { 
   throw new RuntimeException("Failed to get label for OWL Entity " + entity); 
  } 
  return annotations; 
 } 
 
public String extractOWLClassId(OWLEntity cls) 
 { 
  StringBuilder stringBuilder = new StringBuilder(); 
  String clsIri = cls.getIRI().toString(); 
  // Case where id is separated by # 
  String[] split = null; 
  if (clsIri.contains("#")) 
  { 
   split = clsIri.split("#"); 
  } 
  else 
  { 
   split = clsIri.split("/"); 
  } 
  stringBuilder.append(split[split.length - 1]); 
  return stringBuilder.toString(); 
 } 
 
 public String getOntologyIRI() 
 { 
  return ontologyURI; 
 } 
 
 public String getOntologyName() 
 { 
  return ontologyName; 
 } 
 
 public OWLDataFactory getOWLDataFactory()
 {
	 return factory;
 }
 
 public String getOntologyFilePath() 
 { 
  return ontologyFile.getAbsolutePath(); 
 } 
 
 public Map<String, OWLClass> getHashToRetrieveClass() 
 { 
  return owlClassIdMap; 
 } 
 
 public Set<OWLSubClassOfAxiom> getSubClassAxiomsForSuperClass(OWLClass cls) 
 { 
  return loadedOntology.getSubClassAxiomsForSuperClass(cls); 
 } 
 
 public Set<OWLSubClassOfAxiom> getSubClassAxiomsForSubClass(OWLClass cls) 
 { 
  return loadedOntology.getSubClassAxiomsForSubClass(cls); 
 } 
 
 public OWLClass createClass(String iri, Set<OWLClass> rootClasses) 
 { 
  OWLClass owlClass = factory.getOWLClass(IRI.create(iri)); 
  for (OWLClass rootClass : rootClasses) 
  { 
   if (rootClass != owlClass) addClass(rootClass, owlClass); 
  } 
  return owlClass; 
 } 
 
 public void addClass(OWLClass cls, OWLClass parentClass) 
 { 
  if (parentClass == null) parentClass = factory.getOWLThing(); 
  manager.applyChange(new AddAxiom(loadedOntology, factory.getOWLSubClassOfAxiom(cls, parentClass))); 
 } 
 
public void printOntologyMetrics()
 {

	System.out.println(this.getOntologyMetrics());
	
		
 }

public String getOntologyMetrics()
{
	String ontologyMetrics = "";
	ontologyMetrics += "\n--------------------------------------------------------"
					 + "\nOntology Metrics"
					 + "\n--------------------------------------------------------"
					 + "\nOntology Name     : " + this.getOntologyName()
					 + "\nOntology IRI      : " + this.getOntologyIRI()
					 + "\nOntology Format   : " + manager.getOntologyFormat(loadedOntology)
					 + "\nNamed Classes     : " + loadedOntology.getClassesInSignature().size()
					 + "\nObject Properties : " + loadedOntology.getObjectPropertiesInSignature().size()
					 + "\nIndividuals       : " + loadedOntology.getIndividualsInSignature().size()
					 + "\nLogical Axioms    : " + loadedOntology.getLogicalAxioms().size()
					 + "\nAll Axioms        : " + loadedOntology.getAxioms().size()
					 + "\n--------------------------------------------------------";
	
	return ontologyMetrics;
}

public void printAllObjectProperties()
	{
	    Set<OWLObjectProperty> owlObjectProperties = null; 
	    owlObjectProperties = loadedOntology.getObjectPropertiesInSignature();
	    
	    System.out.println("-----------------------------------");   
		System.out.println("List of All OWL Object Properites (" 
						   + owlObjectProperties.size() + ")");
		System.out.println("-----------------------------------"); 
		 
		for (OWLObjectProperty p : owlObjectProperties)  
		  System.out.println(this.getLabel(p)); 

	}
	
public void printAllClasses() 
	{ 
		Set<OWLClass> owlClasses = loadedOntology.getClassesInSignature();        	
	    System.out.println("-----------------------------------");   
		System.out.println("List of All OWL Classes (" 
						   + owlClasses.size() + ")");
		System.out.println("-----------------------------------"); 
		
		for (OWLClass c : owlClasses)
			//System.out.println(this.getLabel(c));
			System.out.println(c.toString());
		  
	}


//// return a set of OWLNamed Individuals of a particular class Type.
//public  NodeSet<OWLNamedIndividual> getOWLNamedIndividuals(OWLOntology ontology, String owlClassLabel)
//{
//   OWLReasonerFactory reasonerFactory = new ReasonerFactory();
//   OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
//   
//   
//   //System.out.println(x);
//   
//    NodeSet<OWLNamedIndividual> individuals = null;
//    
//    for (OWLClass c : ontology.getClassesInSignature())
//    {
//        if (c.getIRI().getShortForm().equals(owlClassLabel))
//        {
//            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
//            System.out.println("Class : "+ c.getIRI().getShortForm());
//            for (OWLNamedIndividual i : instances.getFlattened())
//            {
//                System.out.println(i.getIRI().getShortForm()); 
//            }
//        }
//    }
//    
//    return individuals;
//}

//return a set of OWLNamed Individuals of a particular class Type after inference.
public  Set<OWLNamedIndividual> getInferredOWLNamedIndividuals(OWLClass owlClass)
{
	Set <OWLNamedIndividual> allIndividuals = inferredOntology.getIndividualsInSignature();
	Set <OWLNamedIndividual> individuals =  new HashSet<OWLNamedIndividual>();
		
	for (OWLNamedIndividual i: allIndividuals)
	{
		if (EntitySearcher.getTypes(i, inferredOntology).contains(owlClass))
			{
				individuals.add(i);
				
			}
	}	
  
 return individuals;
}

public  Set<OWLNamedIndividual> getOWLNamedIndividuals(OWLClass owlClass)
{
	Set <OWLNamedIndividual> allIndividuals = loadedOntology.getIndividualsInSignature();
	Set <OWLNamedIndividual> individuals =  new HashSet<OWLNamedIndividual>();
		
	for (OWLNamedIndividual i: allIndividuals)
	{
		if (EntitySearcher.getTypes(i, loadedOntology).contains(owlClass))
			{
				individuals.add(i);				
			}
	}	
  
 return individuals;
}

public Set<OWLNamedIndividual> getIntersectedIndividuals (OWLClass firstOWLClass, OWLClass secondOWLClass)
{
    Set <OWLNamedIndividual> firstIndividualsSet = this.getOWLNamedIndividuals(firstOWLClass);
    Set <OWLNamedIndividual> secondIndividualsSet = this.getOWLNamedIndividuals(secondOWLClass);
   	Set<OWLNamedIndividual> intersectedIndividualsSet  = new HashSet<OWLNamedIndividual>(firstIndividualsSet);
   	intersectedIndividualsSet.retainAll(secondIndividualsSet);
  
  return intersectedIndividualsSet;
}

public Set<OWLNamedIndividual> getDifferentIndividuals (OWLClass firstOWLClass, OWLClass secondOWLClass)
{
    Set <OWLNamedIndividual> firstIndividualsSet = this.getOWLNamedIndividuals(firstOWLClass);
    Set <OWLNamedIndividual> secondIndividualsSet = this.getOWLNamedIndividuals(secondOWLClass);
   	Set<OWLNamedIndividual> differentIndividualsSet  = new HashSet<OWLNamedIndividual>(firstIndividualsSet);
   	differentIndividualsSet.removeAll(secondIndividualsSet);
  
  return differentIndividualsSet;
}

public void setEntityNegation(Set <OWLNamedIndividual> individuals, OWLClass positiveClass)
{
	OWLClassExpression negativeClassExpression = factory.getOWLObjectComplementOf(positiveClass);
	for (OWLNamedIndividual i: individuals)
	{
		OWLAxiom axiom = factory.getOWLClassAssertionAxiom(negativeClassExpression, i);
		AddAxiom addAxiom = new AddAxiom(loadedOntology, axiom);
		manager.applyChange(addAxiom);		
	}	
}

public  OWLClass getOWLClass(String classURI, String className)
{
	IRI classIRI = IRI.create(classURI + "#" + className);		
	OWLClass owlClass = this.loadedOntology.getOWLOntologyManager()
						    .getOWLDataFactory().getOWLClass(classIRI);
	//System.out.println(owlClass);
	return owlClass;
}


public  Set<OWLExpressionAxiom> getOWLNamedIndividuals(OWLObjectProperty owlProperty)
{
	
	Set <OWLExpressionAxiom> axioms = new HashSet <OWLExpressionAxiom>();
	Set <OWLNamedIndividual> allIndividuals = loadedOntology.getIndividualsInSignature();
			
	for (OWLNamedIndividual i: allIndividuals)
	{
		for (OWLNamedIndividual j: allIndividuals)
		if (EntitySearcher.hasObjectPropertyValue(i, owlProperty, j, loadedOntology))
			{
			   OWLExpressionAxiom ax = new OWLExpressionAxiom(i, owlProperty, j);
			   axioms.add(ax);			  				
			}
	}	
  
 return axioms;
}

public  Set<OWLExpressionAxiom> getOWLNamedIndividuals(OWLClass subjectType, OWLObjectProperty owlProperty, OWLClass objectType)
{
	
	Set <OWLExpressionAxiom> axioms = new HashSet <OWLExpressionAxiom>();
	
	Set<OWLNamedIndividual> subjectIndividuals = this.getOWLNamedIndividuals(subjectType);
	Set<OWLNamedIndividual> objectIndividuals = this.getOWLNamedIndividuals(objectType);
	
	for (OWLNamedIndividual i: subjectIndividuals)
	{
		for (OWLNamedIndividual j: objectIndividuals)
		if (EntitySearcher.hasObjectPropertyValue(i, owlProperty, j, loadedOntology))
			{
			   OWLExpressionAxiom ax = new OWLExpressionAxiom(i, owlProperty, j);
			   axioms.add(ax);			  				
			}
	}	
  
 return axioms;
}

public  Set<OWLExpressionAxiom> getOWLNamedIndividuals(OWLClass subjectType, OWLObjectProperty owlProperty)
{
	
	Set <OWLExpressionAxiom> axioms = new HashSet <OWLExpressionAxiom>();
	
	Set<OWLNamedIndividual> subjectIndividuals = this.getOWLNamedIndividuals(subjectType);
	Set <OWLNamedIndividual> allIndividuals = loadedOntology.getIndividualsInSignature();
	
	for (OWLNamedIndividual i: subjectIndividuals)
	{
		for (OWLNamedIndividual j: allIndividuals)
		if (EntitySearcher.hasObjectPropertyValue(i, owlProperty, j, loadedOntology))
			{
			   OWLExpressionAxiom ax = new OWLExpressionAxiom(i, owlProperty, j);
			   axioms.add(ax);			  				
			}
	}	
  
 return axioms;
}


public void printAllIndividuals()
	{
		Set<OWLNamedIndividual> individuals;
		individuals = loadedOntology.getIndividualsInSignature();
		
		System.out.println("-----------------------------------");   
		System.out.println("List of All OWL Individuals ("
						   + individuals.size() + ")");
		System.out.println("-----------------------------------"); 
        
		for (OWLNamedIndividual i : individuals) 
			System.out.println(this.getLabel(i));		   	
	}
 
public Set<OWLAxiom> getOWLIndividualAxioms()
{
	Set<OWLAxiom> axioms = loadedOntology.getAxioms();
	Set<OWLAxiom> owlIndividualAxioms = new HashSet<OWLAxiom>();
	for (OWLAxiom axiom : axioms)
	{
	  if (axiom instanceof OWLIndividualAxiom)
			owlIndividualAxioms.add(axiom);		
	}
		
	return owlIndividualAxioms;
}

 
public void printAllAxioms() 
	{ 
		  Set<OWLAxiom> axioms = loadedOntology.getAxioms();
	      Set<OWLAxiom> individualAxioms = new HashSet<OWLAxiom>(); 
		  Set<OWLAxiom> dataPropertyAxioms = new HashSet<OWLAxiom>(); 
		  Set<OWLAxiom> objectPropertyAxioms = new HashSet<OWLAxiom>(); 
		  Set<OWLAxiom> owlClassAxioms = new HashSet<OWLAxiom>(); 
		  Set<OWLAxiom> otherAxioms = new HashSet<OWLAxiom>(); 
		 
		  for (OWLAxiom axiom : axioms) 
		  { 
		   axiom.getSignature(); 
		   if ((axiom instanceof OWLClassAxiom))
		   		{owlClassAxioms.add(axiom);} 
		   else if (axiom instanceof OWLDataPropertyAxiom)
		   		{dataPropertyAxioms.add(axiom);} 
		   else if (axiom instanceof OWLObjectPropertyAxiom)
		    	{dataPropertyAxioms.add(axiom);} 
		   else if (axiom instanceof OWLIndividualAxiom) 
		   		{individualAxioms.add(axiom);}
		   else otherAxioms.add(axiom); 
		  } 
		 
		  System.out.println("-----------------------------------");   
		  System.out.println("List of All Axioms (" + axioms.size() + ")");
		  System.out.println("-----------------------------------");   
		  
		  for (OWLAxiom owlAxiom : individualAxioms)
			  { 
			   String line; 
			   line = owlAxiom.toString() + " Axiom Type: Individual Axiom"; 
			   System.out.println(line); 
			  } 
		  
		  for (OWLAxiom owlAxiom : dataPropertyAxioms)
			  { 
			   String line; 
			   line = owlAxiom.toString() + " Axiom Type: Data Property"; 
			   System.out.println(line);
			  } 
			  
		  for (OWLAxiom owlAxiom : objectPropertyAxioms) 
			  { 
			   String line; 
			   line = owlAxiom.toString() + " Axiom Type: Object Property"; 
			   System.out.println(line); 
			  } 
		  
		  for (OWLAxiom owlAxiom : owlClassAxioms)
			  { 
			   String line; 
			   line = owlAxiom.toString() + " Axiom Type: Class Axiom"; 
			   System.out.println(line); 
			  }
		  
		  for (OWLAxiom owlAxiom : otherAxioms)
			  { 
			   String line; 
			   line = owlAxiom.toString() + " Axiom Type: Other"; 
			   System.out.println(line); 
			  } 
		  		
	} // End of public void printAxioms(Set<OWLAxiom> axioms)

/**
 * @return the inferredOntology
 */
public OWLOntology getInferredOntology() 
{
	return inferredOntology;
}

/**
 * @param inferredOntology the inferredOntology to set from an ontology file.
 */
	public void setInferredOntology(File ontologyFile) throws Exception
	{
		OWLOntologyManager inputOntologyManager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager inferredOntologyManager =  OWLManager.createOWLOntologyManager();
		
		OWLOntology inputOntology = inputOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
		
		// Instantiate HermiT reasoner for the inputOntology.
		OWLReasonerFactory hermitReasonerFactory = new ReasonerFactory();
		OWLReasoner hermitReasoner = hermitReasonerFactory.createReasoner(inputOntology);
		
		// Classify the input ontology.
		//hermitReasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
		//hermitReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		
		
		// To generate an inferred ontology we use implementations of 
		// inferred axiom generators 
			
		//setup list of inferred axioms.
		//List<InferredAxiomGenerator<? extends OWLAxiom>> inferredAxGenerator = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>(); 
		//inferredAxGenerator.add(new InferredClassAssertionAxiomGenerator()); 
		//inferredAxGenerator.add(new InferredEquivalentClassAxiomGenerator()); 
		
		this.inferredOntology = inferredOntologyManager.createOntology();
			
		InferredOntologyGenerator iOG = new InferredOntologyGenerator(hermitReasoner);
		iOG.fillOntology(inferredOntologyManager.getOWLDataFactory(), this.inferredOntology);	
		hermitReasoner.dispose();
	}

	public class OWLExpressionAxiom
	{
		private OWLNamedIndividual subject = null;
		private OWLObjectProperty objectProperty = null;		
		private OWLNamedIndividual object = null;
		
		OWLExpressionAxiom (OWLNamedIndividual subject, OWLObjectProperty objectProperty, OWLNamedIndividual object )
		{
			this.subject = subject;
			this.objectProperty = objectProperty;
			this.object = object;
		}
		
		public OWLNamedIndividual getSubject()
		{
			return subject;
		}
		
		public OWLObjectProperty getObjectProperty()
		{
			return objectProperty;
		}
		
		public OWLNamedIndividual getObject()
		{
			return object;
		}
		
	}
}// End of class EFBOOntologyManager