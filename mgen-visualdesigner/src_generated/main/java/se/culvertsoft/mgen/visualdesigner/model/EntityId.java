/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                               GENERATED WITH MGEN                                    *****
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/
package se.culvertsoft.mgen.visualdesigner.model;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;
import se.culvertsoft.mgen.javapack.serialization.FieldVisitor;
import se.culvertsoft.mgen.javapack.serialization.Reader;
import se.culvertsoft.mgen.javapack.util.EqualityTester;
import se.culvertsoft.mgen.javapack.util.DeepCopyer;
import se.culvertsoft.mgen.javapack.util.FieldHasher;

public class EntityId extends EntityIdBase {

    private long m_lsb;
    private long m_msb;
    private boolean _m_lsb_isSet;
    private boolean _m_msb_isSet;

    public EntityId() {
        super();
        m_lsb = 0L;
        m_msb = 0L;
        _m_lsb_isSet = false;
        _m_msb_isSet = false;
    }

    public EntityId(final long lsb,
                final long msb) {
        m_lsb = lsb;
        m_msb = msb;
        _m_lsb_isSet = true;
        _m_msb_isSet = true;
    }

    public long getLsb() {
        return m_lsb;
    }

    public long getMsb() {
        return m_msb;
    }

    public boolean hasLsb() {
        return _isLsbSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasMsb() {
        return _isMsbSet(FieldSetDepth.SHALLOW);
    }

    public EntityId unsetLsb() {
        _setLsbSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public EntityId unsetMsb() {
        _setMsbSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public EntityId setLsb(final long lsb) {
        m_lsb = lsb;
        _m_lsb_isSet = true;
        return this;
    }

    public EntityId setMsb(final long msb) {
        m_msb = msb;
        _m_msb_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.EntityId:\n");
        sb.append("  ").append("lsb = ").append(getLsb()).append("\n");
        sb.append("  ").append("msb = ").append(getMsb());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -1240127120;
        result = _isLsbSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getLsb(), _lsb_METADATA.typ())) : result;
        result = _isMsbSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getMsb(), _msb_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (EntityId.class != other.getClass()) return false;
        final EntityId o = (EntityId)other;
        return true
          && (_isLsbSet(FieldSetDepth.SHALLOW) == o._isLsbSet(FieldSetDepth.SHALLOW))
          && (_isMsbSet(FieldSetDepth.SHALLOW) == o._isMsbSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getLsb(), o.getLsb(), _lsb_METADATA.typ())
          && EqualityTester.areEqual(getMsb(), o.getMsb(), _msb_METADATA.typ());
    }

    @Override
    public EntityId deepCopy() {
        final EntityId out = new EntityId();
        out.setLsb(DeepCopyer.deepCopy(getLsb(), _lsb_METADATA.typ()));
        out.setMsb(DeepCopyer.deepCopy(getMsb(), _msb_METADATA.typ()));
        out._setLsbSet(_isLsbSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setMsbSet(_isMsbSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        return out;
    }


							
/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                    TYPE METADATA ACCESS AND SERIALIZATION METHODS                    *****
           *****          (accessed primarily by (de-)serializers and for ORM functionality)          *****	
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/	 		  
		  

    @Override
    public String _typeName() {
        return _TYPE_NAME;
    }

    @Override
    public short _typeHash16bit() {
        return _TYPE_HASH_16BIT;
    }

    @Override
    public int _typeHash32bit() {
        return _TYPE_HASH_32BIT;
    }

    @Override
    public void _accept(final FieldVisitor visitor) throws java.io.IOException {
        visitor.beginVisit(this, _nFieldsSet(FieldSetDepth.SHALLOW));
        visitor.visit(getLsb(), _lsb_METADATA, _isLsbSet(FieldSetDepth.SHALLOW));
        visitor.visit(getMsb(), _msb_METADATA, _isMsbSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_lsb_HASH_16BIT):
                setLsb((long)reader.readInt64Field(field, context));
                return true;
            case (_msb_HASH_16BIT):
                setMsb((long)reader.readInt64Field(field, context));
                return true;
            default:
                reader.handleUnknownField(field, context);
                return false;
        }
    }

    @Override
    public java.util.Collection<Field> _fields() {
        return FIELDS;
    }

    public boolean _isLsbSet(final FieldSetDepth fieldSetDepth) {
        return _m_lsb_isSet;
    }

    public boolean _isMsbSet(final FieldSetDepth fieldSetDepth) {
        return _m_msb_isSet;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_lsb_HASH_16BIT):
                return _isLsbSet(depth);
            case (_msb_HASH_16BIT):
                return _isMsbSet(depth);
            default:
                return false;
        }
    }

    public EntityId _setLsbSet(final boolean state, final FieldSetDepth depth) {
        _m_lsb_isSet = state;
        if (!state)
            m_lsb = 0L;
        return this;
    }

    public EntityId _setMsbSet(final boolean state, final FieldSetDepth depth) {
        _m_msb_isSet = state;
        if (!state)
            m_msb = 0L;
        return this;
    }

    public EntityId _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setLsbSet(state, depth);
        _setMsbSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isLsbSet(FieldSetDepth.SHALLOW)
                && _isMsbSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && _isLsbSet(FieldSetDepth.DEEP)
                && _isMsbSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isLsbSet(fieldSetDepth) ? 1 : 0;
        out += _isMsbSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_lsb_HASH_16BIT):
                return _lsb_METADATA;
            case (_msb_HASH_16BIT):
                return _msb_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_lsb_HASH_32BIT):
                return _lsb_METADATA;
            case (_msb_HASH_32BIT):
                return _msb_METADATA;
            default:
                return null;
        }
    }

    @Override
    public short[] _typeHashes16bit() {
        return _TYPE_HASHES_16BIT;
    }

    @Override
    public int[] _typeHashes32bit() {
        return _TYPE_HASHES_32BIT;
    }

    @Override
    public java.util.Collection<String> _typeNames() {
        return _TYPE_NAMES;
    }

    @Override
    public java.util.Collection<String> _typeHashes16bitBase64() {
        return _TYPE_HASHES_16BIT_BASE64;
    }

    @Override
    public java.util.Collection<String> _typeHashes32bitBase64() {
        return _TYPE_HASHES_32BIT_BASE64;
    }


							
/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                                    TYPE METADATA                                     *****
           *****             (generally speaking, it's a bad idea to edit this manually)              *****	
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/	 		  
		  

    public static final Field _lsb_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.EntityId", "lsb", se.culvertsoft.mgen.api.model.Int64Type.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _msb_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.EntityId", "msb", se.culvertsoft.mgen.api.model.Int64Type.INSTANCE, java.util.Arrays.asList("required"));

    public static final short _lsb_HASH_16BIT = 13176;
    public static final short _msb_HASH_16BIT = 1096;

    public static final int _lsb_HASH_32BIT = 421708055;
    public static final int _msb_HASH_32BIT = 417377056;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.EntityId";
    public static final short _TYPE_HASH_16BIT = 9339;
    public static final int _TYPE_HASH_32BIT = -663412032;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_lsb_METADATA);
        fields.add(_msb_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[2];
        _TYPE_HASHES_32BIT = new int[2];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = -25632;
        _TYPE_HASHES_32BIT[0] = -146605438;
        names.add("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase");
        base6416bit.add("m+A");
        base6432bit.add("90L6gg");
        _TYPE_HASHES_16BIT[1] = 9339;
        _TYPE_HASHES_32BIT[1] = -663412032;
        names.add("se.culvertsoft.mgen.visualdesigner.model.EntityId");
        base6416bit.add("JHs");
        base6432bit.add("2HUiwA");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
