package se.culvertsoft.mgen.api.plugins;

import java.io.File;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ParsedSources;
import se.culvertsoft.mgen.api.model.Project;

public interface Parser {

	/**
	 * New parser APi
	 */
	ParsedSources parse(
			final List<File> sources,
			final Map<String, String> settings,
			final Project parent);

}
