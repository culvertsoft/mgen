package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Int64TypeImpl;

/**
 * Represents a 64 bit fixed point number type
 */
public interface Int64Type extends PrimitiveType {

	public final static Int64Type INSTANCE = Int64TypeImpl.INSTANCE;
	
}

