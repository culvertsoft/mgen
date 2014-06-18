package se.culvertsoft.mgen.javapack.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ClassRegistry {

	private final HashMap<Long, ClassRegistryEntry> m_typeId2Entry;
	private final HashMap<String, ClassRegistryEntry> m_name2Entry;

	public ClassRegistry() {
		m_typeId2Entry = new HashMap<Long, ClassRegistryEntry>();
		m_name2Entry = new HashMap<String, ClassRegistryEntry>();
	}

	public void add(final ClassRegistryEntry entry) {
		m_typeId2Entry.put(entry.typeId(), entry);
		m_name2Entry.put(entry.clsName(), entry);
	}

	public void add(final long typeId, final String className, final Ctor ctor) {
		add(new ClassRegistryEntry(typeId, className, ctor));
	}

	public void add(final ClassRegistry registry) {
		for (final ClassRegistryEntry entry : registry.entries())
			add(entry);
	}

	public long typeIds16Bit2TypeId(final short[] ids) {
		return -1;
	}

	public long typeIds16Base64Bit2TypeId(final String[] ids) {
		return -1;
	}

	public long names2TypeId(final String[] ids) {
		return -1;
	}

	public final MGenBase instantiateByTypeId(final long typeId) {
		final ClassRegistryEntry entry = m_typeId2Entry.get(typeId);
		return entry != null ? entry.construct() : null;
	}
	
	public MGenBase instantiateByTypeIds16Bit(final short[] ids) {
		return null;
	}

	public MGenBase instantiateByTypeIds16BitBase64(final String[] ids) {
		return null;
	}

	public MGenBase instantiateByNames(final String[] ids) {
		return null;
	}

	public ClassRegistryEntry getByName(final String fullClassName) {
		return m_name2Entry.get(fullClassName);
	}

	public ClassRegistryEntry getById(final long typeId) {
		return m_typeId2Entry.get(typeId);
	}

	public Set<Long> registeredIds() {
		return m_typeId2Entry.keySet();
	}

	public Set<String> registeredClassNames() {
		return m_name2Entry.keySet();
	}

	public Collection<ClassRegistryEntry> entries() {
		return m_name2Entry.values();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("ClassRegistry with classes:").append('\n');
		for (final ClassRegistryEntry entry : m_name2Entry.values()) {
			sb.append("  ").append(entry.toString()).append('\n');
		}
		return sb.toString();
	}

}
