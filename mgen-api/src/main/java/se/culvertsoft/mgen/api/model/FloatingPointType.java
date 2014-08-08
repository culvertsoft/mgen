package se.culvertsoft.mgen.api.model;

/**
 * A base class representing all floating point numeric types (float32,
 * float64).
 */
public abstract class FloatingPointType extends PrimitiveType {

	protected FloatingPointType(final TypeEnum enm, final Class<?> cls, final String name) {
		super(enm, cls, name);
	}
}
