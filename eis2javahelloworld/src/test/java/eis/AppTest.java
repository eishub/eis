package eis;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import static org.junit.Assert.*;
/**
 * Unit test for simple App.
 */
public class AppTest {
    
	@Test
	public void test() {
		
		EnvironmentInterfaceStandard ei = null;
		
		// load the interface
		try {
			ei = EILoader.fromClassName("eis.eis2javahelloworld.HelloWorldEnvironment");
		} catch (IOException e) {
			e.printStackTrace();
			fail("failed to load environment");
		}
		
		// initialize
		try {
			ei.init(new HashMap<String,Parameter>());
		} catch (ManagementException e1) {
			e1.printStackTrace();
			fail("failed to initialize environment");
		}
		
		try {
			ei.registerAgent("agent");
		} catch (AgentException e1) {
			e1.printStackTrace();
			fail("failed to register agent");
		}
		
		// associate
		try {
			ei.associateEntity("agent", "entity1");
		} catch (RelationException e) {
			e.printStackTrace();
			fail("failed to associate entity with agent");
		}
		
		// start
		try {
			ei.start();
		} catch (ManagementException e1) {
			e1.printStackTrace();
			fail("failed to start");
		}
		
		// act
		try {
			ei.performAction("agent", new Action("printText", new Identifier("hello world")));
		} catch (ActException e1) {
			e1.printStackTrace();
			e1.getCause().printStackTrace();
			fail("failed to act" + e1);
		}
		
		// perceive
		try {
			Map<String, Collection<Percept>> ps = ei.getAllPercepts("agent");
			//System.out.println(ps);
		} catch (PerceiveException e) {
			e.printStackTrace();
			fail("failed to perceive");
		} catch (NoEnvironmentException e) {
			e.printStackTrace();
			fail("failed to perceive");
		}
		
    }

}
