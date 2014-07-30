package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.List;

import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.CRC16;

public class FieldImpl implements Field {

	private final String m_ownerClassName;
	private final String m_name;
	private final Type m_type;
	private final List<String> m_flags;

	private final short m_id;
	private final DefaultValue m_defaultValue;
	private final String m_idBase64;

	private final boolean m_required;
	private final boolean m_polymorphic;
	private final boolean m_parked;
	private final boolean m_transient;

	@SuppressWarnings("unchecked")
	public FieldImpl(
			final String ownerClassName,
			final String name,
			final Type type,
			final List<String> flags,
			final short id,
			final DefaultValue defaultValue) {
		m_ownerClassName = ownerClassName;
		m_name = name;
		m_type = type;
		m_flags = flags != null ? flags : (List<String>) Collections.EMPTY_LIST;
		m_id = id;
		m_defaultValue = defaultValue;
		m_idBase64 = Base64.encode(m_id);
		m_required = m_flags.contains("required");
		m_polymorphic = m_flags.contains("polymorphic");
		m_parked = m_flags.contains("parked");
		m_transient = m_flags.contains("transient");
	}

	public FieldImpl(
			final String ownerClassName,
			final String name,
			final Type type,
			final List<String> flags,
			final short id) {
		this(ownerClassName, name, type, flags, id, null);
	}

	@Override
	public String name() {
		return m_name;
	}

	@Override
	public Type typ() {
		return m_type;
	}

	@Override
	public List<String> flags() {
		return m_flags;
	}

	public FieldImpl transform(final Type type) {
		return new FieldImpl(m_ownerClassName, m_name, type, m_flags, m_id, m_defaultValue);
	}

	public FieldImpl transform(final DefaultValue v) {
		return new FieldImpl(m_ownerClassName, m_name, m_type, m_flags, m_id, v);
	}

	@Override
	public short id() {
		return m_id;
	}

	@Override
	public DefaultValue defaultValue() {
		return m_defaultValue;
	}

	@Override
	public boolean hasIdOverride() {
		return m_id != CRC16.calc(m_name);
	}

	@Override
	public String idBase64() {
		return m_idBase64;
	}

	@Override
	public boolean isRequired() {
		return m_required;
	}

	public boolean isPolymorphic() {
		return m_polymorphic;
	}

	@Override
	public boolean isParked() {
		return m_parked;
	}

	@Override
	public boolean isTransient() {
		return m_transient;
	}

	@Override
	public boolean isLinked() {
		return m_type.isLinked() && (!hasDefaultValue() || m_defaultValue.isLinked());
	}

	@Override
	public boolean hasDefaultValue() {
		return m_defaultValue != null;
	}

	@Override
	public String toString() {
		return m_name + ": " + m_type + " (flags: " + m_flags + ")";
	}

}
