package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue;

/**
 * Represents an unlinked (before the compiler's type linkage stage) value.
 */
public abstract class UnlinkedDefaultValueImpl extends DefaultValueImpl
		implements UnlinkedDefaultValue {

	public UnlinkedDefaultValueImpl() {
		super(null);
	}

}
