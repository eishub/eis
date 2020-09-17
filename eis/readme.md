# The Environment Interface

The environment interface framework provides support for connecting agents to an environment. By implementing this interface (see the wiki for a how-to-guide), agents developed using any platform that supports EIS can connect to the environment.

For mroe information, please see the [wiki](https://github.com/eishub/eis/wiki).

## Releases

Releases of the EIS interface can be found [here](https://github.com/eishub/eis/releases). You can also find releases in [eishub's maven repository](https://github.com/eishub/mvn-repo/tree/master/eishub/eis).

## Building

The environment interface project uses [Apache's Maven](http://maven.apache.org).

## Dependency information

```
<repository>
 <id>eishub-mvn-repo</id>
 <url>https://github.com/eishub/mvn-repo/master</url>
</repository>
```

```
<dependency>
 <groupId>eishub</groupId>
 <artifactId>eis</artifactId>
 <version>0.7.0</version>
</dependency>
```