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
import se.culvertsoft.mgen.javapack.util.Validator;
import se.culvertsoft.mgen.javapack.util.Marker;

public class MapType extends GenericType {

    private SimpleType m_keyType;
    private FieldType m_valueType;
    private boolean _m_keyType_isSet;
    private boolean _m_valueType_isSet;

    public MapType() {
        super();
        m_keyType = null;
        m_valueType = null;
        _m_keyType_isSet = false;
        _m_valueType_isSet = false;
    }

    public MapType(final SimpleType keyType,
                final FieldType valueType) {
        m_keyType = keyType;
        m_valueType = valueType;
        _m_keyType_isSet = true;
        _m_valueType_isSet = true;
    }

    public SimpleType getKeyType() {
        return m_keyType;
    }

    public FieldType getValueType() {
        return m_valueType;
    }

    public SimpleType getKeyTypeMutable() {
        _m_keyType_isSet = true;
        return m_keyType;
    }

    public FieldType getValueTypeMutable() {
        _m_valueType_isSet = true;
        return m_valueType;
    }

    public boolean hasKeyType() {
        return _isKeyTypeSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasValueType() {
        return _isValueTypeSet(FieldSetDepth.SHALLOW);
    }

    public MapType unsetKeyType() {
        _setKeyTypeSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public MapType unsetValueType() {
        _setValueTypeSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public MapType setKeyType(final SimpleType keyType) {
        m_keyType = keyType;
        _m_keyType_isSet = true;
        return this;
    }

    public MapType setValueType(final FieldType valueType) {
        m_valueType = valueType;
        _m_valueType_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.MapType:\n");
        sb.append("  ").append("keyType = ").append(getKeyType()).append("\n");
        sb.append("  ").append("valueType = ").append(getValueType());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -2044950684;
        result = _isKeyTypeSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getKeyType(), _keyType_METADATA.typ())) : result;
        result = _isValueTypeSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getValueType(), _valueType_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (MapType.class != other.getClass()) return false;
        final MapType o = (MapType)other;
        return true
          && (_isKeyTypeSet(FieldSetDepth.SHALLOW) == o._isKeyTypeSet(FieldSetDepth.SHALLOW))
          && (_isValueTypeSet(FieldSetDepth.SHALLOW) == o._isValueTypeSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getKeyType(), o.getKeyType(), _keyType_METADATA.typ())
          && EqualityTester.areEqual(getValueType(), o.getValueType(), _valueType_METADATA.typ());
    }

    @Override
    public MapType deepCopy() {
        final MapType out = new MapType();
        out.setKeyType(DeepCopyer.deepCopy(getKeyType(), _keyType_METADATA.typ()));
        out.setValueType(DeepCopyer.deepCopy(getValueType(), _valueType_METADATA.typ()));
        out._setKeyTypeSet(_isKeyTypeSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setValueTypeSet(_isValueTypeSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getKeyType(), _keyType_METADATA, _isKeyTypeSet(FieldSetDepth.SHALLOW));
        visitor.visit(getValueType(), _valueType_METADATA, _isValueTypeSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_keyType_HASH_16BIT):
                setKeyType((SimpleType)reader.readMgenObjectField(field, context));
                return true;
            case (_valueType_HASH_16BIT):
                setValueType((FieldType)reader.readMgenObjectField(field, context));
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

    public boolean _isKeyTypeSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_keyType_isSet;
        } else {
            return _m_keyType_isSet && Validator.validateFieldDeep(getKeyType(), _keyType_METADATA.typ());
        }
    }

    public boolean _isValueTypeSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_valueType_isSet;
        } else {
            return _m_valueType_isSet && Validator.validateFieldDeep(getValueType(), _valueType_METADATA.typ());
        }
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_keyType_HASH_16BIT):
                return _isKeyTypeSet(depth);
            case (_valueType_HASH_16BIT):
                return _isValueTypeSet(depth);
            default:
                return false;
        }
    }

    public MapType _setKeyTypeSet(final boolean state, final FieldSetDepth depth) {
        _m_keyType_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getKeyType(), _keyType_METADATA.typ());
        if (!state)
            m_keyType = null;
        return this;
    }

    public MapType _setValueTypeSet(final boolean state, final FieldSetDepth depth) {
        _m_valueType_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getValueType(), _valueType_METADATA.typ());
        if (!state)
            m_valueType = null;
        return this;
    }

    public MapType _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setKeyTypeSet(state, depth);
        _setValueTypeSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true
                && (!_isKeyTypeSet(FieldSetDepth.SHALLOW) || _isKeyTypeSet(FieldSetDepth.DEEP))
                && (!_isValueTypeSet(FieldSetDepth.SHALLOW) || _isValueTypeSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isKeyTypeSet(fieldSetDepth) ? 1 : 0;
        out += _isValueTypeSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_keyType_HASH_16BIT):
                return _keyType_METADATA;
            case (_valueType_HASH_16BIT):
                return _valueType_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_keyType_HASH_32BIT):
                return _keyType_METADATA;
            case (_valueType_HASH_32BIT):
                return _valueType_METADATA;
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
		  

    public static final Field _keyType_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.MapType", "keyType", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.SimpleType"), java.util.Arrays.asList("polymorphic"));
    public static final Field _valueType_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.MapType", "valueType", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.FieldType"), java.util.Arrays.asList("polymorphic"));

    public static final short _keyType_HASH_16BIT = 27921;
    public static final short _valueType_HASH_16BIT = 13092;

    public static final int _keyType_HASH_32BIT = -1248920515;
    public static final int _valueType_HASH_32BIT = 846359297;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.MapType";
    public static final short _TYPE_HASH_16BIT = -22153;
    public static final int _TYPE_HASH_32BIT = -1445374947;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_keyType_METADATA);
        fields.add(_valueType_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[4];
        _TYPE_HASHES_32BIT = new int[4];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 1314;
        _TYPE_HASHES_32BIT[0] = -298374023;
        names.add("se.culvertsoft.mgen.visualdesigner.model.FieldType");
        base6416bit.add("BSI");
        base6432bit.add("7jcseQ");
        _TYPE_HASHES_16BIT[1] = -27713;
        _TYPE_HASHES_32BIT[1] = -1000408287;
        names.add("se.culvertsoft.mgen.visualdesigner.model.BuiltInType");
        base6416bit.add("k78");
        base6432bit.add("xF77IQ");
        _TYPE_HASHES_16BIT[2] = -11310;
        _TYPE_HASHES_32BIT[2] = 1969575124;
        names.add("se.culvertsoft.mgen.visualdesigner.model.GenericType");
        base6416bit.add("09I");
        base6432bit.add("dWVU1A");
        _TYPE_HASHES_16BIT[3] = -22153;
        _TYPE_HASHES_32BIT[3] = -1445374947;
        names.add("se.culvertsoft.mgen.visualdesigner.model.MapType");
        base6416bit.add("qXc");
        base6432bit.add("qdlUHQ");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
