package se.culvertsoft.mgen.api.plugins;

import java.util.Map;

import se.culvertsoft.mgen.api.model.Project;

public interface Parser {

	/**
	 * A parser is responsible for taking a map of settings, deciding which
	 * files to parse, and producing a Project object which can later be used to
	 * generate code from.
	 * 
	 * @param settings
	 * @return
	 */
	public Project parse(final Map<String, String> settings);

}
