package se.culvertsoft.mgen.compiler.defaultparser;

import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.StringType;

/**
 * Represents a default value for a string field/type.
 */
public class StringDefaultValue extends DefaultValue {

	protected StringDefaultValue(final StringType typ, String writtenString) {
		super(typ, writtenString);
	}

}
