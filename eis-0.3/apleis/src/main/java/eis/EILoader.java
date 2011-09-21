package eis;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import eis.EnvironmentInterfaceStandard;

/**
 * Loads an environment-interface from a file and instantiates it.
 * Uses java-reflection to load the environment-interface from the respective
 * jar-file. Also checks the required version for compatibility.
 * 
 * @author tristanbehrens
 *
 */
public class EILoader {
	
	private static String version = "0.3";
	
	/**
	 * Loads an environment-interface from a jar-file.
	 * Firstly, the jar-file is added to the classpath.
	 * Secondly, the main-class is determined by inspecting the manifest.
	 * And finally, an instance of that very main-class is created and returned.
	 * 
	 * @param file the file to be loaded
	 * @return an instance of the environment-interface contained in the jar-file
	 * @throws IOException is thrown if loading was not successfull
	 */
	public static EnvironmentInterfaceStandard fromJarFile(File file) throws IOException {
		
		// 1. locate file, check for existence, check for being a jar
		if( file.exists() == false )
			throw new IOException("\"" + file.getAbsolutePath() + "\" does not exist.");
			
		if( file.getName().endsWith(".jar") == false )
			throw new IOException("\"" + file.getAbsolutePath() + "\" is not a jar-file.");

		// 2. read manifest and get main class
		JarFile jarFile = new JarFile(file);
		Manifest manifest = jarFile.getManifest();

		String mainClass = manifest.getMainAttributes().getValue("Main-Class");
		if ( mainClass == null || mainClass.equals("") )
			throw new IOException(file + "does not specify a main-class");

		// 3. add the jar file to the classpath
		URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		URL url = file.toURI().toURL();
		
		try {
			Method method = sysclass.getDeclaredMethod("addURL",new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(sysloader,new Object[]{ url });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader",t);
		}

		// 4. load the class
		URLClassLoader loader = new URLClassLoader(new URL[]{url});
		Class<?> envInterfaceClass = null;
		try {
			envInterfaceClass = loader.loadClass(mainClass);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"",e);
		}
		
		// 5.  get an instance of the class
		Constructor<?> c = null;
		EnvironmentInterfaceStandard ei = null;
		try {
			c = envInterfaceClass.getConstructor();
			ei = (EnvironmentInterfaceStandard)(c.newInstance());
		} catch (Exception e) {
			System.out.println(e);
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"", e);
		} 

		// check version
		if( version.equals(ei.requiredVersion()) == false )
			throw new IOException("Loaded environment interface version does not match the required one \"" + version + "\" vs. \"" + ei.requiredVersion() + "\"");
		
		return ei;

	}
	
	
	/**
	 * Instantiates an environment-interface from a given class-name.
	 * Assumes that all required classes are already in the classpath.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static EnvironmentInterfaceStandard fromClassName(String className) throws IOException {
		
		// 4. load the class
		ClassLoader loader = EnvironmentInterfaceStandard.class.getClassLoader();
		Class<?> envInterfaceClass = null;
		try {
			envInterfaceClass = loader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class \"" + className + "\" could not be loaded",e);
		}
		
		// 5.  get an instance of the class
		Constructor<?> c = null;
		EnvironmentInterfaceStandard ei = null;
		try {
			c = envInterfaceClass.getConstructor();
			ei = (EnvironmentInterfaceStandard)(c.newInstance());
		} catch (Exception e) {
			System.out.println(e);
			throw new IOException("Class \"" + className + "\" could not be loaded", e);
		} 

		// check version
		if( version.equals(ei.requiredVersion()) == false )
			throw new IOException("Loaded environment interface version does not match the required one \"" + version + "\" vs. \"" + ei.requiredVersion() + "\"");
		
		return ei;

	}


	/**
	 * Loads an environment-interface.
	 * 
	 * @param args the first string has to be a path to a jar-file containing an environment-interface
	 * @throws IOException thrown if the file could not be loaded
	 */
	public static void main(String[] args) throws IOException {
		
		if( args.length == 0) {
			
			System.out.println("You have to provide a filename.");
			
		}
		else {
		
			EnvironmentInterfaceStandard ei = EILoader.fromJarFile( new File(args[0]) ); 
		
		}
		
	}
	
}
