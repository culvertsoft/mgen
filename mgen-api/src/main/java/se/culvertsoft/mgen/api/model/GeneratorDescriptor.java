package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a code generator class selected by the compiler from IDL
 * specifications.
 */
public class GeneratorDescriptor {

	/**
	 * The name of this generator. Note this is not the class name but just an
	 * identifier for the compiler.
	 */
	public String getGeneratorName() {
		return m_generatorName;
	}

	/**
	 * The class path/qualified class name of the generator that this descriptor
	 * represents.
	 */
	public String getGeneratorClassPath() {
		return m_generatorClassName;
	}

	/**
	 * The additional settings specified in the IDL to be used when running this
	 * generator.
	 */
	public Map<String, String> getGeneratorSettings() {
		return m_generatorSettings;
	}

	public GeneratorDescriptor(
			final String generatorName,
			final String generatorClassName,
			final Map<String, String> generatorSettings) {
		m_generatorName = generatorName;
		m_generatorClassName = generatorClassName;
		m_generatorSettings = generatorSettings;
	}

	@Override
	public String toString() {
		return "SelectedGenerator [generatorName=" + m_generatorName
				+ ", generatorClassName=" + m_generatorClassName
				+ ", generatorSettings=" + m_generatorSettings + "]";
	}

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
