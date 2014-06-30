package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;

abstract public class BuiltInReader implements Reader {

	protected static final Charset charset = Charset.forName("UTF8");
	protected static final short[] NO_IDS = new short[0];

	protected final DataInputStream m_stream;
	protected final ClassRegistry m_clsReg;

	protected BuiltInReader(DataInputStream stream, ClassRegistry classRegistry) {
		m_stream = stream;
		m_clsReg = classRegistry;
	}

	protected MGenBase instantiate(String[] ids, UnknownCustomType expType) {

		final ClassRegistryEntry entry = ids != null ? m_clsReg
				.getByTypeIds16BitBase64(ids) : m_clsReg.getById(expType
				.typeId());

		if (expType != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: "
						+ Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(expType.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected "
						+ expType.fullName() + " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

	protected MGenBase instantiate(short[] ids, UnknownCustomType expType) {

		final ClassRegistryEntry entry = ids != null ? m_clsReg
				.getByTypeIds16Bit(ids) : m_clsReg.getById(expType.typeId());

		if (expType != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: "
						+ Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(expType.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected "
						+ expType.fullName() + " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

}
