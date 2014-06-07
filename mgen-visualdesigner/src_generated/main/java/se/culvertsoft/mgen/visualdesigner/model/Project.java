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

public class Project extends Entity {

    private java.util.ArrayList<Generator> m_generators;
    private java.util.HashMap<String, String> m_settings;
    private java.util.ArrayList<String> m_dependencies;
    private java.util.ArrayList<Module> m_modules;
    private boolean _m_generators_isSet;
    private boolean _m_settings_isSet;
    private boolean _m_dependencies_isSet;
    private boolean _m_modules_isSet;

    public Project() {
        super();
        m_generators = null;
        m_settings = null;
        m_dependencies = null;
        m_modules = null;
        _m_generators_isSet = false;
        _m_settings_isSet = false;
        _m_dependencies_isSet = false;
        _m_modules_isSet = false;
    }

    public Project(final java.util.ArrayList<Generator> generators) {
        m_generators = generators;
        _m_generators_isSet = true;
        _m_settings_isSet = false;
        _m_dependencies_isSet = false;
        _m_modules_isSet = false;
    }

    public Project(final EntityIdBase id,
                final String name,
                final EntityIdBase parent,
                final java.util.ArrayList<Generator> generators,
                final java.util.HashMap<String, String> settings,
                final java.util.ArrayList<String> dependencies,
                final java.util.ArrayList<Module> modules) {
        super(id, name, parent);
        m_generators = generators;
        m_settings = settings;
        m_dependencies = dependencies;
        m_modules = modules;
        _m_generators_isSet = true;
        _m_settings_isSet = true;
        _m_dependencies_isSet = true;
        _m_modules_isSet = true;
    }

    public java.util.ArrayList<Generator> getGenerators() {
        return m_generators;
    }

    public java.util.HashMap<String, String> getSettings() {
        return m_settings;
    }

    public java.util.ArrayList<String> getDependencies() {
        return m_dependencies;
    }

    public java.util.ArrayList<Module> getModules() {
        return m_modules;
    }

    public java.util.ArrayList<Generator> getGeneratorsMutable() {
        _m_generators_isSet = true;
        return m_generators;
    }

    public java.util.HashMap<String, String> getSettingsMutable() {
        _m_settings_isSet = true;
        return m_settings;
    }

    public java.util.ArrayList<String> getDependenciesMutable() {
        _m_dependencies_isSet = true;
        return m_dependencies;
    }

    public java.util.ArrayList<Module> getModulesMutable() {
        _m_modules_isSet = true;
        return m_modules;
    }

