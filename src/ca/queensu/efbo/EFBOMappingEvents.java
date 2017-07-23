package ca.queensu.efbo;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class EFBOMappingEvents 
{
  private OWLNamedIndividual firstSystemEvent;
  private OWLNamedIndividual secondSystemEvent;
  private EFBOOntologyManager efboOntologyManager;
  
  public EFBOMappingEvents()
  {
	  firstSystemEvent = null;
	  secondSystemEvent = null;
	  efboOntologyManager = null;
  }
  
  public EFBOMappingEvents(OWLNamedIndividual firstSystemEvent,
		  				   OWLNamedIndividual secondSystemEvent,
		  				   EFBOOntologyManager efboOntologyManager )
  {
	  this.setFirstSystemEvent(firstSystemEvent);
	  this.setSecondSystemEvent(secondSystemEvent);
	  this.setEFBOOntologyManager(efboOntologyManager);
	  //this.setMappingEvents();
  }
  
  public void setMappingEvents()
  {
		OWLObjectProperty objectProperty = null;
        objectProperty = efboOntologyManager.getOWLObjectProperty(EFBOValidationManager.EFBO_V_URI, "hasMappingEvent");
        efboOntologyManager.addOWLObjectPropertyAxiom(firstSystemEvent, objectProperty, secondSystemEvent);
	  
  }

/**
 * @return the firstSystemEvents
 */
public OWLNamedIndividual getFirstSystemEvent() 
{
	return firstSystemEvent;
}

/**
 * @param firstSystemEvents the firstSystemEvents to set
 */
public void setFirstSystemEvent(OWLNamedIndividual firstSystemEvents)
{
	this.firstSystemEvent = firstSystemEvents;
}

/**
 * @return the secondSystemEvents
 */
public OWLNamedIndividual getSecondSystemEvent()
{
	return secondSystemEvent;
}

/**
 * @param secondSystemEvents the secondSystemEvents to set
 */
public void setSecondSystemEvent(OWLNamedIndividual secondSystemEvents)
{
	this.secondSystemEvent = secondSystemEvents;
}

/**
 * @return the efboOntologyManager
 */
public EFBOOntologyManager getEFBOOntologyManager() 
{
	return efboOntologyManager;
}

/**
 * @param efboOntologyManager the efboOntologyManager to set
 */
public void setEFBOOntologyManager(EFBOOntologyManager efboOntologyManager)
{
	this.efboOntologyManager = efboOntologyManager;
}

public String getEFBOMappingEvents()
	{
      String s = efboOntologyManager.getLabel(this.firstSystemEvent)
    		    + "==MappedTo==" + efboOntologyManager.getLabel(this.secondSystemEvent);
      return s;
	}
}