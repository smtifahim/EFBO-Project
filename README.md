# The EFBO-Based Consistency Validation System

This repository contains the files relevant to the EFBO-Based Functional Consistency Validation System as part of a Software Engineering thesis. This thesis introduces a novel approach to comparing functionality between two systems that utilizes the benefits of an ontology along with its powerful logical reasoning capabilities. This kind of consistency validation can be useful for cross-platform applications that are developed and evolved independently with the same set of functional goals. As a proof-of-concept of the validation process introduced in the thesis, we have developed this end-to-end software system called the `EFBO Consistency Validation System`. The system is implemented in Java and uses OWL-API 4.3 for its various ontology-related operations and reasoning. The three ontologies used for the system, the `EFBO`, `EFBO-FRC`, and `EFBO-V`, are documented [here](https://github.com/smtifahim/EFBO-Ontology-Repository/tree/master). Refer to the thesis document to learn more about the project.
* F. T. Imam (2017). [An ontology-oriented approach to represent and compare the functional behavior of event-based systems](https://www.proquest.com/openview/907c24804d15aecb749840396e429bf8/). Doctoral dissertation, Queen's University (Canada).

**Abstract:** Representing the functional behavior of a system is a critical practice in any engineering discipline. However, how to compare and reason about the represented systems has mostly been dependent on our cognitive processing ability. An effective
representation must support machine-assisted reasoning mechanisms as well as human intuitions. We present an effective, ontology-oriented formal approach to representation that is designed to be both machine-processable and human-comprehensible. Based on the theory of events and change in AI-based commonsense reasoning and the notion of affordances, we developed a novel approach to functional reasoning which is more intuitive and practical compared to the existing formal systems of representation. After discussing the notion of ontologies along with their implementation formalisms, we present our representational facility called the Event-Based Functional Behaviour Ontology (EFBO). A detailed overview of the EFBO is presented along with our approach to behavior modeling. As a special application, we present
and demonstrate the EFBO-based validation system that can be used to validate the levels of functional consistencies between cross-platform systems. We also discuss the commonsense reasoning theories relevant to our representation along with other related work. Finally, we discuss the broader perspectives of our contributions within the context of modern computing and software engineering.

## The EFBO-Based Validation Process: A General Overview
The first step of the process involves deriving the EFBO-based functionality models for each of the systems. For the systems that are already developed, the EFBO can be used to annotate the source code elements that correspond to the functional entities of the ontology. Once annotated, the source code entities can then be instantiated for their corresponding EFBO entities. Once the instantiation process is complete for both systems, the ontology will then represent a comparable set of functional behavior models for each of the source systems. 

![efbo-v-process](https://github.com/smtifahim/EFBO-Project/assets/13155192/10f4fc95-a49a-49c4-93fb-983283a75893)
**Figure:** The EFBO-Based Functional Consistency Validation Process.

Next, the functionality models for each of the sources have to be integrated into a single ontology. As discussed in the next section, we use a utility ontology called the EFBO-V that we have developed to integrate the functionality models into a comparable representation. The merged functionality models of the ontology can then be passed into an automated reasoner. The reasoner can then derive the classification of functional entities based on their sources under different functional reasoning categories.

At this point, the reasoner can also generate a mappable list of events for each of the source systems. The actual mappings of events are assumed to be a manual process that involves checking the events that are classified under the `System-I Event` and the `System-II Event` of the `EFBO-V` ontology. It should be noted that only the event entities of the sources need to be mapped. The mapping of other entities such as the interfaces and agents can be inferred automatically as mappable entities for each of the mapping events between the source systems.

## The EFBO-Based Validation System Architecture
The system is divided into five constituent modules each of which is assigned to managing different tasks within the validation process. These include the `EFBO Validation Manager`, `Annotation Extraction Manager`, `Knowledgebase Manager`, `Mapping Events Manager`, and `Consistency Status Report Manager`. Each of these modules corresponds to a Java class with different attributes and properties. The following diagram presents the cross-functional swimming lane diagram of the EFBO validation system architecture. The first lane in the figure illustrates the tasks of the EFBO User Interface Manager. The manager provides a set of user interaction points for the six-step consistency validation process and delegates the corresponding tasks to the rest of the EFBO managers.

![Alt text](https://github.com/smtifahim/EFBO-Project/blob/master/EFBO-Swimlane.png?raw=true "EFBO Architecure")
**Figure:** The EFBO-Based Consistency Validation Architecture.
