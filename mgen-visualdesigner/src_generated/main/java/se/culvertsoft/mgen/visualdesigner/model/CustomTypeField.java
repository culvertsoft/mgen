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

public class CustomTypeField extends Entity {

    private FieldType m_type;
    private java.util.ArrayList<String> m_flags;
    private boolean _m_type_isSet;
    private boolean _m_flags_isSet;

    public CustomTypeField() {
        super();
        m_type = null;
        m_flags = null;
        _m_type_isSet = false;
        _m_flags_isSet = false;
    }

    public CustomTypeField(final FieldType type) {
        m_type = type;
        _m_type_isSet = true;
        _m_flags_isSet = false;
    }

    public CustomTypeField(final EntityIdBase id,
                final String name,
                final EntityIdBase parent,
                final FieldType type,
                final java.util.ArrayList<String> flags) {
        super(id, name, parent);
        m_type = type;
        m_flags = flags;
        _m_type_isSet = true;
        _m_flags_isSet = true;
    }

    public FieldType getType() {
        return m_type;
    }

    public java.util.ArrayList<String> getFlags() {
        return m_flags;
    }

    public FieldType getTypeMutable() {
        _m_type_isSet = true;
        return m_type;
    }

    public java.util.ArrayList<String> getFlagsMutable() {
        _m_flags_isSet = true;
        return m_flags;
    }

    public boolean hasType() {
        return _isTypeSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasFlags() {
        return _isFlagsSet(FieldSetDepth.SHALLOW);
    }

    public CustomTypeField unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeField unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeField unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeField unsetType() {
        _setTypeSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeField unsetFlags() {
        _setFlagsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomTypeField setType(final FieldType type) {
        m_type = type;
        _m_type_isSet = true;
        return this;
    }

    public CustomTypeField setFlags(final java.util.ArrayList<String> flags) {
        m_flags = flags;
        _m_flags_isSet = true;
        return this;
    }

    public CustomTypeField setId(final EntityIdBase id) {
        super.setId(id);
        return this;
    }

    public CustomTypeField setName(final String name) {
        super.setName(name);
        return this;
    }

    public CustomTypeField setParent(final EntityIdBase parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent()).append("\n");
        sb.append("  ").append("type = ").append(getType()).append("\n");
        sb.append("  ").append("flags = ").append(getFlags());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -132720611;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        result = _isTypeSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getType(), _type_METADATA.typ())) : result;
        result = _isFlagsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getFlags(), _flags_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (CustomTypeField.class != other.getClass()) return false;
        final CustomTypeField o = (CustomTypeField)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && (_isTypeSet(FieldSetDepth.SHALLOW) == o._isTypeSet(FieldSetDepth.SHALLOW))
          && (_isFlagsSet(FieldSetDepth.SHALLOW) == o._isFlagsSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ())
          && EqualityTester.areEqual(getType(), o.getType(), _type_METADATA.typ())
          && EqualityTester.areEqual(getFlags(), o.getFlags(), _flags_METADATA.typ());
    }

