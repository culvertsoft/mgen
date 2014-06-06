package se.culvertsoft.mgen.javapack.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import se.culvertsoft.mgen.api.exceptions.TypeConflictException;

public class ClassRegistry {

	private final HashMap<String, ClassRegistryEntry> m_clsName2Entry;
	private final HashMap<Short, ClassRegistryEntry> m_clsHash16bit2Entry;
	private final HashMap<Integer, ClassRegistryEntry> m_clsHash32bit2Entry;
	private final HashMap<Class<?>, ClassRegistryEntry> m_cls2Entry;

	public ClassRegistry() {
		m_clsName2Entry = new HashMap<String, ClassRegistryEntry>();
		m_clsHash16bit2Entry = new HashMap<Short, ClassRegistryEntry>();
		m_clsHash32bit2Entry = new HashMap<Integer, ClassRegistryEntry>();
		m_cls2Entry = new HashMap<Class<?>, ClassRegistryEntry>();
	}

	public void add(final ClassRegistryEntry entry) {

		ensureNoConflict(
				entry,
				m_clsName2Entry.get(entry.clsName()),
				"class name");
		ensureNoConflict(
				entry,
				m_clsHash16bit2Entry.get(entry.typeHash16bit()),
				"type hash (16bit)");
		ensureNoConflict(
				entry,
				m_clsHash32bit2Entry.get(entry.typeHash32bit()),
				"type hash (32bit)");

		m_clsName2Entry.put(entry.clsName(), entry);
		m_clsHash16bit2Entry.put(entry.typeHash16bit(), entry);
		m_clsHash32bit2Entry.put(entry.typeHash32bit(), entry);
		m_cls2Entry.put(entry.cls(), entry);
	}

	private void ensureNoConflict(
			final ClassRegistryEntry newEntry,
			final ClassRegistryEntry oldEntry,
			final String reason) {
		if (oldEntry != null && !newEntry.equals(oldEntry)) {
			throw new TypeConflictException("Failed (conflict reason: "
					+ reason + ") adding new entry '" + newEntry + "' to "
					+ this + " due to conflict with old entry '" + oldEntry
					+ "'");
		}
	}

	public void add(
			final String className,
			final short typeHash16bit,
			final int typeHash32bit,
			final Class<?> cls,
			final Ctor ctor) {
		add(new ClassRegistryEntry(
				className,
				typeHash16bit,
				typeHash32bit,
				cls,
				ctor));
	}

	public void add(final ClassRegistry registry) {
		for (final ClassRegistryEntry entry : registry.entries())
			add(entry);
	}

	public ClassRegistryEntry getByClass(final Class<?> cls) {
		return m_cls2Entry.get(cls);
	}

	public ClassRegistryEntry getByName(final String fullClassName) {
		return m_clsName2Entry.get(fullClassName);
	}

	public ClassRegistryEntry getBy16bitHash(final short hash) {
		return m_clsHash16bit2Entry.get(hash);
	}

	public ClassRegistryEntry getBy32bitHash(final int hash) {
		return m_clsHash32bit2Entry.get(hash);
	}

	public Set<Short> registered16bitHashes() {
		return m_clsHash16bit2Entry.keySet();
	}

	public Set<Integer> registered32bitHashes() {
		return m_clsHash32bit2Entry.keySet();
	}

	public Set<String> registeredClassNames() {
		return m_clsName2Entry.keySet();
	}

	public Set<Class<?>> registeredClasses() {
		return m_cls2Entry.keySet();
	}

	public Collection<ClassRegistryEntry> entries() {
		return m_clsName2Entry.values();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((m_clsHash16bit2Entry == null) ? 0 : m_clsHash16bit2Entry
						.hashCode());
		result = prime
				* result
				+ ((m_clsHash32bit2Entry == null) ? 0 : m_clsHash32bit2Entry
						.hashCode());
		result = prime * result
				+ ((m_clsName2Entry == null) ? 0 : m_clsName2Entry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassRegistry other = (ClassRegistry) obj;
		if (m_clsHash16bit2Entry == null) {
			if (other.m_clsHash16bit2Entry != null)
				return false;
		} else if (!m_clsHash16bit2Entry.equals(other.m_clsHash16bit2Entry))
			return false;
		if (m_clsHash32bit2Entry == null) {
			if (other.m_clsHash32bit2Entry != null)
				return false;
		} else if (!m_clsHash32bit2Entry.equals(other.m_clsHash32bit2Entry))
			return false;
		if (m_clsName2Entry == null) {
			if (other.m_clsName2Entry != null)
				return false;
		} else if (!m_clsName2Entry.equals(other.m_clsName2Entry))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("ClassRegistry with classes:").append('\n');
		for (final ClassRegistryEntry entry : m_clsName2Entry.values()) {
			sb.append("  ").append(entry.toString()).append('\n');
		}
		return sb.toString();
	}

}
