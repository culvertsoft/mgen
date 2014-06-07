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

public class CustomType extends PlacedEntity {

    private EntityIdBase m_superType;
    private java.util.ArrayList<EntityIdBase> m_subTypes;
    private java.util.ArrayList<CustomTypeField> m_fields;
    private boolean _m_superType_isSet;
    private boolean _m_subTypes_isSet;
    private boolean _m_fields_isSet;

    public CustomType() {
        super();
        m_superType = null;
        m_subTypes = null;
        m_fields = null;
        _m_superType_isSet = false;
        _m_subTypes_isSet = false;
        _m_fields_isSet = false;
    }

    public CustomType(final Placement placement) {
        super(placement);
        _m_superType_isSet = false;
        _m_subTypes_isSet = false;
        _m_fields_isSet = false;
    }

    public CustomType(final EntityIdBase id,
                final String name,
                final EntityIdBase parent,
                final Placement placement,
                final EntityIdBase superType,
                final java.util.ArrayList<EntityIdBase> subTypes,
                final java.util.ArrayList<CustomTypeField> fields) {
        super(id, name, parent, placement);
        m_superType = superType;
        m_subTypes = subTypes;
        m_fields = fields;
        _m_superType_isSet = true;
        _m_subTypes_isSet = true;
        _m_fields_isSet = true;
    }

    public EntityIdBase getSuperType() {
        return m_superType;
    }

    public java.util.ArrayList<EntityIdBase> getSubTypes() {
        return m_subTypes;
    }

    public java.util.ArrayList<CustomTypeField> getFields() {
        return m_fields;
    }

    public EntityIdBase getSuperTypeMutable() {
        _m_superType_isSet = true;
        return m_superType;
    }

    public java.util.ArrayList<EntityIdBase> getSubTypesMutable() {
        _m_subTypes_isSet = true;
        return m_subTypes;
    }

    public java.util.ArrayList<CustomTypeField> getFieldsMutable() {
        _m_fields_isSet = true;
        return m_fields;
    }

