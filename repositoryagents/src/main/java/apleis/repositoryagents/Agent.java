package apleis.repositoryagents;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.Goal;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.LogicGoal;
import apltk.interpreter.data.Message;

import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * This class represents a simple agent. Note that is is an abstract class, that is
 * that you are supposed to inherit from this class in order to implement
 * specialized agents.</p>
 * 
 * An abstract agent consists of beliefs and goals and has access to a simple 
 * system for communicating with others.</p>
 * 
 * Note that specialized agents can and will be instantiated by the interpreter
 * using Java-reflection.
 * 
 * @author tristanbehrens
 *
 */
public abstract class Agent {

	// the name of the agent. supposed to be unique (ensured by the constructor).
	private String name;
	
	// the team of the agent. this is required for comminication.
	private String team;
	
	// agent specific stuff
	private Set<LogicBelief> beliefs; // what the agent knows about the world
	protected Set<LogicGoal> goals; // what the agent wants to achieve in the world

	// static stuff, shared by all agents
	private static EnvironmentInterfaceStandard ei; // access to the environment
	private static Collection<String> agents; // all agents in the agent system
	private static Collection<Message> messages; // messages
	private static Map<String,String> agentsTeams;
	static {
    	agents = new LinkedList<String>();
    	messages = new LinkedList<Message>();
    	agentsTeams = new HashMap<String,String>();
	}
	
	/**
	 * Initializes an agent with a given name. Ensures that the name is unique.
	 * @param name is the name of the agent
	 * @param team is the team of the agent
	 */
	public Agent(String name, String team) {
		
		this.name = name;
		this.team = team;
		
		if ( agents.contains(name) ) 
			throw new AssertionError("duplicate agent name \"" + name);
		agents.add(name);
		
		beliefs = new HashSet<LogicBelief>();
		goals = new HashSet<LogicGoal>();
		
		agentsTeams.put(name, team);
		
	}

	/**
	 * Yields the name of the agent.
	 * @return the name of the agent
	 */
	public String getName() {
		return name;
	}

	/**
	 * Yields the team of the agent.
	 * @return the team of the agent
	 */
	public String getTeam() {
		return team;
	}
	
