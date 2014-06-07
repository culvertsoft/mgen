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

public class CustomTypeRef extends FieldType {

    private EntityIdBase m_id;
    private boolean _m_id_isSet;

    public CustomTypeRef() {
        super();
        m_id = null;
        _m_id_isSet = false;
    }

    public CustomTypeRef(final EntityIdBase id) {
        m_id = id;
        _m_id_isSet = true;
    }

    public EntityIdBase getId() {
        return m_id;
    }

    public EntityIdBase getIdMutable() {
        _m_id_isSet = true;
        return m_id;
    }

    public boolean hasId() {
        return _isIdSet(FieldSetDepth.SHALLOW);
    }

    public CustomTypeRef unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeRef setId(final EntityIdBase id) {
        m_id = id;
        _m_id_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef:\n");
        sb.append("  ").append("id = ").append(getId());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1948474486;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (CustomTypeRef.class != other.getClass()) return false;
        final CustomTypeRef o = (CustomTypeRef)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ());
    }

    @Override
    public CustomTypeRef deepCopy() {
        final CustomTypeRef out = new CustomTypeRef();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getId(), _id_METADATA, _isIdSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_id_HASH_16BIT):
                setId((EntityIdBase)reader.readMgenObjectField(field, context));
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

    public boolean _isIdSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_id_isSet;
        } else {
            return _m_id_isSet && Validator.validateFieldDeep(getId(), _id_METADATA.typ());
        }
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_id_HASH_16BIT):
                return _isIdSet(depth);
            default:
                return false;
        }
    }

    public CustomTypeRef _setIdSet(final boolean state, final FieldSetDepth depth) {
        _m_id_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getId(), _id_METADATA.typ());
        if (!state)
            m_id = null;
        return this;
    }

    public CustomTypeRef _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true
                && (!_isIdSet(FieldSetDepth.SHALLOW) || _isIdSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_id_HASH_16BIT):
                return _id_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_id_HASH_32BIT):
                return _id_METADATA;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;

    public static final int _id_HASH_32BIT = -1086757040;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef";
    public static final short _TYPE_HASH_16BIT = -27922;
    public static final int _TYPE_HASH_32BIT = 1371495858;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_id_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[2];
        _TYPE_HASHES_32BIT = new int[2];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 1314;
        _TYPE_HASHES_32BIT[0] = -298374023;
        names.add("se.culvertsoft.mgen.visualdesigner.model.FieldType");
        base6416bit.add("BSI");
        base6432bit.add("7jcseQ");
        _TYPE_HASHES_16BIT[1] = -27922;
        _TYPE_HASHES_32BIT[1] = 1371495858;
        names.add("se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef");
        base6416bit.add("ku4");
        base6432bit.add("Ub9dsg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
