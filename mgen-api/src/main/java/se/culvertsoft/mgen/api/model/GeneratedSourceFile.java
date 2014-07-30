package se.culvertsoft.mgen.api.model;

/**
 * Represents a generated source file. Generators should not write to disk
 * directly but return a list of this type - GeneratedSourceFile.
 */
public interface GeneratedSourceFile {

	/**
	 * The actual generated source code
	 */
	String sourceCode();

	/**
	 * The filepath where to write the source file to.
	 */
	String filePath();

}