    @Override
    public CustomTypeField deepCopy() {
        final CustomTypeField out = new CustomTypeField();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out.setType(DeepCopyer.deepCopy(getType(), _type_METADATA.typ()));
        out.setFlags(DeepCopyer.deepCopy(getFlags(), _flags_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setTypeSet(_isTypeSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setFlagsSet(_isFlagsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getName(), _name_METADATA, _isNameSet(FieldSetDepth.SHALLOW));
        visitor.visit(getParent(), _parent_METADATA, _isParentSet(FieldSetDepth.SHALLOW));
        visitor.visit(getType(), _type_METADATA, _isTypeSet(FieldSetDepth.SHALLOW));
        visitor.visit(getFlags(), _flags_METADATA, _isFlagsSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_id_HASH_16BIT):
                setId((EntityIdBase)reader.readMgenObjectField(field, context));
                return true;
            case (_name_HASH_16BIT):
                setName((String)reader.readStringField(field, context));
                return true;
            case (_parent_HASH_16BIT):
                setParent((EntityIdBase)reader.readMgenObjectField(field, context));
                return true;
            case (_type_HASH_16BIT):
                setType((FieldType)reader.readMgenObjectField(field, context));
                return true;
            case (_flags_HASH_16BIT):
                setFlags((java.util.ArrayList<String>)reader.readListField(field, context));
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

    public boolean _isTypeSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_type_isSet;
        } else {
            return _m_type_isSet && Validator.validateFieldDeep(getType(), _type_METADATA.typ());
        }
    }

    public boolean _isFlagsSet(final FieldSetDepth fieldSetDepth) {
        return _m_flags_isSet;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_id_HASH_16BIT):
                return _isIdSet(depth);
            case (_name_HASH_16BIT):
                return _isNameSet(depth);
            case (_parent_HASH_16BIT):
                return _isParentSet(depth);
            case (_type_HASH_16BIT):
                return _isTypeSet(depth);
            case (_flags_HASH_16BIT):
                return _isFlagsSet(depth);
            default:
                return false;
        }
    }

    public CustomTypeField _setTypeSet(final boolean state, final FieldSetDepth depth) {
        _m_type_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getType(), _type_METADATA.typ());
        if (!state)
            m_type = null;
        return this;
    }

    public CustomTypeField _setFlagsSet(final boolean state, final FieldSetDepth depth) {
        _m_flags_isSet = state;
        if (!state)
            m_flags = null;
        return this;
    }

    public CustomTypeField _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        _setTypeSet(state, depth);
        _setFlagsSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isTypeSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && (!_isIdSet(FieldSetDepth.SHALLOW) || _isIdSet(FieldSetDepth.DEEP))
                && (!_isParentSet(FieldSetDepth.SHALLOW) || _isParentSet(FieldSetDepth.DEEP))
                && _isTypeSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
        out += _isTypeSet(fieldSetDepth) ? 1 : 0;
        out += _isFlagsSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_id_HASH_16BIT):
                return _id_METADATA;
            case (_name_HASH_16BIT):
                return _name_METADATA;
            case (_parent_HASH_16BIT):
                return _parent_METADATA;
            case (_type_HASH_16BIT):
                return _type_METADATA;
            case (_flags_HASH_16BIT):
                return _flags_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_id_HASH_32BIT):
                return _id_METADATA;
            case (_name_HASH_32BIT):
                return _name_METADATA;
            case (_parent_HASH_32BIT):
                return _parent_METADATA;
            case (_type_HASH_32BIT):
                return _type_METADATA;
            case (_flags_HASH_32BIT):
                return _flags_METADATA;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _type_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField", "type", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.FieldType"), java.util.Arrays.asList("required"));
    public static final Field _flags_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField", "flags", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(se.culvertsoft.mgen.api.model.StringType.INSTANCE), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;
    public static final short _type_HASH_16BIT = 32391;
    public static final short _flags_HASH_16BIT = -23641;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;
    public static final int _type_HASH_32BIT = -1931585751;
    public static final int _flags_HASH_32BIT = 184893882;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.CustomTypeField";
    public static final short _TYPE_HASH_16BIT = -25806;
    public static final int _TYPE_HASH_32BIT = -594078854;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_id_METADATA);
        fields.add(_name_METADATA);
        fields.add(_parent_METADATA);
        fields.add(_type_METADATA);
        fields.add(_flags_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[2];
        _TYPE_HASHES_32BIT = new int[2];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 14592;
        _TYPE_HASHES_32BIT[0] = 1798311022;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Entity");
        base6416bit.add("OQA");
        base6432bit.add("azAMbg");
        _TYPE_HASHES_16BIT[1] = -25806;
        _TYPE_HASHES_32BIT[1] = -594078854;
        names.add("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField");
        base6416bit.add("mzI");
        base6432bit.add("3JcTeg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
