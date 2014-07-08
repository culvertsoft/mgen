package se.culvertsoft.mgen.api.model.impl;

import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.exceptions.MGenException;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class UnlinkedCustomType extends TypeImpl implements CustomType {

	private final String m_writtenType;
	private final long m_typeId;

	public UnlinkedCustomType(final String writtenType, final long typeId) {
		super(TypeEnum.UNKNOWN);
		m_writtenType = writtenType;
		m_typeId = typeId;
	}

	@Override
	public long typeId() {
		return m_typeId;
	}

	@Override
	public String fullName() {
		return m_writtenType;
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public String writtenType() {
		return m_writtenType;
	}

	@Override
	public String toString() {
		return fullName();
	}

	@Override
	public boolean isLinked() {
		return false;
	}

	@Override
	public boolean containsCustomType() {
		return true;
	}

	@Override
	public String name() {
		return fullName();
	}

	@Override
	public Class<?> doClassOf() {
		try {
			return Class.forName(fullName());
		} catch (ClassNotFoundException e) {
			throw new MGenException(e);
		}
	}

	@Override
	public Set<CustomType> referencedTypes() {
		throw new MGenException(
				"Type details unknown: Cannot call referencedTypes()");
	}

	@Override
	public String typeId16BitBase64() {
		throw new MGenException(
				"Type details unknown: Cannot call typeId16BitBase64()");
	}

	@Override
	public short typeId16Bit() {
		throw new MGenException(
				"Type details unknown: Cannot call typeId16Bit()");
	}

	@Override
	public Module module() {
		throw new MGenException("Type details unknown: Cannot call module()");
	}

	@Override
	public CustomType superType() {
		throw new MGenException("Type details unknown: Cannot call superType()");
	}

	@Override
	public boolean hasSuperType() {
		throw new MGenException(
				"Type details unknown: Cannot call hasSuperType()");
	}

	@Override
	public boolean hasSubTypes() {
		throw new MGenException(
				"Type details unknown: Cannot call hasSubTypes()");
	}

	@Override
	public List<CustomType> subTypes() {
		throw new MGenException("Type details unknown: Cannot call subTypes()");
	}

	@Override
	public List<CustomType> superTypeHierarchy() {
		throw new MGenException(
				"Type details unknown: Cannot call superTypeHierarchy()");
	}

	@Override
	public List<Field> fields() {
		throw new MGenException("Type details unknown: Cannot call fields()");
	}

	@Override
	public List<Field> fieldsInclSuper() {
		throw new MGenException(
				"Type details unknown: Cannot call fieldsInclSuper()");
	}

	@Override
	public Set<CustomType> directDependencies() {
		throw new MGenException(
				"Type details unknown: Cannot call directDependencies()");
	}

}
