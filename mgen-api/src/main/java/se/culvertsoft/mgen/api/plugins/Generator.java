package se.culvertsoft.mgen.api.plugins;

import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.GeneratedSourceFile;
import se.culvertsoft.mgen.api.model.Project;

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
	 */
	List<GeneratedSourceFile> generate(
			final Project project,
			final Map<String, String> generatorSettings);

}
