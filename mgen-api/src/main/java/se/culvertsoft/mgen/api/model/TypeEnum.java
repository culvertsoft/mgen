package se.culvertsoft.mgen.api.model;

import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_BOOL;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_CLASS;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT16;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT8;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_LIST;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_MAP;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_STRING;

/**
 * Convenience enumeration for switching on field and class types. Each MGen
 * data model type has one enum.
 */
public enum TypeEnum {
	ENUM(TAG_STRING),
	BOOL(TAG_BOOL),
	INT8(TAG_INT8),
	INT16(TAG_INT16),
	INT32(TAG_INT32),
	INT64(TAG_INT64),
	FLOAT32(TAG_FLOAT32),
	FLOAT64(TAG_FLOAT64),
	STRING(TAG_STRING),
	ARRAY(TAG_LIST),
	LIST(TAG_LIST),
	MAP(TAG_MAP),
	CLASS(TAG_CLASS),
	UNKNOWN(TAG_CLASS);

	TypeEnum(final byte binaryMetadatTag) {
		m_binaryMetadatTag = binaryMetadatTag;
	}

	/**
	 * Gets the binary type tag of the type that this TypeEnum represents. This
	 * is a convenience method primarily for the MGen BinaryWriter and
	 * BinaryReader.
	 * 
	 * @return The binary type tag of the type that this TypeEnum represents
	 */
	public byte binaryMetadatTag() {
		return m_binaryMetadatTag;
	}

	private final byte m_binaryMetadatTag;
}
