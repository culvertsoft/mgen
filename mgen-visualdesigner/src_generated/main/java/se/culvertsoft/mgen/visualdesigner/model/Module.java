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

public class Module extends PlacedEntity {

    private java.util.HashMap<String, String> m_settings;
    private java.util.ArrayList<Module> m_submodules;
    private java.util.ArrayList<CustomType> m_types;
    private boolean _m_settings_isSet;
    private boolean _m_submodules_isSet;
    private boolean _m_types_isSet;

    public Module() {
        super();
        m_settings = null;
        m_submodules = null;
        m_types = null;
        _m_settings_isSet = false;
        _m_submodules_isSet = false;
        _m_types_isSet = false;
    }

    public Module(final Placement placement) {
        super(placement);
        _m_settings_isSet = false;
        _m_submodules_isSet = false;
        _m_types_isSet = false;
    }

    public Module(final EntityIdBase id,
                final String name,
                final EntityIdBase parent,
                final Placement placement,
                final java.util.HashMap<String, String> settings,
                final java.util.ArrayList<Module> submodules,
                final java.util.ArrayList<CustomType> types) {
        super(id, name, parent, placement);
        m_settings = settings;
        m_submodules = submodules;
        m_types = types;
        _m_settings_isSet = true;
        _m_submodules_isSet = true;
        _m_types_isSet = true;
    }

    public java.util.HashMap<String, String> getSettings() {
        return m_settings;
    }

    public java.util.ArrayList<Module> getSubmodules() {
        return m_submodules;
    }

    public java.util.ArrayList<CustomType> getTypes() {
        return m_types;
    }

    public java.util.HashMap<String, String> getSettingsMutable() {
        _m_settings_isSet = true;
        return m_settings;
    }

    public java.util.ArrayList<Module> getSubmodulesMutable() {
        _m_submodules_isSet = true;
        return m_submodules;
    }

    public java.util.ArrayList<CustomType> getTypesMutable() {
        _m_types_isSet = true;
        return m_types;
    }

