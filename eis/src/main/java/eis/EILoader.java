package eis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Loads an environment-interface from a file and instantiates it. Uses
 * java-reflection to load the environment-interface from the respective
 * jar-file. Also checks the required version for compatibility.
 */
public class EILoader {
	public static final String version = "0.7.0";

	/**
	 * Loads an environment-interface from a jar-file. Firstly, the jar-file is
	 * added to the classpath. Secondly, the main-class is determined by inspecting
	 * the manifest. And finally, an instance of that very main-class is created and
	 * returned.
	 *
	 * @param file the file to be loaded
	 * @return an instance of the environment-interface contained in the jar-file
	 * @throws IOException is thrown if loading was not successful
	 */
	public static EnvironmentInterfaceStandard fromJarFile(final File file) throws IOException {
		// 1. locate file, check for existence, check for being a jar
		if (!file.exists()) {
			throw new IOException("\"" + file.getAbsolutePath() + "\" does not exist.");
		}
		if (!file.getName().endsWith(".jar")) {
			throw new IOException("\"" + file.getAbsolutePath() + "\" is not a jar-file.");
		}

		// 2. read manifest and get main class
		String mainClass = null;
		try (final JarFile jarFile = new JarFile(file)) {
			mainClass = jarFile.getManifest().getMainAttributes().getValue("Main-Class");
		}
		if (mainClass == null || mainClass.isEmpty()) {
			throw new IOException(file + "does not specify a main-class");
		}

		// 3. make a classloader for the jar file
		final URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() },
				ClassLoader.getSystemClassLoader());
		Thread.currentThread().setContextClassLoader(classLoader);

		// 4. load the class
		Class<?> envInterfaceClass = null;
		try {
			envInterfaceClass = Class.forName(mainClass, true, classLoader);
		} catch (final ClassNotFoundException e) {
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"", e);
		}

		// 5. get an instance of the class
		EnvironmentInterfaceStandard ei = null;
		try {
			ei = (EnvironmentInterfaceStandard) envInterfaceClass.getConstructor().newInstance();
		} catch (final Exception e) {
			throw new IOException("Class \"" + mainClass + "\" could not be loaded from \"" + file + "\"", e);
		}

		// check version
		if (version.equals(ei.requiredVersion())) {
			return ei;
		} else {
			throw new IOException("Loaded environment interface version does not match the required one \"" + version
					+ "\" vs. \"" + ei.requiredVersion() + "\"");
		}
	}
}
