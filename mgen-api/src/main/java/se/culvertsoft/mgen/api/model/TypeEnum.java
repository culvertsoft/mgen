package se.culvertsoft.mgen.api.model;

import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_ARRAY;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_BOOL;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_CUSTOM;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT16;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT8;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_LIST;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_MAP;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_STRING;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_UNKNOWN;

public enum TypeEnum {
	BOOL(TAG_BOOL),
	INT8(TAG_INT8),
	INT16(TAG_INT16),
	INT32(TAG_INT32),
	INT64(TAG_INT64),
	FLOAT32(TAG_FLOAT32),
	FLOAT64(TAG_FLOAT64),
	STRING(TAG_STRING),
	ARRAY(TAG_ARRAY),
	LIST(TAG_LIST),
	MAP(TAG_MAP),
	CUSTOM(TAG_CUSTOM),
	UNKNOWN(TAG_UNKNOWN);

	TypeEnum(final byte binaryMetadatTag) {
		m_binaryMetadatTag = binaryMetadatTag;
	}

	public byte binaryMetadatTag() {
		return m_binaryMetadatTag;
	}

	private final byte m_binaryMetadatTag;
}
