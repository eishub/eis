SET-UP

EIS strongly supports Apache's Maven. For documentation go
here:
http://maven.apache.org

Run "mvn package" to compile classes and generate jar-file.

Run "mvn eclipse:eclipse" to generate an eclipse project.
Do not forget to add your local maven-repository to the
class path:
http://maven.apache.org/guides/mini/guide-ide-eclipse.html

Run "mvn site" to generate statistics (includes javadoc).

DIRECTORIES

src/main/java        - sourcecode
target               - jar-files
target/classes       - bytecode
target/site/apidocs  - javadoc 

