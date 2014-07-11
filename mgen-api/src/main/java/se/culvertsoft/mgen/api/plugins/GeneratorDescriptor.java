package se.culvertsoft.mgen.api.plugins;

import java.util.Map;

public class GeneratorDescriptor {

	public GeneratorDescriptor(
			final String generatorName,
			final String generatorClassName,
			final Map<String, String> generatorSettings) {
		this.generatorName = generatorName;
		this.generatorClassName = generatorClassName;
		this.generatorSettings = generatorSettings;
	}

	public String getGeneratorName() {
		return generatorName;
	}

	public String getGeneratorClassPath() {
		return generatorClassName;
	}

	public Map<String, String> getGeneratorSettings() {
		return generatorSettings;
	}

	@Override
	public String toString() {
		return "SelectedGenerator [generatorName=" + generatorName + ", generatorClassName="
				+ generatorClassName + ", generatorSettings=" + generatorSettings + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((generatorClassName == null) ? 0 : generatorClassName.hashCode());
		result = prime * result + ((generatorName == null) ? 0 : generatorName.hashCode());
		result = prime * result + ((generatorSettings == null) ? 0 : generatorSettings.hashCode());
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
		if (generatorClassName == null) {
			if (other.generatorClassName != null)
				return false;
		} else if (!generatorClassName.equals(other.generatorClassName))
			return false;
		if (generatorName == null) {
			if (other.generatorName != null)
				return false;
		} else if (!generatorName.equals(other.generatorName))
			return false;
		if (generatorSettings == null) {
			if (other.generatorSettings != null)
				return false;
		} else if (!generatorSettings.equals(other.generatorSettings))
			return false;
		return true;
	}

	private final String generatorName;
	private final String generatorClassName;
	private final Map<String, String> generatorSettings;
}
