package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.CRC64;

public class LinkedCustomType extends TypeImpl implements CustomType {

	private final String m_name;
	private final String m_fullName;
	private final Module m_module;
	private final short m_id16Bit;
	private CustomType m_superType;
	private List<CustomType> m_superTypeHierarchy;
	private List<CustomType> m_subTypes;
	private ArrayList<Field> m_fields;

	private ArrayList<Field> m_fieldsInclSuper;
	private Set<CustomType> m_allReferencedTypes;
	private Set<CustomType> m_directDependencies;

	public LinkedCustomType(
			final String name,
			final Module module,
			final short id16Bit,
			final CustomType superType) {
		super(TypeEnum.CUSTOM);
		m_name = name;
		m_fullName = module.path() + "." + m_name;
		m_id16Bit = id16Bit;
		m_module = module;
		m_superType = superType;
		m_fields = new ArrayList<Field>();
		m_superTypeHierarchy = null;
		m_subTypes = new ArrayList<CustomType>();
		m_fieldsInclSuper = null;
		m_allReferencedTypes = null;
		m_directDependencies = null;
	}

	@Override
	public long typeId() {
		return CRC64.calc(m_fullName);
	}

	@Override
	public String typeId16BitBase64() {
		return Base64.encode(typeId16Bit());
	}

	@Override
	public short typeId16Bit() {
		return m_id16Bit;
	}

	public void setSuperType(final CustomType superType) {
		m_superType = superType;
	}

	public void setFields(final List<Field> fields) {
		m_fields = new ArrayList<Field>(fields);
	}

	public void addField(final Field field) {
		m_fields.add(field);
	}

	public String name() {
		return m_name;
	}

	public Module module() {
		return m_module;
	}

	public CustomType superType() {
		return m_superType;
	}

	@Override
	public String fullName() {
		return m_fullName;
	}

	public List<Field> fields() {
		return m_fields;
	}

	@Override
	public String shortName() {
		return m_name;
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public List<Field> fieldsInclSuper() {

		if (m_fieldsInclSuper == null) {

			m_fieldsInclSuper = new ArrayList<Field>();

			if (hasSuperType()) {
				m_fieldsInclSuper
						.addAll(superType().fieldsInclSuper());
			}

			m_fieldsInclSuper.addAll(m_fields);

		}

		return m_fieldsInclSuper;
	}

	@Override
	public Set<CustomType> referencedTypes() {

		if (m_allReferencedTypes == null) {

			m_allReferencedTypes = new HashSet<CustomType>();

			m_allReferencedTypes.add(this);

			if (hasSuperType()) {
				m_allReferencedTypes.addAll(superType()
						.referencedTypes());
			}

			for (final Field field : m_fields)
				m_allReferencedTypes.addAll(field
						.typ()
						.referencedTypes());

		}

		return m_allReferencedTypes;

	}

	@Override
	public Set<CustomType> directDependencies() {

		if (m_directDependencies == null) {

			m_directDependencies = new HashSet<CustomType>();

			if (hasSuperType()) {
				m_directDependencies.add(superType());
			}

			for (final Field f : m_fields) {
				m_directDependencies.addAll(f.directDependencies());
			}

		}

		return m_directDependencies;
	}

	@Override
	public List<CustomType> superTypeHierarchy() {

		if (m_superTypeHierarchy == null) {

			final List<CustomType> l = new ArrayList<CustomType>();

			if (hasSuperType())
				l.addAll(superType().superTypeHierarchy());

			l.add(this);

			m_superTypeHierarchy = l;

		}

		return m_superTypeHierarchy;
	}

	@Override
	public Class<?> doClassOf() {
		try {
			return Class.forName(m_fullName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return m_fullName;
	}

	@Override
	public boolean containsCustomType() {
		return true;
	}

	@Override
	public boolean hasSuperType() {
		return superType() != null;
	}

	@Override
	public boolean hasSubTypes() {
		return !subTypes().isEmpty();
	}

	@Override
	public List<CustomType> subTypes() {
		return m_subTypes;
	}

	public CustomType addSubType(final CustomType t) {
		m_subTypes.add(t);
		return this;
	}

}
