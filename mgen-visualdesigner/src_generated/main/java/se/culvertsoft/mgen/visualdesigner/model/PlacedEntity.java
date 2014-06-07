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

public class PlacedEntity extends Entity {

    private Placement m_placement;
    private boolean _m_placement_isSet;

    public PlacedEntity() {
        super();
        m_placement = null;
        _m_placement_isSet = false;
    }

    public PlacedEntity(final Placement placement) {
        m_placement = placement;
        _m_placement_isSet = true;
    }

    public PlacedEntity(final EntityIdBase id,
                final String name,
                final EntityIdBase parent,
                final Placement placement) {
        super(id, name, parent);
        m_placement = placement;
        _m_placement_isSet = true;
    }

    public Placement getPlacement() {
        return m_placement;
    }

    public Placement getPlacementMutable() {
        _m_placement_isSet = true;
        return m_placement;
    }

    public boolean hasPlacement() {
        return _isPlacementSet(FieldSetDepth.SHALLOW);
    }

    public PlacedEntity unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public PlacedEntity unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public PlacedEntity unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public PlacedEntity unsetPlacement() {
        _setPlacementSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public PlacedEntity setPlacement(final Placement placement) {
        m_placement = placement;
        _m_placement_isSet = true;
        return this;
    }

    public PlacedEntity setId(final EntityIdBase id) {
        super.setId(id);
        return this;
    }

    public PlacedEntity setName(final String name) {
        super.setName(name);
        return this;
    }

    public PlacedEntity setParent(final EntityIdBase parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent()).append("\n");
        sb.append("  ").append("placement = ").append(getPlacement());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 136906386;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        result = _isPlacementSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getPlacement(), _placement_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (PlacedEntity.class != other.getClass()) return false;
        final PlacedEntity o = (PlacedEntity)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && (_isPlacementSet(FieldSetDepth.SHALLOW) == o._isPlacementSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ())
          && EqualityTester.areEqual(getPlacement(), o.getPlacement(), _placement_METADATA.typ());
    }

    @Override
    public PlacedEntity deepCopy() {
        final PlacedEntity out = new PlacedEntity();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out.setPlacement(DeepCopyer.deepCopy(getPlacement(), _placement_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setPlacementSet(_isPlacementSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
            case (_placement_HASH_16BIT):
                setPlacement((Placement)reader.readMgenObjectField(field, context));
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

    public boolean _isPlacementSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_placement_isSet;
        } else {
            return _m_placement_isSet && Validator.validateFieldDeep(getPlacement(), _placement_METADATA.typ());
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
            default:
                return false;
        }
    }

    public PlacedEntity _setPlacementSet(final boolean state, final FieldSetDepth depth) {
        _m_placement_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getPlacement(), _placement_METADATA.typ());
        if (!state)
            m_placement = null;
        return this;
    }

    public PlacedEntity _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        _setPlacementSet(state, depth);
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
                && _isPlacementSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
        out += _isPlacementSet(fieldSetDepth) ? 1 : 0;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _placement_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity", "placement", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Placement"), java.util.Arrays.asList("required"));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;
    public static final short _placement_HASH_16BIT = -30377;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;
    public static final int _placement_HASH_32BIT = 1222341902;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.PlacedEntity";
    public static final short _TYPE_HASH_16BIT = -18052;
    public static final int _TYPE_HASH_32BIT = 1682256305;

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
        _TYPE_HASHES_16BIT[1] = -18052;
        _TYPE_HASHES_32BIT[1] = 1682256305;
        names.add("se.culvertsoft.mgen.visualdesigner.model.PlacedEntity");
        base6416bit.add("uXw");
        base6432bit.add("ZEUxsQ");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
