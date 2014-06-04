package apleis.repositoryagents.agents2011;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import apleis.repositoryagents.Agent;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.LogicGoal;
import apltk.interpreter.data.Message;
import eis.iilang.Action;
import eis.iilang.Percept;

public class SimpleInspectorAgent extends Agent {

	public SimpleInspectorAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action step() {

		handleMessages();
		handlePercepts();

		Action act = null;

		// 1. recharging
		act = planRecharge();
		if ( act != null ) return act;
		
		// 2 buying battery
		act = planBuyBattery();
		if ( act != null ) return act;

		// 3. inspecting if necessary
		act = planInspect();
		if ( act != null ) return act;
		
		// 4. (almost) random walking
		act = planRandomWalk();
		if ( act != null ) return act;

		return Mars2011Util.skipAction();
		
	}


	private void handleMessages() {
		
		// handle messages... believe everything the others say
		Collection<Message> messages = getMessages();
		for ( Message msg : messages ) {
			println(msg.sender + " told me " + msg.value);
			println("I do not care. I have more important things to do");
		}
		
	}

	private void handlePercepts() {

		String position = null;
		//Vector<String> neighbors = new Vector<String>();
		
		// check percepts
		Collection<Percept> percepts = getAllPercepts();
		//if ( gatherSpecimens ) processSpecimens(percepts);
		removeBeliefs("visibleEntity");
		removeBeliefs("visibleEdge");
		for ( Percept p : percepts ) {
			if ( p.getName().equals("step") ) {
				println(p);
			}
			else if ( p.getName().equals("visibleEntity") ) {
				LogicBelief b = Mars2011Util.perceptToBelief(p);
				if ( containsBelief(b) == false ) {
					//println("I perceive an edge I have not known before");
					addBelief(b);
					//broadcastBelief(b);
				}
				else {
					//println("I already knew " + b);
				}
			}
			else if ( p.getName().equals("visibleEdge") ) {
				LogicBelief b = Mars2011Util.perceptToBelief(p);
				if ( containsBelief(b) == false ) {
					//println("I perceive an edge I have not known before");
					addBelief(b);
					//broadcastBelief(b);
				}
				else {
					//println("I already knew " + b);
				}
			}
			else if ( p.getName().equals("inspectedEntity") ) {
				println("I have perceived an inspected entity " + p);
			}
			else if ( p.getName().equals("health")) {
				Integer health = new Integer(p.getParameters().get(0).toString());
				println("my health is " +health );
				if ( health.intValue() == 0 ) {
					println("my health is zero. asking for help");
					broadcastBelief(new LogicBelief("iAmDisabled"));
				}
			}
			else if ( p.getName().equals("position") ) {
				position = p.getParameters().get(0).toString();
				removeBeliefs("position");
				addBelief(new LogicBelief("position",position));
			}
			else if ( p.getName().equals("energy") ) {
				Integer energy = new Integer(p.getParameters().get(0).toString());
				removeBeliefs("energy");
				addBelief(new LogicBelief("energy",energy.toString()));
			}
			else if ( p.getName().equals("maxEnergy") ) {
				Integer maxEnergy = new Integer(p.getParameters().get(0).toString());
				removeBeliefs("maxEnergy");
				addBelief(new LogicBelief("maxEnergy",maxEnergy.toString()));
			}
			else if ( p.getName().equals("money") ) {
				Integer money = new Integer(p.getParameters().get(0).toString());
				removeBeliefs("money");
				addBelief(new LogicBelief("money",money.toString()));
			}
		}
		
		// again for checking neighbors
		this.removeBeliefs("neighbor");
		for ( Percept p : percepts ) {
			if ( p.getName().equals("visibleEdge") ) {
				String vertex1 = p.getParameters().get(0).toString();
				String vertex2 = p.getParameters().get(1).toString();
				if ( vertex1.equals(position) ) 
					addBelief(new LogicBelief("neighbor",vertex2));
				if ( vertex2.equals(position) ) 
					addBelief(new LogicBelief("neighbor",vertex1));
			}
		}	
	}

