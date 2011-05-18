package apleis.repositoryagents;

import java.io.File;
import java.io.IOException;

import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ManagementException;

/**
 * This app instantiates an interpreter (loading agents),
 * creates the connection to the MASSim-server, and
 * executes the agents.
 *
 */
public class App {
    
	public static void main( String[] args ) {
		
		eis.iilang.IILElement.toProlog = true;
		
		//  instantiate interpreter
		System.out.println("PHASE 1: INSTANTIATING INTERPRETER");
		AgentsInterpreter interpreter = null;
		if ( args.length != 0 ) 
			interpreter = new AgentsInterpreter(args[0]);
		else
			interpreter = new AgentsInterpreter();
		System.out.println("interpreter loaded");

		// load the interface
		System.out.println("");
		System.out.println("PHASE 2: INSTANTIATING ENVIRONMENT");
		EnvironmentInterfaceStandard ei = null;
		try {
			ei = EILoader.fromJarFile(new File(interpreter.getEnvFileName()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("environment-interface loaded");
		if ( interpreter.getEnvParameters().isEmpty() == false ) {
			try {
				ei.init(interpreter.getEnvParameters());
				System.out.println("environment-interface initialized with parameters");
			} catch (ManagementException e) {
				e.printStackTrace();
			}
		}
		
		// start the interface
		try {
			ei.start();
		} catch (ManagementException e) {
			e.printStackTrace();
		}
		System.out.println("environment-interface started");

		System.out.println("");
		System.out.println("PHASE 3: CONNECTING INTERPRETER AND ENVIRONMENT");
		//  connect to environment
		interpreter.addEnvironment(ei);
		System.out.println("interpreter and environment connected");
				
		//  run stepwise
		System.out.println("");
		System.out.println("PHASE 4: RUNNING");
		int step = 1;
		boolean running = true;
		while ( running ) {
			System.out.println("STEP " + step);
			interpreter.step();
			step ++;
			System.out.println("");
			
		}

	}

}
