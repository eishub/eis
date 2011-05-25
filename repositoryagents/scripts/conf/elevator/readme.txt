Elevator readme file, 16feb2009 W.Pasman

This directory contains the elevator environment for GOAL

The elevator simulator is Copyright 2004-2005 Chris Dailey & Neil McKellar
The elevator project page is on http://sourceforge.net/projects/elevatorsim.
We must be using version 0.4 since that is the first version with the
time factor, animated people getting into cars etc.

We needed to hack the source code in order to create and plug in our
GOAL controller that controls the elevator behaviour based on GOAL programs.

You can find extensive documentation on the environment in the pdf file.
Briefly, to run the environment, you need to apply the following steps after the blank environment window opened:

1. select File/New...
2. Pick "Random Rider Insertion" (the default choice) 
3. press the Real-Time button
4. Select 3 elevators, 10 floors (the rest can be changed but the default should be good), and select the GOALController.
5. press Apply
6. press Go, Dude!

After this the environment is ready to run and you can run the GOAL agents.

You can press Pause at any moment during the run to see the statistics. Press Go,Dude! again to continue.

As usually you kill the environment from GOAL by selecting the .mas and selecting Run/Kill.


stand-alone run possible, with
 java -classpath elevatorenv.jar\:eis.jar ElevatorEnv.EnvironmentInterface

 