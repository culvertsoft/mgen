package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a code generator class selected by the compiler from IDL
 * specifications.
 */
public interface GeneratorDescriptor {

	/**
	 * The name of this generator. Note this is not the class name but just an
	 * identifier for the compiler.
	 */
	String getGeneratorName();

	/**
	 * The class path/qualified class name of the generator that this descriptor
	 * represents.
	 */
	String getGeneratorClassPath();

	/**
	 * The additional settings specified in the IDL to be used when running this
	 * generator.
	 */
	Map<String, String> getGeneratorSettings();

}
