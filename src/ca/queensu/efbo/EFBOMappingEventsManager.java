package ca.queensu.efbo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Container;
import javax.swing.border.*;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import java.awt.List;
import java.awt.Button;

public class EFBOMappingEventsManager extends JFrame
{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 5340300100609276700L;

	private JPanel contentPane;

	DefaultListModel<String> listModel = new DefaultListModel<>();
	private JList<String> mappingList = new JList<String>(listModel);
	
	DefaultListModel<String> firstSystemModel = new DefaultListModel<>();
	JList<String> firstSystemEventList = new JList<String>(firstSystemModel);
	
	DefaultListModel<String> secondSystemModel = new DefaultListModel<>();
	JList<String> secondSystemEventList = new JList<String>(secondSystemModel);	
	
	
	private Set<OWLNamedIndividual> firstSystemEvents;
	private Set<OWLNamedIndividual> secondSystemEvents;
	
	private EFBOOntologyManager efboOntologyManager;
	private ArrayList <EFBOMappingEvents> mappingEvents = new ArrayList <EFBOMappingEvents>();

	ArrayList <OWLNamedIndividual> systemIEvents = new ArrayList <OWLNamedIndividual>();
	ArrayList <OWLNamedIndividual> systemIIEvents= new ArrayList <OWLNamedIndividual>();
	int lm =0;
	int me =0;
	
	public EFBOMappingEventsManager() 
	{
		firstSystemEvents = null;
		secondSystemEvents = null;
		efboOntologyManager = null;
		//mappingEvents = new ArrayList <EFBOMappingEvents>();
		
	}
	
	public EFBOMappingEventsManager(Set<OWLNamedIndividual> firstSystemEvents,  
									Set<OWLNamedIndividual> secondSystemEvents, 
									EFBOOntologyManager efboOntologyManager ) 
	{
		this.setFirstSystemEvents(firstSystemEvents);
		this.setSecondSystemEvents(secondSystemEvents);
		this.setEFBOOntologyManager(efboOntologyManager);
		
		firstSystemEventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		secondSystemEventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		populateListElements();
		this.setGUIElements();
		this.setVisible(true);
	}
	
	public void populateListElements()
	{
		int i = 0;
		for (OWLNamedIndividual e : firstSystemEvents)
		{ 
			String eventLabel = this.efboOntologyManager.getLabel(e);
			firstSystemModel.add(i, eventLabel);
			systemIEvents.add(i, e);
			i++;			
		}
			
		
		int j = 0;		
		for (OWLNamedIndividual e : secondSystemEvents)
		{ 
			String eventLabel = this.efboOntologyManager.getLabel(e);
			secondSystemModel.add(j, eventLabel);
			systemIIEvents.add(j, e);
			j++;	
		}
			
		this.setGUIElements();
	}
	
	

	private void setGUIElements()
	{
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 450, 521);
	contentPane = new JPanel();
	contentPane.setBorder(null);
	setContentPane(contentPane);
	setTitle("EFBO Mapping Events Interface");
	
	contentPane.setLayout(new MigLayout("", "[219px,grow,center][219px,grow]", "[254px,grow][grow][]"));

	
	JPanel firstSystemEventsPanel = new JPanel();
	firstSystemEventsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), 
						"System-I Events", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
	contentPane.add(firstSystemEventsPanel, "cell 0 0,grow");
	firstSystemEventsPanel.setLayout(new MigLayout("", "[219px,grow,center]", "[254px,grow]"));
	
	JScrollPane scrollPane_1 = new JScrollPane();
	firstSystemEventsPanel.add(scrollPane_1, "cell 0 0,grow");
	
	
	scrollPane_1.setViewportView(firstSystemEventList);
	
	JPanel secondSystemEventsPanel = new JPanel();
	secondSystemEventsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), 
					  "System-II Events", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
	contentPane.add(secondSystemEventsPanel, "cell 1 0,grow");
	secondSystemEventsPanel.setLayout(new MigLayout("", "[219px,grow]", "[254px,grow]"));
	
	JScrollPane scrollPane_2 = new JScrollPane();
	secondSystemEventsPanel.add(scrollPane_2, "cell 0 0,grow");
	
	scrollPane_2.setViewportView(secondSystemEventList);
	
	JScrollPane scrollPane = new JScrollPane();
	contentPane.add(scrollPane, "cell 0 1 2 1,grow");
	
	
	mappingList.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), 
			       "List of Mapping Evenets between System-I and System-II", 
			       TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
	
	scrollPane.setViewportView(mappingList);
	
	JButton btnMappingButton = new JButton("Map Selected Events");
	
	btnMappingButton.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e)
		{
					
			String system1Value = firstSystemEventList.getSelectedValue().toString();
			String system2Value = secondSystemEventList.getSelectedValue().toString();
			
			if (system1Value.contains("Event Mapped") || system2Value.contains("Event Mapped"))
			{
				JOptionPane.showMessageDialog(null, "Event Already Mapped", "Invalid Selection",
						                     JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{	
				int i =  firstSystemEventList.getSelectedIndex();
				int j =  secondSystemEventList.getSelectedIndex();
				
				mappingEvents.add(new EFBOMappingEvents(systemIEvents.get(i), systemIIEvents.get(j), efboOntologyManager));
						
				listModel.add(lm, mappingEvents.get(me).getEFBOMappingEvents());
				
				//firstSystemModel.setElementAt("mapped", i);
				//secondSystemModel.setElementAt("mapped", j);
				
				firstSystemModel.set(i, "Event Mapped");
				secondSystemModel.set(j, "Event Mapped");
				//secondSystemModel.removeElement(secondSystemEventList.getSelectedValue());
				
				lm++;
				me++;
			}
						
		}
	});
	scrollPane.setColumnHeaderView(btnMappingButton);
	
	
	JButton btnAddMappings = new JButton("Add the Mappings Above to the EFBO-V Ontology");
	btnAddMappings.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			for (EFBOMappingEvents events: mappingEvents)
			{
				events.setMappingEvents();
				
			}
			
			listModel.removeAllElements();
			JOptionPane.showMessageDialog(null, "Mapping Events Added to the EFBO-V", "Mappins Asserted",
                    JOptionPane.INFORMATION_MESSAGE);
															
		}
	});
	contentPane.add(btnAddMappings, "cell 0 2 2 1,growx");
}

	/**
	 * @return the firstSystemEvents
	 */
	public Set<OWLNamedIndividual> getFirstSystemEvents() 
	{
		return firstSystemEvents;
	}

	/**
	 * @param firstSystemEvents2 the firstSystemEvents to set
	 */
	public void setFirstSystemEvents(Set<OWLNamedIndividual> firstSystemEvents)
	{
		this.firstSystemEvents = firstSystemEvents;
	}

	
	
	/**
	 * @return the secondSystemEvents
	 */
	public Set<OWLNamedIndividual> getSecondSystemEvents() 
	{
		return secondSystemEvents;
	}

	/**
	 * @param secondSystemEvents2 the secondSystemEvents to set
	 */
	public void setSecondSystemEvents(Set<OWLNamedIndividual> secondSystemEvents2)
	{
		this.secondSystemEvents = secondSystemEvents2;
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

	/**
	 * @return the mappingEvents
	 */
	public ArrayList <EFBOMappingEvents> getMappingEvents() 
	{
		return mappingEvents;
	}

	
}


