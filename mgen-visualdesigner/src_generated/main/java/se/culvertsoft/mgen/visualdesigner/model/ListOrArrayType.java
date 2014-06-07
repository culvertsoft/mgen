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

public class ListOrArrayType extends GenericType {

    private FieldType m_elementType;
    private boolean _m_elementType_isSet;

    public ListOrArrayType() {
        super();
        m_elementType = null;
        _m_elementType_isSet = false;
    }

    public ListOrArrayType(final FieldType elementType) {
        m_elementType = elementType;
        _m_elementType_isSet = true;
    }

    public FieldType getElementType() {
        return m_elementType;
    }

    public FieldType getElementTypeMutable() {
        _m_elementType_isSet = true;
        return m_elementType;
    }

    public boolean hasElementType() {
        return _isElementTypeSet(FieldSetDepth.SHALLOW);
    }

    public ListOrArrayType unsetElementType() {
        _setElementTypeSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public ListOrArrayType setElementType(final FieldType elementType) {
        m_elementType = elementType;
        _m_elementType_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType:\n");
        sb.append("  ").append("elementType = ").append(getElementType());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1086523840;
        result = _isElementTypeSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getElementType(), _elementType_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (ListOrArrayType.class != other.getClass()) return false;
        final ListOrArrayType o = (ListOrArrayType)other;
        return true
          && (_isElementTypeSet(FieldSetDepth.SHALLOW) == o._isElementTypeSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getElementType(), o.getElementType(), _elementType_METADATA.typ());
    }

    @Override
    public ListOrArrayType deepCopy() {
        final ListOrArrayType out = new ListOrArrayType();
        out.setElementType(DeepCopyer.deepCopy(getElementType(), _elementType_METADATA.typ()));
        out._setElementTypeSet(_isElementTypeSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getElementType(), _elementType_METADATA, _isElementTypeSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_elementType_HASH_16BIT):
                setElementType((FieldType)reader.readMgenObjectField(field, context));
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

    public boolean _isElementTypeSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_elementType_isSet;
        } else {
            return _m_elementType_isSet && Validator.validateFieldDeep(getElementType(), _elementType_METADATA.typ());
        }
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_elementType_HASH_16BIT):
                return _isElementTypeSet(depth);
            default:
                return false;
        }
    }

    public ListOrArrayType _setElementTypeSet(final boolean state, final FieldSetDepth depth) {
        _m_elementType_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getElementType(), _elementType_METADATA.typ());
        if (!state)
            m_elementType = null;
        return this;
    }

    public ListOrArrayType _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setElementTypeSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true
                && (!_isElementTypeSet(FieldSetDepth.SHALLOW) || _isElementTypeSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isElementTypeSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_elementType_HASH_16BIT):
                return _elementType_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_elementType_HASH_32BIT):
                return _elementType_METADATA;
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
		  

    public static final Field _elementType_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType", "elementType", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.FieldType"), java.util.Arrays.asList("polymorphic"));

    public static final short _elementType_HASH_16BIT = 1917;

    public static final int _elementType_HASH_32BIT = 911176325;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType";
    public static final short _TYPE_HASH_16BIT = -3042;
    public static final int _TYPE_HASH_32BIT = -2043782985;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_elementType_METADATA);
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
        _TYPE_HASHES_16BIT[3] = -3042;
        _TYPE_HASHES_32BIT[3] = -2043782985;
        names.add("se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType");
        base6416bit.add("9B4");
        base6432bit.add("hi5Ytw");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
