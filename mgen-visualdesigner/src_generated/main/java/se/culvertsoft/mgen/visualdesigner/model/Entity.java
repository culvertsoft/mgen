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

public class Entity extends se.culvertsoft.mgen.javapack.classes.MGenBase {

    private EntityIdBase m_id;
    private String m_name;
    private EntityIdBase m_parent;
    private boolean _m_id_isSet;
    private boolean _m_name_isSet;
    private boolean _m_parent_isSet;

    public Entity() {
        super();
        m_id = null;
        m_name = null;
        m_parent = null;
        _m_id_isSet = false;
        _m_name_isSet = false;
        _m_parent_isSet = false;
    }

    public Entity(final EntityIdBase id,
                final String name,
                final EntityIdBase parent) {
        m_id = id;
        m_name = name;
        m_parent = parent;
        _m_id_isSet = true;
        _m_name_isSet = true;
        _m_parent_isSet = true;
    }

    public EntityIdBase getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public EntityIdBase getParent() {
        return m_parent;
    }

    public EntityIdBase getIdMutable() {
        _m_id_isSet = true;
        return m_id;
    }

    public EntityIdBase getParentMutable() {
        _m_parent_isSet = true;
        return m_parent;
    }

    public boolean hasId() {
        return _isIdSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasName() {
        return _isNameSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasParent() {
        return _isParentSet(FieldSetDepth.SHALLOW);
    }

    public Entity unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Entity unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Entity unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Entity setId(final EntityIdBase id) {
        m_id = id;
        _m_id_isSet = true;
        return this;
    }

    public Entity setName(final String name) {
        m_name = name;
        _m_name_isSet = true;
        return this;
    }

    public Entity setParent(final EntityIdBase parent) {
        m_parent = parent;
        _m_parent_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.Entity:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -975591051;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (Entity.class != other.getClass()) return false;
        final Entity o = (Entity)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ());
    }

    @Override
    public Entity deepCopy() {
        final Entity out = new Entity();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
            case (_name_HASH_16BIT):
                setName((String)reader.readStringField(field, context));
                return true;
            case (_parent_HASH_16BIT):
                setParent((EntityIdBase)reader.readMgenObjectField(field, context));
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

    public boolean _isNameSet(final FieldSetDepth fieldSetDepth) {
        return _m_name_isSet;
    }

    public boolean _isParentSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_parent_isSet;
        } else {
            return _m_parent_isSet && Validator.validateFieldDeep(getParent(), _parent_METADATA.typ());
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
            default:
                return false;
        }
    }

    public Entity _setIdSet(final boolean state, final FieldSetDepth depth) {
        _m_id_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getId(), _id_METADATA.typ());
        if (!state)
            m_id = null;
        return this;
    }

    public Entity _setNameSet(final boolean state, final FieldSetDepth depth) {
        _m_name_isSet = state;
        if (!state)
            m_name = null;
        return this;
    }

    public Entity _setParentSet(final boolean state, final FieldSetDepth depth) {
        _m_parent_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getParent(), _parent_METADATA.typ());
        if (!state)
            m_parent = null;
        return this;
    }

    public Entity _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true
                && (!_isIdSet(FieldSetDepth.SHALLOW) || _isIdSet(FieldSetDepth.DEEP))
                && (!_isParentSet(FieldSetDepth.SHALLOW) || _isParentSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Entity", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Entity", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Entity", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.Entity";
    public static final short _TYPE_HASH_16BIT = 14592;
    public static final int _TYPE_HASH_32BIT = 1798311022;

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
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[1];
        _TYPE_HASHES_32BIT = new int[1];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 14592;
        _TYPE_HASHES_32BIT[0] = 1798311022;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Entity");
        base6416bit.add("OQA");
        base6432bit.add("azAMbg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
