package se.culvertsoft.mgen.api.plugins;

import java.io.File;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Project;

/**
 * Interface for all IDL parsers. You can either use one of the parsers supplied
 * with mgen or write your own that implement this interface. You can specify
 * idl parser class paths in the project files. Parser classes can be injected
 * by adding them to the mgen compiler's java class path or use the plugin_paths
 * command line parameter to supply your own jar files.
 * 
 * The parsers job is to add new or fill existing modules with types. It does
 * not need to perform linking of field types, super types or default values -
 * Linking will be performed by the compiler after parsing all sources.
 */
public interface Parser {

	/**
	 * Parses a set of idl source files as specified in project files.
	 */
	void parse(
			final List<File> sources,
			final Map<String, String> settings,
			final Project parent);

}
