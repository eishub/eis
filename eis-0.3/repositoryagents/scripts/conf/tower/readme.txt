***** Tower Environment *****

This README file describes the EISified tower environment that can be used to connect GOAL agents.

** Original environment **
The original simulator has been developed by Chris T.K. Ng and Nils J. Nilsson, from the Robotics 
Laboratory, Dept. of Computer Science, Stanford University. 

http://www.robotics.stanford.edu/users/nilsson/trweb/TRTower/

The main changes made to the environment
concern the extension to connect to GOAL via the EIS interface standard. The basic functionality of
the original environment has not been changed. However, the goal state window has been removed as
a GOAL agent is supposed to specify the goal to be achieved now.

** User interface **
The environment starts up empty but ready to run. The simulator can be paused by pressing the pause button "||",
and in that state a single action can be performed by using the step button ">|". Playing with these buttons a
little will quickly clarify what the basic actions pickup and putdown available in the environment do (notably,
these actions have duration). Before anything can be moved blocks need to be added by using the "New Block" 
button. Only a limited number of blocks can be added to ensure that the table is always `clear`, i.e. it is
always possible to put a block on the table (a constraint in the original Blocks World that somewhat simplifies
the problem). All blocks are deleted from the environment again by pressing the "Reset" button. The slider can
be used to control the simulation speed of the gripper: Slide to the left to decrease speed and to the right to
increase speed. Depending on your CPU speed, max speed may be reached already before the slider is at the right
end.

What makes this environment interesting is that the user can interfer and change the configuration. The
environment can be modified at any time by dragging the blocks with the mouse. Note that when doing this the
gripper is freezed, i.e. does not do anything anymore until mouse control is released again. Note that you can
even drop blocks midways inside a tower, pull blocks out of the gripper or insert them in the gripper.

** Actions available to a GOAL agent **
A GOAL agent can be used to control the gripper in the environment. There are three actions provided by
the environment to control the gripper:
- pickup(<BLOCK>): picks up a block, where <BLOCK> is the label associated with the block in the environment
  (make sure to use lower case labels if you use Prolog). Precondition of this action is that the block to be
  picked up is clear, i.e. there is no other block on top of it.
- putdown(<BLOCK>, <TARGET>): puts down a block labeled <BLOCK> on another block labeled <TARGET> or the table,
  if <TARGET> is `table`. Precondition is that <TARGET> is clear, which is always true if <TARGET> is the table
  due to the limited number of blocks that may be added to the environment.
- nil: The `no operation action`, i.e. the action that stops the gripper and moves it back to the top left
  corner.