    public boolean hasGenerators() {
        return _isGeneratorsSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasSettings() {
        return _isSettingsSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasDependencies() {
        return _isDependenciesSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasModules() {
        return _isModulesSet(FieldSetDepth.SHALLOW);
    }

    public Project unsetId() {
        _setIdSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetParent() {
        _setParentSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetGenerators() {
        _setGeneratorsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetSettings() {
        _setSettingsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetDependencies() {
        _setDependenciesSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project unsetModules() {
        _setModulesSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Project setGenerators(final java.util.ArrayList<Generator> generators) {
        m_generators = generators;
        _m_generators_isSet = true;
        return this;
    }

    public Project setSettings(final java.util.HashMap<String, String> settings) {
        m_settings = settings;
        _m_settings_isSet = true;
        return this;
    }

    public Project setDependencies(final java.util.ArrayList<String> dependencies) {
        m_dependencies = dependencies;
        _m_dependencies_isSet = true;
        return this;
    }

    public Project setModules(final java.util.ArrayList<Module> modules) {
        m_modules = modules;
        _m_modules_isSet = true;
        return this;
    }

    public Project setId(final EntityIdBase id) {
        super.setId(id);
        return this;
    }

    public Project setName(final String name) {
        super.setName(name);
        return this;
    }

    public Project setParent(final EntityIdBase parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.Project:\n");
        sb.append("  ").append("id = ").append(getId()).append("\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("parent = ").append(getParent()).append("\n");
        sb.append("  ").append("generators = ").append(getGenerators()).append("\n");
        sb.append("  ").append("settings = ").append(getSettings()).append("\n");
        sb.append("  ").append("dependencies = ").append(getDependencies()).append("\n");
        sb.append("  ").append("modules = ").append(getModules());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1103968199;
        result = _isIdSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getId(), _id_METADATA.typ())) : result;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isParentSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getParent(), _parent_METADATA.typ())) : result;
        result = _isGeneratorsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getGenerators(), _generators_METADATA.typ())) : result;
        result = _isSettingsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSettings(), _settings_METADATA.typ())) : result;
        result = _isDependenciesSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getDependencies(), _dependencies_METADATA.typ())) : result;
        result = _isModulesSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getModules(), _modules_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (Project.class != other.getClass()) return false;
        final Project o = (Project)other;
        return true
          && (_isIdSet(FieldSetDepth.SHALLOW) == o._isIdSet(FieldSetDepth.SHALLOW))
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isParentSet(FieldSetDepth.SHALLOW) == o._isParentSet(FieldSetDepth.SHALLOW))
          && (_isGeneratorsSet(FieldSetDepth.SHALLOW) == o._isGeneratorsSet(FieldSetDepth.SHALLOW))
          && (_isSettingsSet(FieldSetDepth.SHALLOW) == o._isSettingsSet(FieldSetDepth.SHALLOW))
          && (_isDependenciesSet(FieldSetDepth.SHALLOW) == o._isDependenciesSet(FieldSetDepth.SHALLOW))
          && (_isModulesSet(FieldSetDepth.SHALLOW) == o._isModulesSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getId(), o.getId(), _id_METADATA.typ())
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getParent(), o.getParent(), _parent_METADATA.typ())
          && EqualityTester.areEqual(getGenerators(), o.getGenerators(), _generators_METADATA.typ())
          && EqualityTester.areEqual(getSettings(), o.getSettings(), _settings_METADATA.typ())
          && EqualityTester.areEqual(getDependencies(), o.getDependencies(), _dependencies_METADATA.typ())
          && EqualityTester.areEqual(getModules(), o.getModules(), _modules_METADATA.typ());
    }

    @Override
    public Project deepCopy() {
        final Project out = new Project();
        out.setId(DeepCopyer.deepCopy(getId(), _id_METADATA.typ()));
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setParent(DeepCopyer.deepCopy(getParent(), _parent_METADATA.typ()));
        out.setGenerators(DeepCopyer.deepCopy(getGenerators(), _generators_METADATA.typ()));
        out.setSettings(DeepCopyer.deepCopy(getSettings(), _settings_METADATA.typ()));
        out.setDependencies(DeepCopyer.deepCopy(getDependencies(), _dependencies_METADATA.typ()));
        out.setModules(DeepCopyer.deepCopy(getModules(), _modules_METADATA.typ()));
        out._setIdSet(_isIdSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setParentSet(_isParentSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setGeneratorsSet(_isGeneratorsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSettingsSet(_isSettingsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setDependenciesSet(_isDependenciesSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setModulesSet(_isModulesSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getGenerators(), _generators_METADATA, _isGeneratorsSet(FieldSetDepth.SHALLOW));
        visitor.visit(getSettings(), _settings_METADATA, _isSettingsSet(FieldSetDepth.SHALLOW));
        visitor.visit(getDependencies(), _dependencies_METADATA, _isDependenciesSet(FieldSetDepth.SHALLOW));
        visitor.visit(getModules(), _modules_METADATA, _isModulesSet(FieldSetDepth.SHALLOW));
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
            case (_generators_HASH_16BIT):
                setGenerators((java.util.ArrayList<Generator>)reader.readListField(field, context));
                return true;
            case (_settings_HASH_16BIT):
                setSettings((java.util.HashMap<String, String>)reader.readMapField(field, context));
                return true;
            case (_dependencies_HASH_16BIT):
                setDependencies((java.util.ArrayList<String>)reader.readListField(field, context));
                return true;
            case (_modules_HASH_16BIT):
                setModules((java.util.ArrayList<Module>)reader.readListField(field, context));
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

    public boolean _isGeneratorsSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_generators_isSet;
        } else {
            return _m_generators_isSet && Validator.validateFieldDeep(getGenerators(), _generators_METADATA.typ());
        }
    }

    public boolean _isSettingsSet(final FieldSetDepth fieldSetDepth) {
        return _m_settings_isSet;
    }

    public boolean _isDependenciesSet(final FieldSetDepth fieldSetDepth) {
        return _m_dependencies_isSet;
    }

    public boolean _isModulesSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_modules_isSet;
        } else {
            return _m_modules_isSet && Validator.validateFieldDeep(getModules(), _modules_METADATA.typ());
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
            case (_generators_HASH_16BIT):
                return _isGeneratorsSet(depth);
            case (_settings_HASH_16BIT):
                return _isSettingsSet(depth);
            case (_dependencies_HASH_16BIT):
                return _isDependenciesSet(depth);
            case (_modules_HASH_16BIT):
                return _isModulesSet(depth);
            default:
                return false;
        }
    }

    public Project _setGeneratorsSet(final boolean state, final FieldSetDepth depth) {
        _m_generators_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getGenerators(), _generators_METADATA.typ());
        if (!state)
            m_generators = null;
        return this;
    }

    public Project _setSettingsSet(final boolean state, final FieldSetDepth depth) {
        _m_settings_isSet = state;
        if (!state)
            m_settings = null;
        return this;
    }

    public Project _setDependenciesSet(final boolean state, final FieldSetDepth depth) {
        _m_dependencies_isSet = state;
        if (!state)
            m_dependencies = null;
        return this;
    }

    public Project _setModulesSet(final boolean state, final FieldSetDepth depth) {
        _m_modules_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getModules(), _modules_METADATA.typ());
        if (!state)
            m_modules = null;
        return this;
    }

    public Project _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setIdSet(state, depth);
        _setNameSet(state, depth);
        _setParentSet(state, depth);
        _setGeneratorsSet(state, depth);
        _setSettingsSet(state, depth);
        _setDependenciesSet(state, depth);
        _setModulesSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isGeneratorsSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && (!_isIdSet(FieldSetDepth.SHALLOW) || _isIdSet(FieldSetDepth.DEEP))
                && (!_isParentSet(FieldSetDepth.SHALLOW) || _isParentSet(FieldSetDepth.DEEP))
                && _isGeneratorsSet(FieldSetDepth.DEEP)
                && (!_isModulesSet(FieldSetDepth.SHALLOW) || _isModulesSet(FieldSetDepth.DEEP));
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isIdSet(fieldSetDepth) ? 1 : 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isParentSet(fieldSetDepth) ? 1 : 0;
        out += _isGeneratorsSet(fieldSetDepth) ? 1 : 0;
        out += _isSettingsSet(fieldSetDepth) ? 1 : 0;
        out += _isDependenciesSet(fieldSetDepth) ? 1 : 0;
        out += _isModulesSet(fieldSetDepth) ? 1 : 0;
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
            case (_generators_HASH_16BIT):
                return _generators_METADATA;
            case (_settings_HASH_16BIT):
                return _settings_METADATA;
            case (_dependencies_HASH_16BIT):
                return _dependencies_METADATA;
            case (_modules_HASH_16BIT):
                return _modules_METADATA;
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
            case (_generators_HASH_32BIT):
                return _generators_METADATA;
            case (_settings_HASH_32BIT):
                return _settings_METADATA;
            case (_dependencies_HASH_32BIT):
                return _dependencies_METADATA;
            case (_modules_HASH_32BIT):
                return _modules_METADATA;
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
		  

    public static final Field _id_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "id", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _parent_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "parent", new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase"), java.util.Arrays.asList(""));
    public static final Field _generators_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "generators", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Generator")), java.util.Arrays.asList("required"));
    public static final Field _settings_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "settings", new se.culvertsoft.mgen.api.model.impl.MapTypeImpl(se.culvertsoft.mgen.api.model.StringType.INSTANCE, se.culvertsoft.mgen.api.model.StringType.INSTANCE), java.util.Arrays.asList(""));
    public static final Field _dependencies_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "dependencies", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(se.culvertsoft.mgen.api.model.StringType.INSTANCE), java.util.Arrays.asList(""));
    public static final Field _modules_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Project", "modules", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Module")), java.util.Arrays.asList(""));

    public static final short _id_HASH_16BIT = -32609;
    public static final short _name_HASH_16BIT = -28058;
    public static final short _parent_HASH_16BIT = 12721;
    public static final short _generators_HASH_16BIT = -32151;
    public static final short _settings_HASH_16BIT = -23871;
    public static final short _dependencies_HASH_16BIT = -9277;
    public static final short _modules_HASH_16BIT = 2062;

    public static final int _id_HASH_32BIT = -1086757040;
    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _parent_HASH_32BIT = 1032740943;
    public static final int _generators_HASH_32BIT = 1270943820;
    public static final int _settings_HASH_32BIT = -448421691;
    public static final int _dependencies_HASH_32BIT = -368086899;
    public static final int _modules_HASH_32BIT = 783762391;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.Project";
    public static final short _TYPE_HASH_16BIT = 11584;
    public static final int _TYPE_HASH_32BIT = -961188461;

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
        fields.add(_generators_METADATA);
        fields.add(_settings_METADATA);
        fields.add(_dependencies_METADATA);
        fields.add(_modules_METADATA);
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
        _TYPE_HASHES_16BIT[1] = 11584;
        _TYPE_HASHES_32BIT[1] = -961188461;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Project");
        base6416bit.add("LUA");
        base6432bit.add("xrVtkw");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
