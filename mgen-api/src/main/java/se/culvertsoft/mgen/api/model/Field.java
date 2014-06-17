package se.culvertsoft.mgen.api.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.Hasher;

public class Field {

	private final String m_ownerClassName;
	private final String m_name;
	private final Type m_type;
	private final List<String> m_flags;
	private final Set<CustomType> m_directDependencies;
	private final short m_16bitHash;
	private final String m_16bitHashBase64;
	private final boolean m_required;
	private final boolean m_polymorphic;
	private final boolean m_parked;

	@SuppressWarnings("unchecked")
	public Field(
			final String ownerClassName,
			final String name,
			final Type type,
			final List<String> flags) {
		m_ownerClassName = ownerClassName;
		m_name = name;
		m_type = type;
		m_flags = flags != null ? flags : (List<String>) Collections.EMPTY_LIST;
		m_directDependencies = new HashSet<CustomType>();
		m_16bitHash = Hasher.static_16bit(m_name);
		m_16bitHashBase64 = Base64.encode(m_16bitHash);
		m_required = m_flags.contains("required");
		m_polymorphic = m_flags.contains("polymorphic");
		m_parked = m_flags.contains("parked");
		traceDirectDependencies(m_directDependencies, m_type);
	}

	private static void traceDirectDependencies(
			final Set<CustomType> out,
			final Type t) {
		switch (t.typeEnum()) {
		case ARRAY:
			traceDirectDependencies(out, ((ArrayType) t).elementType());
			break;
		case LIST:
			traceDirectDependencies(out, ((ListType) t).elementType());
			break;
		case MAP:
			traceDirectDependencies(out, ((MapType) t).keyType());
			traceDirectDependencies(out, ((MapType) t).valueType());
			break;
		case CUSTOM:
			out.add((CustomType) t);
			break;
		default:
			break;
		}
	}

	public String name() {
		return m_name;
	}

	public Type typ() {
		return m_type;
	}

	public List<String> flags() {
		return m_flags;
	}

	public Field transformToType(final Type type) {
		return new Field(m_ownerClassName, m_name, type, m_flags);
	}

	public short fieldHash16bit() {
		return m_16bitHash;
	}

	public String fieldHash16bitBase64() {
		return m_16bitHashBase64;
	}
	
	public boolean isRequired() {
		return m_required;
	}

	public boolean isPolymorphic() {
		return m_polymorphic;
	}

	public boolean isParked() {
		return m_parked;
	}

	@Override
	public String toString() {
		return m_name + ": " + m_type + " (flags: " + m_flags + ")";
	}

	public Set<CustomType> getDirectDependencies() {
		return m_directDependencies;
	}

}
