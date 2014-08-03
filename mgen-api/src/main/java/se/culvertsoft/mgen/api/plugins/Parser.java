package se.culvertsoft.mgen.api.plugins;

import java.io.File;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Project;

public interface Parser {

	/**
	 * A parser is responsible for taking a map of settings, deciding which
	 * files to parse, and producing a Project object which can later be used to
	 * generate code from.
	 */
	Project parse(final Map<String, String> settings);

	/**
	 * New parser APi
	 */
	// ParsedSources parse(final List<File> sources, final Map<String, String> settings);

}
