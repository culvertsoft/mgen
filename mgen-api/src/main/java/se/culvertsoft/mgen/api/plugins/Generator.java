package se.culvertsoft.mgen.api.plugins;

import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.GeneratedSourceFile;
import se.culvertsoft.mgen.api.model.Project;

/**
 * Interface for all source code generators. Users can derive from this class to
 * generate code in their own language and/or format.
 */
public interface Generator {

	/**
	 * Generates code as a list of GeneratedSourceFile objects. Implement this
	 * interface and overload this method to generate source code with your own
	 * Generator.
	 * 
	 * @param project
	 *            The root Project to generate source code for
	 * 
	 * @param generatorSettings
	 *            The settings to be used when generating code
	 * 
	 * @return The generated code
	 */
	List<GeneratedSourceFile> generate(
			final Project project,
			final Map<String, String> generatorSettings);

}
