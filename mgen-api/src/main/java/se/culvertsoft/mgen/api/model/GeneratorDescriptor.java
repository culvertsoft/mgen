package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a code generator class selected by the compiler.
 * GeneratorDescriptors are created for each 'Generator' section in the project
 * files of the IDL.
 */
public class GeneratorDescriptor {

	/**
	 * Gets the name of this generator. This is not the class name but just an
	 * identifier for the compiler (e.g. C++, Java, JavaScript, etc).
	 * 
	 * @return The name of this generator
	 */
	public String getGeneratorName() {
		return m_generatorName;
	}

	/**
	 * Gets the class path/qualified class name of the generator that this
	 * descriptor represents.
	 * 
	 * @return The class path/qualified class name of the generator
	 */
	public String getGeneratorClassPath() {
		return m_generatorClassName;
	}

	/**
	 * Gets the additional settings specified for this generator. Usually these
	 * settings are specified in the IDL together with the generator itself.
	 * 
	 * @return The additional settings specified for this generator
	 */
	public Map<String, String> getGeneratorSettings() {
		return m_generatorSettings;
	}

	/**
	 * Creates a new GeneratorDescriptor
	 * 
	 * @param generatorName
	 *            The name of this generator. This is not the class name but
	 *            just an identifier for the compiler (e.g. C++, Java,
	 *            JavaScript, etc).
	 * 
	 * @param generatorClassName
	 *            The qualified class name of the generator. Needs to be
	 *            precise, because it will be used to later instantiate the
	 *            generator.
	 * 
	 * @param generatorSettings
	 *            The additional settings specified for this generator
	 */
	public GeneratorDescriptor(
			final String generatorName,
			final String generatorClassName,
			final Map<String, String> generatorSettings) {
		m_generatorName = generatorName;
		m_generatorClassName = generatorClassName;
		m_generatorSettings = generatorSettings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "SelectedGenerator [generatorName=" + m_generatorName
				+ ", generatorClassName=" + m_generatorClassName
				+ ", generatorSettings=" + m_generatorSettings + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((m_generatorClassName == null) ? 0 : m_generatorClassName
						.hashCode());
		result = prime * result
				+ ((m_generatorName == null) ? 0 : m_generatorName.hashCode());
		result = prime
				* result
				+ ((m_generatorSettings == null) ? 0 : m_generatorSettings
						.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneratorDescriptor other = (GeneratorDescriptor) obj;
		if (m_generatorClassName == null) {
			if (other.m_generatorClassName != null)
				return false;
		} else if (!m_generatorClassName.equals(other.m_generatorClassName))
			return false;
		if (m_generatorName == null) {
			if (other.m_generatorName != null)
				return false;
		} else if (!m_generatorName.equals(other.m_generatorName))
			return false;
		if (m_generatorSettings == null) {
			if (other.m_generatorSettings != null)
				return false;
		} else if (!m_generatorSettings.equals(other.m_generatorSettings))
			return false;
		return true;
	}

	private final String m_generatorName;
	private final String m_generatorClassName;
	private final Map<String, String> m_generatorSettings;

}