	/**
	 * Yields an instance of a specified agent-class with a given name. This uses Java-reflection and assumes
	 * that the class is already in the classpath.
	 * 
	 * @param agentName is the name of the agent
	 * @param team is the team of the agent
	 * @param agentClass is the fully-qualified name of the agent-class to-be-loaded
	 * @return an agent-instance 
	 */
	static public Agent createAgentFromClass(String agentName, String team, String agentClass) {
	
		// 1. retrieve a class loader
		ClassLoader classLoader = Agent.class.getClassLoader();

		// 2. load the class
		Class<?> aClass = null;
	    try {
	        aClass = classLoader.loadClass(agentClass);
	        System.out.println("instance of \"" + aClass.getName() + "\" created");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

		// 3.  get an instance of the class
		Constructor<?> c = null;
		Agent ret = null;
		try {
			c = aClass.getConstructor(new Class[]{String.class,String.class});
			ret = (Agent)(c.newInstance(agentName,team));
		} catch (Exception e) {
			System.out.println(e);
			//throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"", e);
			assert false:e;
			return null;
		} 

		//ret.name = agentName;
		return ret;
		
	}

	/**
	 * Sets the environment-interface for all agents in this process.
	 * 
	 * @param theEI is the environment-interface
	 */
	public static void setEnvironmentInterface(EnvironmentInterfaceStandard theEI) {
		ei = theEI;
	}
	
	/**
	 * Yields the environment-interface that all agents access.
	 * @return
	 */
	public static EnvironmentInterfaceStandard getEnvironmentInterface() {
		return ei;
	}
	
	/**
	 * Executes one step of the agent. This method is assumed to terminate in appropriate time.
	 */
	public abstract Action step();
	
	/**
	 * Prints an arbitrary object, e.g. a String, to the standard-out. 
	 * Should be used for debugging purposes only.
	 * @param obj
	 */
	protected final void println(Object obj) {
		
		System.out.println("Agent " + name + ": " + obj);
		
	}

	/**
	 * Yields the belief-base of the agent.
	 * 
	 * @return the belief-base
	 */
	public final Collection<LogicBelief> getBeliefBase() {
		
		Collection<LogicBelief> ret = new LinkedList<LogicBelief>();
		ret.addAll(beliefs);
		return ret;

	}	
	
	/**
	 * Yields the goal-base of the agent.
	 * 
	 * @return the goal-base
	 */
	public final Collection<LogicGoal> getGoalBase() {

		Collection<LogicGoal> ret = new LinkedList<LogicGoal>();
		ret.addAll(goals);
		return ret;

	}
	
	/**
	 * Yields all percepts that are currently available.
	 * 
	 * @return a list of percepts or an empty-list if perceiving failed
	 */
	protected Collection<Percept> getAllPercepts() {
		
		try {
			
			Map<String, Collection<Percept>> percepts = ei.getAllPercepts(getName());
			Collection<Percept> ret = new LinkedList<Percept>();
			for ( Collection<Percept> ps : percepts.values() ) {
				ret.addAll(ps);
			}
						
			return ret;
		
		} catch (PerceiveException e) {
			//e.printStackTrace();
			println("error perceiving \"" + e.getMessage() + "\"");	
			return new LinkedList<Percept>();
		} catch (NoEnvironmentException e) {
			//e.printStackTrace();
			println("error perceiving \"" + e.getMessage() + "\"");	
			return new LinkedList<Percept>();
		}
		
	}
	
	/**
	 * Gets all messages that were sent to the agent. After calling this method
	 * the messages will be removed from the message-box.
	 * 
	 * @return all messages
	 */
	protected final Collection<Message> getMessages() {
	
		Collection<Message> ret = new LinkedList<Message>();
		
		for ( Message m : messages ) {
			if ( m.receiver.equals(getName()) ) {
				ret.add(m);
			}
		}

		messages.removeAll(ret);
		
		return ret;
		
	}
	
	/**
	 * Sends a message to a specific agent in the team.
	 * 
	 * @param msg the message to be sent
	 * @param receiver the recipient of the message
	 */
	protected final void sendMessage(Belief belief, String receiver) {
		
		Message msg = new Message();
		
		msg.value = belief;
		msg.sender = getName();
		msg.receiver = receiver;
		
		if ( agentsTeams.get(receiver).equals(team) == false ) {
			System.out.println("cannot send a message to an agent from another team");
		}
		messages.add(msg);
		
	}

	/**
	 * Sends a message to all agents of the team.
	 * 
	 * @param msg the message to be broadcasted
	 */
	protected final void broadcastBelief(LogicBelief belief) {
	
		for ( String ag : agents ) {
			if ( ag.equals(getName())) continue;
			if ( agentsTeams.get(ag).equals(team) == false ) continue;
			sendMessage(belief,ag);
		}
		
	}
	
	/**
	 * This method is called if the environment-interface sends a
	 * percept as a notification. Note, that sending percepts-via-notifications
	 * must be explicitely activated for the environment-interface.
	 * An alternative is to use the <code>getAllPercepts</code> method which
	 * yields all percepts.
	 * @param p the percept to be handled
	 */
	public abstract void handlePercept(Percept p);
	
	@Override
	public boolean equals(Object obj) {
		
		if ( obj == null )
			return false;
		
		if ( obj == this )
			return true;
		
		if ( obj.getClass().equals(this.getClass()) == false )
			return false;
		
		if ( ((Agent)obj).getName().equals(getName()) )
				return true;
		
		return false;
		
	}
	
	@Override
	public int hashCode() {
		
		return name.hashCode();
		
	}

	/**
	 * Yields all beliefs from the belief base that have a specific
	 * predicate.
	 * 
	 * @param predicate the given predicate
	 * @return a list of beliefs that have the given predicate
	 */
	protected LinkedList<LogicBelief> getAllBeliefs(String predicate) {
		
		LinkedList<LogicBelief> ret = new LinkedList<LogicBelief>();
		
		for ( LogicBelief b : beliefs ) {
			if ( b.getPredicate().equals(predicate) )
				ret.add(b);
		}
		
		return ret;
		
	}
	
	/**
	 * Removes all beliefs from the belief-base that have a given predicate.
	 * @param predicate the given predicate
	 */
	protected void removeBeliefs(String predicate) {
		
		LinkedList<LogicBelief> remove = new LinkedList<LogicBelief>();
		
		for ( LogicBelief b : beliefs ) {
			if ( b.getPredicate().equals(predicate) )
				remove.add(b);
		}
	
		beliefs.removeAll(remove);
		
	}
	
	/**
	 * Removes all goals that have a given predicate.
	 * @param predicate the given predicate
	 */
	protected void removeGoals(String predicate) {
		
		LinkedList<LogicGoal> remove = new LinkedList<LogicGoal>();
		
		for ( LogicGoal g : goals ) {
			if ( g.getPredicate().equals(predicate) )
				remove.add(g);
		}
	
		goals.removeAll(remove);
		
	}

	protected void addBelief(LogicBelief belief) {
		beliefs.add(belief);
	}
	
	protected void addGoal(LogicGoal goal) {
		goals.add(goal);
	}
	
	protected boolean containsBelief(LogicBelief belief) {
		return beliefs.contains(belief);
	}

	protected boolean containsGoal(LogicGoal goal) {
		return goals.contains(goals);
	}
	
	protected void clearBeliefs() {
		beliefs.clear();
	}
	
	protected void clearGoals() {
		goals.clear();
	}

}
