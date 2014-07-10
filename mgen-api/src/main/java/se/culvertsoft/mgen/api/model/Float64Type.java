package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Float64TypeImpl;

/**
 * Represents a 64 bit floating point number type
 */
public interface Float64Type extends PrimitiveType {
	
	public final static Float64Type INSTANCE = Float64TypeImpl.INSTANCE;
	
}
