package apleis.repositoryagents;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eis.AgentListener;
import eis.EnvironmentInterfaceStandard;
import eis.EnvironmentListener;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

import apleis.repositoryagents.ParseException;
import apltk.core.StepResult;
import apltk.interpreter.Interpreter;
import apltk.interpreter.InterpreterException;
import apltk.interpreter.QueryCapabilities;
import apltk.interpreter.data.Belief;
import apltk.interpreter.data.Coalition;
import apltk.interpreter.data.Event;
import apltk.interpreter.data.Goal;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;
import apltk.interpreter.data.Percept;
import apltk.interpreter.data.Plan;

/**
 * An agent-specification consists of an agent-name, 
 * a name of an entity, and a class-name.
 * 
 * @author tristanbehrens
 *
 */
class AgentSpec {
	
	public String name;
	public String entity;
	public String team;
	public String className;
	
}

/**
 * This is an agents-interpreter. That is a class that loads, 
 * manages, and executes agents. This interpreter is very rudimentary.
 * There is no scheduling, that is that agents are executec consecutively.
 * This has the risk that an agent blocks the execution too long, leading
 * invalid actions.
 * 
 * @author tristanbehrens
 *
 */
public class AgentsInterpreter implements Interpreter,AgentListener,EnvironmentListener {

	// agent-specifications. loaded from the config-file.
	private Collection<AgentSpec> agentSpecs;
	
	// a collection of agents
	//private Collection<Agent> agents;
	private Map<String,Agent> agents;
	
	private String envFileName;
	private Map<String,Parameter> envParameters;
	
	/**
	 * Instantiates the agents-interpreter. This means, parsing the config file.
	 */
	public AgentsInterpreter() {
		
		try {
			parseConfig("agentsconfig.xml");
		} catch (ParseException e) {
			e.printStackTrace();
			assert false;
		}
		
	}

	/**
	 * Instantiates the agents-interpreter. This means, parsing a given config file.
	 * @param configFile is the given config file
	 */
	public AgentsInterpreter(String configFile) {
		
		try {
			parseConfig(configFile);
		} catch (ParseException e) {
			e.printStackTrace();
			assert false;
		}
		
	}

	/**
	 * Parses a config-file.
	 * 
	 * @param filename the path to the config file
	 * @throws ParseException is thrown when parsing fails
	 */
	private void parseConfig(String filename) throws ParseException {
		
		agentSpecs = new LinkedList<AgentSpec>();
		
		File file = new File(filename);
		
		// parse the XML document
		Document doc = null;
		try {
			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			doc = documentbuilderfactory.newDocumentBuilder().parse(file);
		} catch (SAXException e) {
			throw new ParseException(file.getPath(),"error parsing " + e.getMessage());

		} catch (IOException e) {
			throw new ParseException(file.getPath(),"error parsing " + e.getMessage());

		} catch (ParserConfigurationException e) {
			throw new ParseException(file.getPath(),"error parsing " + e.getMessage());
		}
		
		// get the root
		Element root = doc.getDocumentElement();
		if( root.getNodeName().equalsIgnoreCase("javaAgentsConfig") == false )
			throw new ParseException(file.getPath(),"root-element must be javaAgentsConfig");
		
		// process root's children
		NodeList rootChildren = root.getChildNodes();
		for( int a = 0 ; a < rootChildren.getLength() ; a++ ) {
	
			if ( ( rootChildren.item(a) instanceof Element ) == false ) continue;
			
			Element rootChild = (Element)rootChildren.item(a);

			// ignore text and comment
			if( rootChild.getNodeName().equals("#text") || rootChild.getNodeName().equals("#comment") )
				continue;

			// parse the environment
			if( rootChild.getNodeName().equalsIgnoreCase("environment") ) {

				// get the jar-file
				String jarFileName = rootChild.getAttribute("jar");
				if ( jarFileName.equals("") ) {
					throw new ParseException(file.getPath(),"missing jar-attribute of environment-tag");
				}
				envFileName = jarFileName;

				// get parameters
				envParameters = new HashMap<String,Parameter>();
				NodeList rootChildChildren = rootChild.getChildNodes();
				for( int b = 0 ; b < rootChildChildren.getLength() ; b++ ) {

					Node rootChildChild = rootChildChildren.item(b);
					// parse the parameters list
					if( rootChildChild.getNodeName().equalsIgnoreCase("init") ) {	
						
						Element e = (Element) rootChildChild;
						
						String initKey = e.getAttribute("key");
						String initIdentifier = e.getAttribute("identifier");
						String initNumeral = e.getAttribute("numeral");
						if ( initKey.equals("")) {
							throw new ParseException(file.getPath(),"missing key-attribute of init-tag");
						}
						if ( initIdentifier.equals("") && initNumeral.equals("") ) {
							throw new ParseException(file.getPath(),"init-tag must provide either identifier- or numeral-attribute");
						}
						if ( !initIdentifier.equals("") && !initNumeral.equals("") ) {
							throw new ParseException(file.getPath(),"init-tag cannot provide both identifier- or numeral-attribute");
						}
						if ( !initIdentifier.equals("") ) {
							envParameters.put(initKey, new Identifier(initIdentifier));
							continue;
						}
						if ( !initIdentifier.equals("") ) {
							envParameters.put(initKey, new Numeral(new Integer(initNumeral)));
							continue;
						}
					}
					else {
						//System.out.println("unhandled " + rootChildChild.getNodeName());
					}
				}
			}			

			// parse the agents list
			if( rootChild.getNodeName().equalsIgnoreCase("agents") ) {

				NodeList rootChildChildren = rootChild.getChildNodes();
				for( int b = 0 ; b < rootChildChildren.getLength() ; b++ ) {

					Node rootChildChild = rootChildChildren.item(b);
					
					// ignore text and comment
					if( rootChildChild.getNodeName().equals("#text") || rootChildChild.getNodeName().equals("#comment") )
						continue;

					// parse the entites list
					if( rootChildChild.getNodeName().equalsIgnoreCase("agent") ) {	
						
						Element e = (Element) rootChildChild;
						
						String agentName = e.getAttribute("name");
						if ( agentName == null || agentName.equals(""))
							throw new ParseException(file.getPath(),"missing name-attribute of agent-tag");
						String agentEntity = e.getAttribute("entity");
						if ( agentEntity == null || agentEntity.equals(""))
							throw new ParseException(file.getPath(),"missing entity-attribute of agent-tag");
						String team = e.getAttribute("team");
						if ( team == null || team.equals(""))
							throw new ParseException(file.getPath(),"missing team-attribute of agent-tag");
						String agentClass = e.getAttribute("class");
						if ( agentClass == null || agentClass.equals(""))
							throw new ParseException(file.getPath(),"missing agentClass-attribute of agent-tag");
						
						// add to specs
						AgentSpec as = new AgentSpec();
						as.name = agentName;
						as.entity = agentEntity;
						as.team = team;
						as.className = agentClass;
						agentSpecs.add(as);
						
					}
					else {
						System.out.println("unrecognized xml-tag " + rootChild.getNodeName());
					}


					
				}
			
			}
			else {
				System.out.println("unrecognized xml-tag " + rootChild.getNodeName());
			}
			
		}

	}

