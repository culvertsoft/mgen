package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public abstract class DynamicWriter extends BuiltInWriter {

	private final boolean m_useHashes;
	private final Charset m_charset = Charset.forName("UTF8");

	public DynamicWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final boolean useHashes) {
		super(
				outputStream instanceof DataOutputStream ? (DataOutputStream) outputStream
						: new DataOutputStream(outputStream),
				classRegistry);
		m_useHashes = useHashes;
	}

	protected List<String> typeIdsOf(final MGenBase o) {
		final ArrayList<String> out = new ArrayList<String>();
		if (m_useHashes) {
			for (final String base64Hash : o._typeHashes16bitBase64()) {
				out.add(base64Hash);
			}
		} else {
			for (final String typeName : o._typeNames()) {
				out.add(typeName);
			}
		}
		return out;
	}

	protected String typeIdOf(final Type type) {
		if (m_useHashes) {
			return type.typeHash16bitBase64String();
		} else {
			return type.fullName();
		}
	}

	protected void write(final boolean i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final byte i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final short i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final int i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final long i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final float f) throws IOException {
		write(String.valueOf(f));
	}

	protected void write(final double f) throws IOException {
		write(String.valueOf(f));
	}

	protected void write(final String text) throws IOException {
		stream.write(text.getBytes(m_charset));
	}

}
