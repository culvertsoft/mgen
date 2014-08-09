package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generated source file. Generators should not write to disk
 * directly but return a list of this type - GeneratedSourceFile.
 */
public class GeneratedSourceFile {

	/**
	 * The actual generated source code
	 */
	public String sourceCode() {
		return m_sourceCode;
	}

	/**
	 * The filepath where to write the source file to.
	 */
	public String filePath() {
		return m_filePath;
	}

	/**
	 * Returns the CustomCodeSection items of this generated source file.
	 */
	public List<CustomCodeSection> customCodeSections() {
		return m_customCodeSections;
	}

	/**
	 * Creates a new GeneratedSourceFile with the same source code but prepends
	 * a path to the file path
	 */
	public GeneratedSourceFile transformPrependPath(final String prefix) {
		return new GeneratedSourceFile(prefix + m_filePath, m_sourceCode, m_customCodeSections);
	}

	public GeneratedSourceFile(
			final String filePath,
			final String sourceCode,
			final List<CustomCodeSection> customCodeSections) {
		m_filePath = filePath;
		m_sourceCode = sourceCode;
		m_customCodeSections = customCodeSections;
	}

	public GeneratedSourceFile(final String filePath, final String sourceCode) {
		this(filePath, sourceCode, new ArrayList<CustomCodeSection>());
	}

	private final String m_filePath;
	private final String m_sourceCode;
	private final List<CustomCodeSection> m_customCodeSections;
}