    public boolean hasSuperType() {
        return _isSuperTypeSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasSubTypes() {
        return _isSubTypesSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasFields() {
        return _isFieldsSet(FieldSetDepth.SHALLOW);
    }

    public CustomType unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetPlacement() {
        _setPlacementSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetSuperType() {
        _setSuperTypeSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetSubTypes() {
        _setSubTypesSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType unsetFields() {
        _setFieldsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public CustomType setSuperType(final EntityIdBase superType) {
        m_superType = superType;
        _m_superType_isSet = true;
        return this;
    }

    public CustomType setSubTypes(final java.util.ArrayList<EntityIdBase> subTypes) {
        m_subTypes = subTypes;
        _m_subTypes_isSet = true;
        return this;
    }

    public CustomType setFields(final java.util.ArrayList<CustomTypeField> fields) {
        m_fields = fields;
        _m_fields_isSet = true;
        return this;
    }

    public CustomType setId(final EntityIdBase id) {
        super.setId(id);
        return this;
    }

    public CustomType setName(final String name) {
        super.setName(name);
        return this;
    }

    public CustomType setParent(final EntityIdBase parent) {
        super.setParent(parent);
        return this;
    }

    public CustomType setPlacement(final Placement placement) {
        super.setPlacement(placement);
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.CustomType:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent()).append("\n");
        sb.append("  ").append("placement = ").append(getPlacement()).append("\n");
        sb.append("  ").append("superType = ").append(getSuperType()).append("\n");
        sb.append("  ").append("subTypes = ").append(getSubTypes()).append("\n");
        sb.append("  ").append("fields = ").append(getFields());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 2140412637;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        result = _isPlacementSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getPlacement(), _placement_METADATA.typ())) : result;
        result = _isSuperTypeSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSuperType(), _superType_METADATA.typ())) : result;
        result = _isSubTypesSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSubTypes(), _subTypes_METADATA.typ())) : result;
        result = _isFieldsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getFields(), _fields_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (CustomType.class != other.getClass()) return false;
        final CustomType o = (CustomType)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && (_isPlacementSet(FieldSetDepth.SHALLOW) == o._isPlacementSet(FieldSetDepth.SHALLOW))
          && (_isSuperTypeSet(FieldSetDepth.SHALLOW) == o._isSuperTypeSet(FieldSetDepth.SHALLOW))
          && (_isSubTypesSet(FieldSetDepth.SHALLOW) == o._isSubTypesSet(FieldSetDepth.SHALLOW))
          && (_isFieldsSet(FieldSetDepth.SHALLOW) == o._isFieldsSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ())
          && EqualityTester.areEqual(getPlacement(), o.getPlacement(), _placement_METADATA.typ())
          && EqualityTester.areEqual(getSuperType(), o.getSuperType(), _superType_METADATA.typ())
          && EqualityTester.areEqual(getSubTypes(), o.getSubTypes(), _subTypes_METADATA.typ())
          && EqualityTester.areEqual(getFields(), o.getFields(), _fields_METADATA.typ());
    }

    @Override
    public CustomType deepCopy() {
        final CustomType out = new CustomType();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out.setPlacement(DeepCopyer.deepCopy(getPlacement(), _placement_METADATA.typ()));
        out.setSuperType(DeepCopyer.deepCopy(getSuperType(), _superType_METADATA.typ()));
        out.setSubTypes(DeepCopyer.deepCopy(getSubTypes(), _subTypes_METADATA.typ()));
        out.setFields(DeepCopyer.deepCopy(getFields(), _fields_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setPlacementSet(_isPlacementSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSuperTypeSet(_isSuperTypeSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSubTypesSet(_isSubTypesSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setFieldsSet(_isFieldsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getPlacement(), _placement_METADATA, _isPlacementSet(FieldSetDepth.SHALLOW));
        visitor.visit(getSuperType(), _superType_METADATA, _isSuperTypeSet(FieldSetDepth.SHALLOW));
        visitor.visit(getSubTypes(), _subTypes_METADATA, _isSubTypesSet(FieldSetDepth.SHALLOW));
        visitor.visit(getFields(), _fields_METADATA, _isFieldsSet(FieldSetDepth.SHALLOW));
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
            case (_placement_HASH_16BIT):
                setPlacement((Placement)reader.readMgenObjectField(field, context));
                return true;
            case (_superType_HASH_16BIT):
                setSuperType((EntityIdBase)reader.readMgenObjectField(field, context));
                return true;
            case (_subTypes_HASH_16BIT):
                setSubTypes((java.util.ArrayList<EntityIdBase>)reader.readListField(field, context));
                return true;
            case (_fields_HASH_16BIT):
                setFields((java.util.ArrayList<CustomTypeField>)reader.readListField(field, context));
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

    public boolean _isSuperTypeSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_superType_isSet;
        } else {
            return _m_superType_isSet && Validator.validateFieldDeep(getSuperType(), _superType_METADATA.typ());
        }
    }

    public boolean _isSubTypesSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_subTypes_isSet;
        } else {
            return _m_subTypes_isSet && Validator.validateFieldDeep(getSubTypes(), _subTypes_METADATA.typ());
        }
    }

    public boolean _isFieldsSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_fields_isSet;
        } else {
            return _m_fields_isSet && Validator.validateFieldDeep(getFields(), _fields_METADATA.typ());
        }
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_id_HASH_16BIT):
                return _isIdSet(depth);
            case (_name_HASH_16BIT):
                return _isNameSet(depth);
            case (_parent_HASH_16BIT):
                return _isParentSet(depth);
            case (_placement_HASH_16BIT):
                return _isPlacementSet(depth);
            case (_superType_HASH_16BIT):
                return _isSuperTypeSet(depth);
            case (_subTypes_HASH_16BIT):
                return _isSubTypesSet(depth);
            case (_fields_HASH_16BIT):
                return _isFieldsSet(depth);
            default:
                return false;
        }
    }

    public CustomType _setSuperTypeSet(final boolean state, final FieldSetDepth depth) {
        _m_superType_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getSuperType(), _superType_METADATA.typ());
        if (!state)
            m_superType = null;
        return this;
    }

    public CustomType _setSubTypesSet(final boolean state, final FieldSetDepth depth) {
        _m_subTypes_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getSubTypes(), _subTypes_METADATA.typ());
        if (!state)
            m_subTypes = null;
        return this;
    }

    public CustomType _setFieldsSet(final boolean state, final FieldSetDepth depth) {
        _m_fields_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getFields(), _fields_METADATA.typ());
        if (!state)
            m_fields = null;
        return this;
    }

    public CustomType _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        _setPlacementSet(state, depth);
        _setSuperTypeSet(state, depth);
        _setSubTypesSet(state, depth);
        _setFieldsSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isPlacementSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && (!_isIdSet(FieldSetDepth.SHALLOW) || _isIdSet(FieldSetDepth.DEEP))
                && (!_isParentSet(FieldSetDepth.SHALLOW) || _isParentSet(FieldSetDepth.DEEP))
                && _isPlacementSet(FieldSetDepth.DEEP)
                && (!_isSuperTypeSet(FieldSetDepth.SHALLOW) || _isSuperTypeSet(FieldSetDepth.DEEP))
                && (!_isSubTypesSet(FieldSetDepth.SHALLOW) || _isSubTypesSet(FieldSetDepth.DEEP))
                && (!_isFieldsSet(FieldSetDepth.SHALLOW) || _isFieldsSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
        out += _isPlacementSet(fieldSetDepth) ? 1 : 0;
        out += _isSuperTypeSet(fieldSetDepth) ? 1 : 0;
        out += _isSubTypesSet(fieldSetDepth) ? 1 : 0;
        out += _isFieldsSet(fieldSetDepth) ? 1 : 0;
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
            case (_placement_HASH_16BIT):
                return _placement_METADATA;
            case (_superType_HASH_16BIT):
                return _superType_METADATA;
            case (_subTypes_HASH_16BIT):
                return _subTypes_METADATA;
            case (_fields_HASH_16BIT):
                return _fields_METADATA;
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
            case (_placement_HASH_32BIT):
                return _placement_METADATA;
            case (_superType_HASH_32BIT):
                return _superType_METADATA;
            case (_subTypes_HASH_32BIT):
                return _subTypes_METADATA;
            case (_fields_HASH_32BIT):
                return _fields_METADATA;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _placement_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "placement", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Placement"), java.util.Arrays.asList("required"));
    public static final Field _superType_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "superType", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _subTypes_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "subTypes", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase")), java.util.Arrays.asList(""));
    public static final Field _fields_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.CustomType", "fields", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.CustomTypeField")), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;
    public static final short _placement_HASH_16BIT = -30377;
    public static final short _superType_HASH_16BIT = 27179;
    public static final short _subTypes_HASH_16BIT = 12236;
    public static final short _fields_HASH_16BIT = -4146;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;
    public static final int _placement_HASH_32BIT = 1222341902;
    public static final int _superType_HASH_32BIT = -243487188;
    public static final int _subTypes_HASH_32BIT = -498972178;
    public static final int _fields_HASH_32BIT = 2128995208;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.CustomType";
    public static final short _TYPE_HASH_16BIT = 30846;
    public static final int _TYPE_HASH_32BIT = 673158278;

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
        fields.add(_placement_METADATA);
        fields.add(_superType_METADATA);
        fields.add(_subTypes_METADATA);
        fields.add(_fields_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[3];
        _TYPE_HASHES_32BIT = new int[3];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 14592;
        _TYPE_HASHES_32BIT[0] = 1798311022;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Entity");
        base6416bit.add("OQA");
        base6432bit.add("azAMbg");
        _TYPE_HASHES_16BIT[1] = -18052;
        _TYPE_HASHES_32BIT[1] = 1682256305;
        names.add("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity");
        base6416bit.add("uXw");
        base6432bit.add("ZEUxsQ");
        _TYPE_HASHES_16BIT[2] = 30846;
        _TYPE_HASHES_32BIT[2] = 673158278;
        names.add("se.culvertsoft.mgen.visualdesigner.model.CustomType");
        base6416bit.add("eH4");
        base6432bit.add("KB+Uhg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
