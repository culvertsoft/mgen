package se.culvertsoft.mgen.api.model.impl;

import java.lang.reflect.Array;
import java.util.Set;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class ArrayTypeImpl extends TypeImpl implements ArrayType {

	private Type m_elementType;

	public ArrayTypeImpl(final Type elementType) {
		super(TypeEnum.ARRAY);
		m_elementType = elementType;
	}

	public ArrayTypeImpl(final String writtenElemType) {
		super(TypeEnum.ARRAY);
		m_elementType = new UnknownCustomTypeImpl(writtenElemType, -1);
	}

	public Type elementType() {
		return m_elementType;
	}

	protected void setElemType(final Type elemType) {
		m_elementType = elemType;
		resetHashCaches();
	}

	@Override
	public String fullName() {
		return "array[" + m_elementType.fullName() + "]";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	@Override
	public boolean isTypeKnown() {
		return m_elementType.isTypeKnown();
	}

	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {
		return m_elementType.getAllReferencedModulesInclSuper();
	}

	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {
		return m_elementType.getAllReferencedTypesInclSuper();
	}

	@Override
	public Class<?> doClassOf() {
		try {
			switch (m_elementType.typeEnum()) {
			case BOOL:
				return boolean[].class;
			case INT8:
				return byte[].class;
			case INT16:
				return short[].class;
			case INT32:
				return int[].class;
			case INT64:
				return long[].class;
			case FLOAT32:
				return float[].class;
			case FLOAT64:
				return double[].class;
			case STRING:
				return String[].class;
			default:
				return Class.forName("[L" + m_elementType.classOf().getName()
						+ ";");
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Object newInstance(final int n) {
		switch (m_elementType.typeEnum()) {
		case BOOL:
			return n > 0 ? new boolean[n] : EMPTY_BOOL_ARRAY;
		case INT8:
			return n > 0 ? new byte[n] : EMPTY_INT8_ARRAY;
		case INT16:
			return n > 0 ? new short[n] : EMPTY_INT16_ARRAY;
		case INT32:
			return n > 0 ? new int[n] : EMPTY_INT32_ARRAY;
		case INT64:
			return n > 0 ? new long[n] : EMPTY_INT64_ARRAY;
		case FLOAT32:
			return n > 0 ? new float[n] : EMPTY_FLOAT32_ARRAY;
		case FLOAT64:
			return n > 0 ? new double[n] : EMPTY_FLOAT64_ARRAY;
		case STRING:
			return n > 0 ? new String[n] : EMPTY_STRING_ARRAY;
		default:
			return Array.newInstance(m_elementType.classOf(), n);
		}
	}

	@Override
	public boolean containsMgenCreatedType() {
		return elementType().containsMgenCreatedType();
	}

}