	/* (non-Javadoc)
	 * @see apltk.interpreter.Interpreter#addEnvironment(eis.EnvironmentInterfaceStandard)
	 */
	// agents are instantiated here.
	@Override
	public void addEnvironment(EnvironmentInterfaceStandard ei) {
		
		Agent.setEnvironmentInterface(ei);
		
		agents = new HashMap<String,Agent>();
		
		// process agent-specs
		System.out.println("available entities \"" + ei.getEntities());
		for ( AgentSpec as : agentSpecs ) {
			
			Agent agent = Agent.createAgentFromClass(as.name, as.team, as.className);

			try {
				ei.registerAgent(agent.getName());
			} catch (AgentException e1) {
				e1.printStackTrace();
			}
			
			try {
				ei.associateEntity(agent.getName(), as.entity);
			} catch (RelationException e) {
				e.printStackTrace();
			}
			System.out.println("associated agent \"" + agent.getName() + "\" with entity \"" + as.entity + "\"");

			ei.attachAgentListener(agent.getName(), this);

			agents.put(as.name,agent);

		}
	
		ei.attachEnvironmentListener(this);
		
		// free entities
		System.out.println("free entities: " + ei.getFreeEntities());
		
		
	}

	@Override
	public Collection<String> getAgents() {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Collection<Belief> getBeliefBase(String agent) {
		for ( Agent ag : agents.values() ) {
			
			if ( ag.getName().equals(agent) ) 
				return new LinkedList<Belief>(ag.getBeliefBase());
			
		}
		return null;
	}

	@Override
	public Collection<Coalition> getCoalitions() {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Collection<Event> getEventBase(String agent) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Collection<Goal> getGoalBase(String agent) {
		for ( Agent ag : agents.values() ) {
			
			if ( ag.getName().equals(agent) ) {
				return new LinkedList<Goal>(ag.getGoalBase());
			}
			
		}
		return null;
	}

	@Override
	public Collection<Message> getMessageBox(String agent) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public String getName() {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Collection<Percept> getPerceptBase(String agent) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Collection<Plan> getPlanBase(String agent) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public QueryCapabilities getQueryFlags() {
		assert false : "Implement!";
		return null;
	}

	@Override
	public void init(Element parameters) throws InterpreterException {
		assert false : "Implement!";
	}

	@Override
	public void release() {
		assert false : "Implement!";
		
	}

	@Override
	public void setBasePath(String basePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StepResult step() {

		// execute each agent one step
		for ( Agent ag : agents.values() ) {
			
			Action action = ag.step();
			
			// do not do anything when action is null
			if ( action == null ) continue;
			
			try {
				Agent.getEnvironmentInterface().performAction(ag.getName(), action);
			} catch (ActException e) {
				//e.printStackTrace();
				System.out.println("agent \"" + ag.getName() + "\" action \"" + action.toProlog() + "\" failed!");
				System.out.println("message:" + e.getMessage());
				System.out.println("cause:" + e.getCause());
			}

		}
		
		// print mental state
		boolean verbose = true; // TODO move to config
		if ( verbose == true ) {
			for ( Agent ag : agents.values() ) {
				System.out.println("Agent \"" + ag.getName() + "\"");
				System.out.println("  beliefs: " + ag.getBeliefBase());
				System.out.println("  goals: " + ag.getGoalBase());
			}
		}
		
		// wait a little
		boolean delay = true; // TODO move to config
		if ( delay == true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
		// TODO more
		return new StepResult();
	}


	@Override
	public void handleNewEntity(String entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleStateChange(EnvironmentState newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePercept(String agent, eis.iilang.Percept percept) {
		
		agents.get(agent).handlePercept(percept);
		
	}

	@Override
	public void handleFreeEntity(String entity, Collection<String> agents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDeletedEntity(String entity, Collection<String> agents) {
		// TODO Auto-generated method stub
		
	}
	
	public String getEnvFileName() {
		
		return envFileName;
		
	}

	public Map<String, Parameter> getEnvParameters() {
		
		return envParameters;
		
	}

}
