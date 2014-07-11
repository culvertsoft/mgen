package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Float32TypeImpl;

/**
 * Represents a 32 bit floating point number type
 */
public interface Float32Type extends PrimitiveType {

	public final static Float32Type INSTANCE = Float32TypeImpl.INSTANCE;
}
