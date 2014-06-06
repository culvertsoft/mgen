package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.MGenBaseType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class CustomTypeImpl extends TypeImpl implements CustomType {

	private String m_name;
	private String m_fullName;
	private Module m_module;
	private Type m_superType;
	private List<Type> m_typeHierarchy;
	private ArrayList<Field> m_fields;

	private ArrayList<Field> m_allFieldsInclSuper;
	private Set<Module> m_allReferencedModules;
	private Set<Module> m_allReferencedExtModules;
	private Set<CustomType> m_allReferencedTypes;
	private Set<CustomType> m_allReferencedTypesExclSuper;
	private Set<CustomType> m_allReferencedExtTypes;
	private Set<CustomType> m_directDependencies;

	public CustomTypeImpl(
			final String name,
			final Module module,
			final Type superType,
			final List<Field> fields) {
		super(TypeEnum.CUSTOM);
		m_name = name;
		m_fullName = module.path() + "." + m_name;
		m_module = module;
		m_superType = superType;
		m_fields = new ArrayList<Field>(fields);
		m_allFieldsInclSuper = null;
		m_allReferencedModules = null;
		m_allReferencedExtModules = null;
		m_allReferencedTypes = null;
		m_allReferencedTypesExclSuper = null;
		m_allReferencedExtTypes = null;
		m_directDependencies = null;
	}

	public CustomTypeImpl(
			final String name,
			final Module module,
			final Type superType) {
		super(TypeEnum.CUSTOM);
		m_name = name;
		m_fullName = module.path() + "." + m_name;
		m_module = module;
		m_superType = superType;
		m_fields = new ArrayList<Field>();
		m_allReferencedModules = null;
		m_allReferencedExtModules = null;
		m_allReferencedTypes = null;
		m_allReferencedExtTypes = null;
	}

	public void setSuperType(final Type superType) {
		m_superType = superType;
		resetHashCaches();
	}

	public void setFields(final List<Field> fields) {
		m_fields = new ArrayList<Field>(fields);
		resetHashCaches();
	}

	public void addField(final Field field) {
		m_fields.add(field);
		resetHashCaches();
	}

	public String name() {
		return m_name;
	}

	public Module module() {
		return m_module;
	}

	public Type superType() {
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
	public boolean isTypeKnown() {
		return true;
	}

	@Override
	public List<Field> getAllFieldsInclSuper() {

		if (m_allFieldsInclSuper == null) {

			m_allFieldsInclSuper = new ArrayList<Field>();

			final Type s = superType();
			if (s.typeEnum() == TypeEnum.CUSTOM)
				m_allFieldsInclSuper.addAll(((CustomType) s)
						.getAllFieldsInclSuper());

			m_allFieldsInclSuper.addAll(m_fields);

		}

		return m_allFieldsInclSuper;
	}

	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {

		if (m_allReferencedModules == null) {

			m_allReferencedModules = new HashSet<Module>();

			m_allReferencedModules.add(module());
			m_allReferencedModules.addAll(superType()
					.getAllReferencedModulesInclSuper());

			for (final Field field : m_fields)
				m_allReferencedModules.addAll(field
						.typ()
						.getAllReferencedModulesInclSuper());

		}

		return m_allReferencedModules;
	}

	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {

		if (m_allReferencedTypes == null) {

			m_allReferencedTypes = new HashSet<CustomType>();

			m_allReferencedTypes.add(this);
			m_allReferencedTypes.addAll(superType()
					.getAllReferencedTypesInclSuper());

			for (final Field field : m_fields)
				m_allReferencedTypes.addAll(field
						.typ()
						.getAllReferencedTypesInclSuper());

		}

		return m_allReferencedTypes;

	}

	@Override
	public Set<CustomType> getAllReferencedTypesExclSuper() {

		if (m_allReferencedTypesExclSuper == null) {

			m_allReferencedTypesExclSuper = new HashSet<CustomType>();

			for (final Field field : m_fields)
				m_allReferencedTypesExclSuper.addAll(field
						.typ()
						.getAllReferencedTypesInclSuper());

		}

		return m_allReferencedTypesExclSuper;

	}

	@Override
	public Set<Module> getAllReferencedExtModulesInclSuper() {

		if (m_allReferencedExtModules == null) {

			m_allReferencedExtModules = new HashSet<Module>();

			for (final Module m : getAllReferencedModulesInclSuper()) {
				if (m != module()) {
					m_allReferencedExtModules.add(m);
				}
			}

		}

		return m_allReferencedExtModules;

	}

	@Override
	public Set<CustomType> getAllReferencedExtTypesInclSuper() {

		if (m_allReferencedExtTypes == null) {

			m_allReferencedExtTypes = new HashSet<CustomType>();

			for (final Type t : getAllReferencedTypesInclSuper()) {
				if (t.typeEnum() == TypeEnum.CUSTOM) {
					final CustomType customType = (CustomType) t;
					if (customType.module() != module()) {
						m_allReferencedExtTypes.add(customType);
					}
				}
			}

		}

		return m_allReferencedExtTypes;

	}

	@Override
	public Set<CustomType> getDirectDependencies() {

		if (m_directDependencies == null) {

			m_directDependencies = new HashSet<CustomType>();

			if (superType().typeEnum() == TypeEnum.CUSTOM) {
				m_directDependencies.add((CustomType) superType());
			}

			for (final Field f : m_fields) {
				m_directDependencies.addAll(f.getDirectDependencies());
			}

		}

		return m_directDependencies;
	}

	@Override
	public List<Type> typeHierarchy() {

		if (m_typeHierarchy == null) {

			m_typeHierarchy = new ArrayList<Type>();

			if (superType().typeEnum() == TypeEnum.CUSTOM)
				m_typeHierarchy.addAll(((CustomType) superType())
						.typeHierarchy());

			m_typeHierarchy.add(this);

		}

		return m_typeHierarchy;
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
	public boolean containsMgenCreatedType() {
		return true;
	}

	@Override
	public boolean hasSuperType() {
		return superType() != MGenBaseType.INSTANCE;
	}

}
