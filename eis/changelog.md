# Version 0.4
- Added eis2java package that supports translating actions and percepts from and to EIS to Java.
- Added `reset` method to interface.

# Version 0.3
- added static factory methods to EnvironmentCommand and EnvironmentEvent
- EILoader.fromJarFile throws an IOException that might contain a deeper exception
- replaced specific classes for collections with the generic one
- implemented the EMS
- moved examples to different artifacts
- added a class for truth-values to the IIL
- introduced visitors for the IIL (toXML and toProlog are now deprecated)

# Version 0.2-21 January 2010

- made static fields of ActException final
- EIDefaultImpl if an action...-method returns null, null is not added to the collection that is returned
- when an entity is deleted also its type is also removed
- added equals-method to IILang
- you can now instantiate actions and percepts with only a name (without parameters)
- added a connector to the agent contest 2010 scenario
- added a connector to the agent contest 2007 scenario
- fixed the empty-parameter-list bug

# Version 0.2-5 January 2010

- made getAssociatedEntities and getAssociatedAgents in EnvironmentInterfaceStandard public.
- getAllPerceptsFromEntity throws exceptions now.
- added the class eis.EILoader for loading interfaces from jar-files; jar-files have to include a main-class-entry
  in the manifest that denotes the class that is the interface; renders the method eis.EnvironmentInterfaceStandard.fromJarFile
  deprecated; the examples have been moved into the eis.examples package
- extracted the interface EnvironmentInterfaceStandard; the class EIDefaultImpl is the default implementation
- added a simple version-checking mechanism, the EILoader throws an exception of the interface-to be loaded does not have the expection version
- fixed a bug within notifyAgents
- added getType to EnvironmentInterfaceStandard; implemented getType and setType in EIDefaultImpl
- added timestamp and source to DataContainer
- added type to ActionException