	private Action planRecharge() {

		LinkedList<LogicBelief> beliefs = null;
		
		beliefs =  getAllBeliefs("energy");
		if ( beliefs.size() == 0 ) {
				println("strangely I do not know my energy");
				return Mars2011Util.skipAction();
		}		
		int energy = new Integer(beliefs.getFirst().getParameters().firstElement()).intValue();

		beliefs =  getAllBeliefs("maxEnergy");
		if ( beliefs.size() == 0 ) {
				println("strangely I do not know my maxEnergy");
				return Mars2011Util.skipAction();
		}		
		int maxEnergy = new Integer(beliefs.getFirst().getParameters().firstElement()).intValue();

		// if has the goal of being recharged...
		if ( goals.contains(new LogicGoal("beAtFullCharge")) ) {
			if ( maxEnergy == energy ) {
				println("I can stop recharging. I am at full charge");
				removeGoals("beAtFullCharge");
			}
			else {
				println("recharging...");
				return Mars2011Util.rechargeAction();
			}
		}
		// go to recharge mode if necessary
		else {
			if ( energy < maxEnergy / 3 ) {
				println("I need to recharge");
				goals.add(new LogicGoal("beAtFullCharge"));
				return Mars2011Util.rechargeAction();
			}
		}	
		
		return null;
		
	}

	
	/**
	 * Buy a battery with a given probability
	 * @return
	 */
	private Action planBuyBattery() {
		
		LinkedList<LogicBelief> beliefs = this.getAllBeliefs("money");
		if ( beliefs.size() == 0 ) {
			println("strangely I do not know our money.");
			return null;
		}
		
		LogicBelief moneyBelief = beliefs.get(0);
		int money = new Integer(moneyBelief.getParameters().get(0)).intValue();
		
		if ( money < 10 ) {
			println("we do not have enough money.");
			return null;
		}
		println("we do have enough money.");
		
		//double r = Math.random();
		//if ( r > 0.1 ) {
		//	println("I am not going to buy a battery");
		//	return null;
		//}
		println("I am going to buy a battery");
		
		return Mars2011Util.buyAction("battery");
		
	}
	
	private Action planInspect() {

		LinkedList<LogicBelief> beliefs = null;

		// determine adjacent vertices including the current position
		Vector<String> vertices = new Vector<String>();
		beliefs =  getAllBeliefs("position");
		String position = beliefs.getFirst().getParameters().firstElement();
		vertices.add(position);
		beliefs = getAllBeliefs("neighbor");
		for ( LogicBelief b : beliefs ) {
			vertices.add(b.getParameters().firstElement());
		}
		
		int adjacentNum = 0;
		
		String myTeam = getTeam();
		
		LinkedList<LogicBelief> visible = getAllBeliefs("visibleEntity");
		for ( LogicBelief v : visible ) {
		
			String pos = v.getParameters().get(1);
			String team = v.getParameters().get(2);
			
			// ignore same team
			if ( myTeam.equals(team) ) continue;
			
			// not adjacent
			if ( vertices.contains(pos) == false ) continue;
			adjacentNum ++;
						
		}

		if ( adjacentNum == 0 ) {
			println("there are no opponents to inspect");
			return null;
		}
		
		println("there are " + adjacentNum + " visible opponents that I could inspect");
		
		if ( Math.random() < 0.5 ) {
			println("I will inspect");
			return Mars2011Util.inspectAction();
		}

		println("I won't inspect");
		return null;
		
	}

	private Action planRandomWalk() {

		LinkedList<LogicBelief> beliefs = getAllBeliefs("neighbor");
		Vector<String> neighbors = new Vector<String>();
		for ( LogicBelief b : beliefs ) {
			neighbors.add(b.getParameters().firstElement());
		}
		
		if ( neighbors.size() == 0 ) {
			println("strangely I do not know any neighbors");
			return Mars2011Util.skipAction();
		}
		
		// goto neighbors
		Collections.shuffle(neighbors);
		String neighbor = neighbors.firstElement();
		println("I will go to " + neighbor);
		return Mars2011Util.gotoAction(neighbor);
		
	}

}