    public boolean hasSettings() {
        return _isSettingsSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasSubmodules() {
        return _isSubmodulesSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasTypes() {
        return _isTypesSet(FieldSetDepth.SHALLOW);
    }

    public Module unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetPlacement() {
        _setPlacementSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetSettings() {
        _setSettingsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetSubmodules() {
        _setSubmodulesSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module unsetTypes() {
        _setTypesSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Module setSettings(final java.util.HashMap<String, String> settings) {
        m_settings = settings;
        _m_settings_isSet = true;
        return this;
    }

    public Module setSubmodules(final java.util.ArrayList<Module> submodules) {
        m_submodules = submodules;
        _m_submodules_isSet = true;
        return this;
    }

    public Module setTypes(final java.util.ArrayList<CustomType> types) {
        m_types = types;
        _m_types_isSet = true;
        return this;
    }

    public Module setId(final EntityIdBase id) {
        super.setId(id);
        return this;
    }

    public Module setName(final String name) {
        super.setName(name);
        return this;
    }

    public Module setParent(final EntityIdBase parent) {
        super.setParent(parent);
        return this;
    }

    public Module setPlacement(final Placement placement) {
        super.setPlacement(placement);
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.Module:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent()).append("\n");
        sb.append("  ").append("placement = ").append(getPlacement()).append("\n");
        sb.append("  ").append("settings = ").append(getSettings()).append("\n");
        sb.append("  ").append("submodules = ").append(getSubmodules()).append("\n");
        sb.append("  ").append("types = ").append(getTypes());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -746099714;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        result = _isPlacementSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getPlacement(), _placement_METADATA.typ())) : result;
        result = _isSettingsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSettings(), _settings_METADATA.typ())) : result;
        result = _isSubmodulesSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSubmodules(), _submodules_METADATA.typ())) : result;
        result = _isTypesSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getTypes(), _types_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (Module.class != other.getClass()) return false;
        final Module o = (Module)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && (_isPlacementSet(FieldSetDepth.SHALLOW) == o._isPlacementSet(FieldSetDepth.SHALLOW))
          && (_isSettingsSet(FieldSetDepth.SHALLOW) == o._isSettingsSet(FieldSetDepth.SHALLOW))
          && (_isSubmodulesSet(FieldSetDepth.SHALLOW) == o._isSubmodulesSet(FieldSetDepth.SHALLOW))
          && (_isTypesSet(FieldSetDepth.SHALLOW) == o._isTypesSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ())
          && EqualityTester.areEqual(getPlacement(), o.getPlacement(), _placement_METADATA.typ())
          && EqualityTester.areEqual(getSettings(), o.getSettings(), _settings_METADATA.typ())
          && EqualityTester.areEqual(getSubmodules(), o.getSubmodules(), _submodules_METADATA.typ())
          && EqualityTester.areEqual(getTypes(), o.getTypes(), _types_METADATA.typ());
    }

    @Override
    public Module deepCopy() {
        final Module out = new Module();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out.setPlacement(DeepCopyer.deepCopy(getPlacement(), _placement_METADATA.typ()));
        out.setSettings(DeepCopyer.deepCopy(getSettings(), _settings_METADATA.typ()));
        out.setSubmodules(DeepCopyer.deepCopy(getSubmodules(), _submodules_METADATA.typ()));
        out.setTypes(DeepCopyer.deepCopy(getTypes(), _types_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setPlacementSet(_isPlacementSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSettingsSet(_isSettingsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSubmodulesSet(_isSubmodulesSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setTypesSet(_isTypesSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getSettings(), _settings_METADATA, _isSettingsSet(FieldSetDepth.SHALLOW));
        visitor.visit(getSubmodules(), _submodules_METADATA, _isSubmodulesSet(FieldSetDepth.SHALLOW));
        visitor.visit(getTypes(), _types_METADATA, _isTypesSet(FieldSetDepth.SHALLOW));
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
            case (_settings_HASH_16BIT):
                setSettings((java.util.HashMap<String, String>)reader.readMapField(field, context));
                return true;
            case (_submodules_HASH_16BIT):
                setSubmodules((java.util.ArrayList<Module>)reader.readListField(field, context));
                return true;
            case (_types_HASH_16BIT):
                setTypes((java.util.ArrayList<CustomType>)reader.readListField(field, context));
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

    public boolean _isSettingsSet(final FieldSetDepth fieldSetDepth) {
        return _m_settings_isSet;
    }

    public boolean _isSubmodulesSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_submodules_isSet;
        } else {
            return _m_submodules_isSet && Validator.validateFieldDeep(getSubmodules(), _submodules_METADATA.typ());
        }
    }

    public boolean _isTypesSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_types_isSet;
        } else {
            return _m_types_isSet && Validator.validateFieldDeep(getTypes(), _types_METADATA.typ());
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
            case (_settings_HASH_16BIT):
                return _isSettingsSet(depth);
            case (_submodules_HASH_16BIT):
                return _isSubmodulesSet(depth);
            case (_types_HASH_16BIT):
                return _isTypesSet(depth);
            default:
                return false;
        }
    }

    public Module _setSettingsSet(final boolean state, final FieldSetDepth depth) {
        _m_settings_isSet = state;
        if (!state)
            m_settings = null;
        return this;
    }

    public Module _setSubmodulesSet(final boolean state, final FieldSetDepth depth) {
        _m_submodules_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getSubmodules(), _submodules_METADATA.typ());
        if (!state)
            m_submodules = null;
        return this;
    }

    public Module _setTypesSet(final boolean state, final FieldSetDepth depth) {
        _m_types_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getTypes(), _types_METADATA.typ());
        if (!state)
            m_types = null;
        return this;
    }

    public Module _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        _setPlacementSet(state, depth);
        _setSettingsSet(state, depth);
        _setSubmodulesSet(state, depth);
        _setTypesSet(state, depth);
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
                && (!_isSubmodulesSet(FieldSetDepth.SHALLOW) || _isSubmodulesSet(FieldSetDepth.DEEP))
                && (!_isTypesSet(FieldSetDepth.SHALLOW) || _isTypesSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
        out += _isPlacementSet(fieldSetDepth) ? 1 : 0;
        out += _isSettingsSet(fieldSetDepth) ? 1 : 0;
        out += _isSubmodulesSet(fieldSetDepth) ? 1 : 0;
        out += _isTypesSet(fieldSetDepth) ? 1 : 0;
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
            case (_settings_HASH_16BIT):
                return _settings_METADATA;
            case (_submodules_HASH_16BIT):
                return _submodules_METADATA;
            case (_types_HASH_16BIT):
                return _types_METADATA;
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
            case (_settings_HASH_32BIT):
                return _settings_METADATA;
            case (_submodules_HASH_32BIT):
                return _submodules_METADATA;
            case (_types_HASH_32BIT):
                return _types_METADATA;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _placement_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "placement", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Placement"), java.util.Arrays.asList("required"));
    public static final Field _settings_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "settings", new se.culvertsoft.mgen.api.model.impl.MapTypeImpl(se.culvertsoft.mgen.api.model.StringType.INSTANCE, se.culvertsoft.mgen.api.model.StringType.INSTANCE), java.util.Arrays.asList(""));
    public static final Field _submodules_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "submodules", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Module")), java.util.Arrays.asList(""));
    public static final Field _types_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Module", "types", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.CustomType")), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;
    public static final short _placement_HASH_16BIT = -30377;
    public static final short _settings_HASH_16BIT = -23871;
    public static final short _submodules_HASH_16BIT = -22338;
    public static final short _types_HASH_16BIT = 22189;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;
    public static final int _placement_HASH_32BIT = 1222341902;
    public static final int _settings_HASH_32BIT = -448421691;
    public static final int _submodules_HASH_32BIT = 1708518168;
    public static final int _types_HASH_32BIT = 1496353072;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.Module";
    public static final short _TYPE_HASH_16BIT = -5423;
    public static final int _TYPE_HASH_32BIT = 1765568046;

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
        fields.add(_settings_METADATA);
        fields.add(_submodules_METADATA);
        fields.add(_types_METADATA);
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
        _TYPE_HASHES_16BIT[2] = -5423;
        _TYPE_HASHES_32BIT[2] = 1765568046;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Module");
        base6416bit.add("6tE");
        base6432bit.add("aTxuLg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
