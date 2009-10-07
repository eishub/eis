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
 * Loads an environment interface from a file and instantiates it.
 * 
 * @author tristanbehrens
 *
 */
public class EILoader {
	
	public static String requiredVersion = "0.2";
	
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
			throw new IOException("Error, could not add URL to system classloader");
		}

		// 4. load the class
		URLClassLoader loader = new URLClassLoader(new URL[]{url});
		Class<?> envInterfaceClass = null;
		try {
			envInterfaceClass = loader.loadClass(mainClass);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"");
		}
		
		// 5.  get an instance of the class
		Constructor<?> c = null;
		EnvironmentInterfaceStandard ei = null;
		try {
			c = envInterfaceClass.getConstructor();
			ei = (EnvironmentInterfaceStandard)(c.newInstance());
		} catch (Exception e) {
			System.out.println(e);
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"");
		} 

		// check version
		if( ei.version.equals(requiredVersion) == false )
			throw new IOException("Loaded environment interface version does not match the required one \"" + requiredVersion + "\"");
		
		return ei;

	}
	

	public static void main(String[] args) throws IOException {
		
		if( args.length == 0) {
			
			System.out.println("You have to provide a filename.");
			
		}
		else {
		
			EnvironmentInterfaceStandard ei = EILoader.fromJarFile( new File(args[0]) ); 
		
		}
		
	}
	
}
