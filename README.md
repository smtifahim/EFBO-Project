# The EFBO-Based Functional Consistency Validation System
This repository contains all the files relevant to the EFBO-Based Functional Consistency System. 
To facilitate the validation process, we have developed an end-to-end software system called the EFBO-Based Consistency Validation System. The system is implemented in Java and uses OWL-API 4.3 for its various ontology-related operations and reasoning.

## The EFBO-Based Validation Process: A General Overview
This is a novel approach to comparing functionality between two systems that utilize the benefits of an ontology along with its powerful logical reasoning capabilities. This kind of consistency validation can be useful for cross-platform applications that are developed
and evolved independently with the same set of functional goals.

The first step of the process involves deriving the EFBO-based functionality models for each of the systems. For the systems that are already developed, the EFBO can be used to annotate the source code elements that correspond to the functional entities of the ontology. Once annotated, the source code entities can then be instantiated for their corresponding EFBO entities. Once the instantiation process is complete for both systems, the ontology will then represent a comparable set of functional behavior models for each of the source systems. 

![efbo-v-process](https://github.com/smtifahim/EFBO-Project/assets/13155192/10f4fc95-a49a-49c4-93fb-983283a75893)
**Figure:** The EFBO-Based Functional Consistency Validation Process.

Next, the functionality models for each of the sources have to be integrated into a single ontology. As discussed in the next section, we use a utility ontology called the EFBO-V that we have developed to integrate the functionality models into a comparable representation. The merged functionality models of the ontology can then be passed into an automated reasoner. The reasoner can then derive the classification of functional entities based on their sources under different functional reasoning categories.

At this point, the reasoner can also generate a mappable list of events for each of the source systems. The actual mappings of events are assumed to be a manual process that involves checking the events that are classified under the `System-I Event` and the `System-II Event` of the `EFBO-V` ontology. It should be noted that only the event entities of the sources need to be mapped. The mapping of other entities such as the interfaces and agents can be inferred automatically as mappable entities for each of the mapping events between the source systems.

## The EFBO-Based Validation System Architecture
The system is divided into five constituent modules each of which is assigned to managing different tasks within the validation process. These include the `EFBO Validation Manager`, `Annotation Extraction Manager`, `Knowledgebase Manager`, `Mapping Events Manager`, and `Consistency Status Report Manager`. Each of these modules corresponds to a Java class with different attributes and properties. The following diagram presents the cross-functional swimming lane diagram of the EFBO validation system architecture. The first lane in the figure illustrates the tasks of the EFBO User Interface Manager. The manager provides a set of user interaction points for the six-step consistency validation process and delegates the corresponding tasks to the rest of the EFBO managers.

![Alt text](https://github.com/smtifahim/EFBO-Project/blob/master/EFBO-Swimlane.png?raw=true "EFBO Architecure")
**Figure:** The EFBO-Based Consistency Validation Architecture.
