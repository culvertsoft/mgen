package se.culvertsoft.mgen.javapack.serialization;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;

abstract public class BuiltInReader implements Reader {

	protected static final Charset charset = Charset.forName("UTF8");
	protected static final short[] NO_IDS = new short[0];

	protected final CharsetDecoder stringDecoder;

	protected final ClassRegistryBase m_clsReg;

	protected BuiltInReader(ClassRegistryBase classRegistry) {
		m_clsReg = classRegistry;
		stringDecoder = charset
				.newDecoder()
				.onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

	protected MGenBase instantiate(String[] ids, CustomType expType) {

		final ClassRegistryEntry entry = ids != null ? m_clsReg.getByTypeIds16BitBase64(ids)
				: m_clsReg.getById(expType.typeId());

		if (expType != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: " + Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(expType.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected " + expType.fullName()
						+ " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

	protected MGenBase instantiate(short[] ids, CustomType expType) {

		if (ids == null && expType == null)
			return null;

		final ClassRegistryEntry entry = ids != null ? m_clsReg.getByTypeIds16Bit(ids) : m_clsReg
				.getById(expType.typeId());

		if (expType != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: " + Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(expType.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected " + expType.fullName()
						+ " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

	protected ClassRegistryEntry getRegEntry(final Class<?> typ) {

		final ClassRegistryEntry entry = m_clsReg.getByClass(typ);

		if (entry == null)
			throw new UnknownTypeException("Could not read object of type " + typ
					+ ", since it is know known by the class registry");

		return entry;

	}

	protected String decodeString(final byte[] data) {
		try {
			return stringDecoder.decode(ByteBuffer.wrap(data)).toString();
		} catch (CharacterCodingException x) {
			throw new Error(x); // Can't happen
		}
	}

}
