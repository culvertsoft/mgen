package se.culvertsoft.mgen.api.model;

/**
 * Single byte type tags used by the MGen BinaryWriter and BinaryReader for
 * indicating what data to write/read. One tag exists for each binary wire
 * format type. Note that some binary wire format tags can represent several
 * data model types (e.g. TAG_STRING represent Strings and Enums).
 */
public interface BinaryTypeTag {

	/**
	 * Binary type tag used for booleans in the mgen binary wire format
	 */
	public static final byte TAG_BOOL = (byte) 0;

	/**
	 * Binary type tag used for bytes/int8s in the mgen binary wire format
	 */
	public static final byte TAG_INT8 = (byte) 1;

	/**
	 * Binary type tag used for int16s in the mgen binary wire format
	 */
	public static final byte TAG_INT16 = (byte) 2;

	/**
	 * Binary type tag used for int32s in the mgen binary wire format
	 */
	public static final byte TAG_INT32 = (byte) 3;

	/**
	 * Binary type tag used for int64s in the mgen binary wire format
	 */
	public static final byte TAG_INT64 = (byte) 4;

	/**
	 * Binary type tag used for float32s in the mgen binary wire format
	 */
	public static final byte TAG_FLOAT32 = (byte) 5;

	/**
	 * Binary type tag used for float64s in the mgen binary wire format
	 */
	public static final byte TAG_FLOAT64 = (byte) 6;

	/**
	 * Binary type tag used for strings and enums in the mgen binary wire format
	 */
	public static final byte TAG_STRING = (byte) 7;

	/**
	 * Binary type tag used for lists and arrays in the mgen binary wire format
	 */
	public static final byte TAG_LIST = (byte) 8;

	/**
	 * Binary type tag used for maps in the mgen binary wire format
	 */
	public static final byte TAG_MAP = (byte) 9;

	/**
	 * Binary type tag used for MGen objects in the mgen binary wire format
	 */
	public static final byte TAG_CLASS = (byte) 10;

}
