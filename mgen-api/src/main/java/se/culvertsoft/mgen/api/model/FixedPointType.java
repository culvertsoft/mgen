package se.culvertsoft.mgen.api.model;

/**
 * A base class representing all fixed point numeric types (int8, int16, int32,
 * int64).
 */
public abstract class FixedPointType extends PrimitiveType {

	protected FixedPointType(final TypeEnum enm, final Class<?> cls, final String name) {
		super(enm, cls, name);
	}

}
