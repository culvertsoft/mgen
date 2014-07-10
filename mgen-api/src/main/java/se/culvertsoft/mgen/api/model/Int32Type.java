package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Int32TypeImpl;

/**
 * Represents a 32 bit fixed point number type
 */
public interface Int32Type extends PrimitiveType {

	public final static Int32Type INSTANCE = Int32TypeImpl.INSTANCE;

}
