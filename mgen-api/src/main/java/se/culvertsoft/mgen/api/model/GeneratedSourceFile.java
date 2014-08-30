package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generated source code file. Generators should not write to disk
 * directly but return a list GeneratedSourceFile objects.
 */
public class GeneratedSourceFile {

	/**
	 * Gets the generated source code of this GeneratedSourceFile
	 * 
	 * @return The generated source code for of GeneratedSourceFile
	 */
	public String sourceCode() {
		return m_sourceCode;
	}

	/**
	 * Gets the file path where to write the source file to.
	 * 
	 * @return The file path where to write the source file to
	 */
	public String filePath() {
		return m_filePath;
	}

	/**
	 * Gets the CustomCodeSection items of this generated source file.
	 * 
	 * @return The CustomCodeSection items of this generated source file
	 */
	public List<CustomCodeSection> customCodeSections() {
		return m_customCodeSections;
	}

	/**
	 * Checks if this generated source has custom code sections.
	 * 
	 * @return If this generated source has custom code sections
	 */
	public boolean hasCustomCodeSections() {
		return m_customCodeSections != null && !m_customCodeSections.isEmpty();
	}

	/**
	 * Creates a new GeneratedSourceFile with the same source code but prepends
	 * a path to the file path
	 * 
	 * @param prefix
	 *            The prefix to append to the file path
	 * 
	 * @return The new GeneratedSourceFile
	 */
	public GeneratedSourceFile transformPrependPath(final String prefix) {
		return new GeneratedSourceFile(prefix + m_filePath, m_sourceCode, m_customCodeSections);
	}

	/**
	 * Creates a new GeneratedSourceFile
	 * 
	 * @param filePath
	 *            The file path to write this GeneratedSourceFile to
	 * 
	 * @param sourceCode
	 *            The source code of this GeneratedSourceFile
	 * 
	 * @param customCodeSections
	 *            The custom code sections that this GeneratedSourceFile should
	 *            have
	 */
	public GeneratedSourceFile(
			final String filePath,
			final String sourceCode,
			final List<CustomCodeSection> customCodeSections) {
		m_filePath = filePath;
		m_sourceCode = sourceCode;
		m_customCodeSections = customCodeSections;
	}

	/**
	 * Creates a new GeneratedSourceFile.
	 * 
	 * @param filePath
	 *            The file path to write this GeneratedSourceFile to
	 * 
	 * @param sourceCode
	 *            The source code of this GeneratedSourceFile
	 */
	public GeneratedSourceFile(final String filePath, final String sourceCode) {
		this(filePath, sourceCode, new ArrayList<CustomCodeSection>());
	}

	private final String m_filePath;
	private final String m_sourceCode;
	private final List<CustomCodeSection> m_customCodeSections;
}
