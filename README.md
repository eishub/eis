# The Environment Interface Standard (EIS)

The environment interface standard aims at providing a standard framework for connecting agents to environments such as games, simulators, robots, etc. EIS provides "glue code" that facilitates developing a connection between an agent platform and an environment.

The basic concept of an agent used in EIS is that of an agent that performs actions in the environment and receives percepts from its environments. This is a [standard and generic definition of an agent](http://en.wikipedia.org/wiki/Intelligent_agent) as used in Artificial Intelligence.

Any agent platform that supports the EIS interface can connect to any environment that implements the interface. The EIS framework thus reduces the effort that is needed to connect agents to environments in two ways:

* it requires code on the agent platform side to connect to an environment to be developed only once; once an agent platform supports the EIS interface it can connect to any environment that implements the interface;
* it requires code on the environment side that allows an agent platform to connect to it to be developed only once; once the interface has been implemented for an environment any agent platform that supports EIS can connect to it.

# The Environment Interface
The apleis folder contains the source code of the EIS glue code framework. It defines an interface that needs to be implemented for an environment to allow agents to connect to that environment.

# Releases
See [here](https://github.com/eishub/eis/releases) for available releases of the EIS interface. Releases can also be found in eishub's maven repository [here](https://github.com/eishub/mvn-repo/tree/master/apleis).

# Code Fragments
The folders eis-remote and examples contain possibly useful code fragments for connecting to an environment remotely (using RMI) and for other tasks, such as enabling your agent platform to support EIS.

# Environments that Implement EIS

See [eishub](https://github.com/eishub/) for environments that implement the environment interface, ranging from toy environments such as the blocks world, to commercial games such as Unreal Tournament.