package se.culvertsoft.mgen.api.plugins;

import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Module;

/**
 * Base interface for all source code generators. Users can derive from this
 * class to generate code in their own language and/or format.
 * 
 * @author GiGurra
 * 
 */
public interface Generator {

	/**
	 * Overload this method to generate you own source code.
	 * 
	 * @param modules
	 * @param generatorSettings
	 * @return Generated source code and the output file paths.
	 */
	public abstract List<GeneratedSourceFile> generate(
			final List<Module> modules,
			final Map<String, String> generatorSettings);

}
