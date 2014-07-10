package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Int16TypeImpl;

/**
 * Represents a 16 bit fixed point number type
 */
public interface Int16Type extends PrimitiveType {

	public final static Int16Type INSTANCE = Int16TypeImpl.INSTANCE;
}
