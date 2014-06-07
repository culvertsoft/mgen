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

public class Generator extends se.culvertsoft.mgen.javapack.classes.MGenBase {

    private String m_name;
    private String m_generatorClassName;
    private String m_generatorJarFileFolder;
    private String m_classRegistryPath;
    private String m_outputFolder;
    private java.util.HashMap<String, String> m_settings;
    private boolean _m_name_isSet;
    private boolean _m_generatorClassName_isSet;
    private boolean _m_generatorJarFileFolder_isSet;
    private boolean _m_classRegistryPath_isSet;
    private boolean _m_outputFolder_isSet;
    private boolean _m_settings_isSet;

    public Generator() {
        super();
        m_name = null;
        m_generatorClassName = null;
        m_generatorJarFileFolder = null;
        m_classRegistryPath = null;
        m_outputFolder = null;
        m_settings = null;
        _m_name_isSet = false;
        _m_generatorClassName_isSet = false;
        _m_generatorJarFileFolder_isSet = false;
        _m_classRegistryPath_isSet = false;
        _m_outputFolder_isSet = false;
        _m_settings_isSet = false;
    }

    public Generator(final String name,
                final String generatorClassName,
                final String generatorJarFileFolder,
                final String classRegistryPath,
                final String outputFolder) {
        m_name = name;
        m_generatorClassName = generatorClassName;
        m_generatorJarFileFolder = generatorJarFileFolder;
        m_classRegistryPath = classRegistryPath;
        m_outputFolder = outputFolder;
        _m_name_isSet = true;
        _m_generatorClassName_isSet = true;
        _m_generatorJarFileFolder_isSet = true;
        _m_classRegistryPath_isSet = true;
        _m_outputFolder_isSet = true;
        _m_settings_isSet = false;
    }

    public Generator(final String name,
                final String generatorClassName,
                final String generatorJarFileFolder,
                final String classRegistryPath,
                final String outputFolder,
                final java.util.HashMap<String, String> settings) {
        m_name = name;
        m_generatorClassName = generatorClassName;
        m_generatorJarFileFolder = generatorJarFileFolder;
        m_classRegistryPath = classRegistryPath;
        m_outputFolder = outputFolder;
        m_settings = settings;
        _m_name_isSet = true;
        _m_generatorClassName_isSet = true;
        _m_generatorJarFileFolder_isSet = true;
        _m_classRegistryPath_isSet = true;
        _m_outputFolder_isSet = true;
        _m_settings_isSet = true;
    }

    public String getName() {
        return m_name;
    }

    public String getGeneratorClassName() {
        return m_generatorClassName;
    }

    public String getGeneratorJarFileFolder() {
        return m_generatorJarFileFolder;
    }

    public String getClassRegistryPath() {
        return m_classRegistryPath;
    }

    public String getOutputFolder() {
        return m_outputFolder;
    }

    public java.util.HashMap<String, String> getSettings() {
        return m_settings;
    }

    public java.util.HashMap<String, String> getSettingsMutable() {
        _m_settings_isSet = true;
        return m_settings;
    }

    public boolean hasName() {
        return _isNameSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasGeneratorClassName() {
        return _isGeneratorClassNameSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasGeneratorJarFileFolder() {
        return _isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasClassRegistryPath() {
        return _isClassRegistryPathSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasOutputFolder() {
        return _isOutputFolderSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasSettings() {
        return _isSettingsSet(FieldSetDepth.SHALLOW);
    }

    public Generator unsetName() {
        _setNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator unsetGeneratorClassName() {
        _setGeneratorClassNameSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator unsetGeneratorJarFileFolder() {
        _setGeneratorJarFileFolderSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator unsetClassRegistryPath() {
        _setClassRegistryPathSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator unsetOutputFolder() {
        _setOutputFolderSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator unsetSettings() {
        _setSettingsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Generator setName(final String name) {
        m_name = name;
        _m_name_isSet = true;
        return this;
    }

    public Generator setGeneratorClassName(final String generatorClassName) {
        m_generatorClassName = generatorClassName;
        _m_generatorClassName_isSet = true;
        return this;
    }

    public Generator setGeneratorJarFileFolder(final String generatorJarFileFolder) {
        m_generatorJarFileFolder = generatorJarFileFolder;
        _m_generatorJarFileFolder_isSet = true;
        return this;
    }

    public Generator setClassRegistryPath(final String classRegistryPath) {
        m_classRegistryPath = classRegistryPath;
        _m_classRegistryPath_isSet = true;
        return this;
    }

    public Generator setOutputFolder(final String outputFolder) {
        m_outputFolder = outputFolder;
        _m_outputFolder_isSet = true;
        return this;
    }

    public Generator setSettings(final java.util.HashMap<String, String> settings) {
        m_settings = settings;
        _m_settings_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.Generator:\n");
        sb.append("  ").append("name = ").append(getName()).append("\n");
        sb.append("  ").append("generatorClassName = ").append(getGeneratorClassName()).append("\n");
        sb.append("  ").append("generatorJarFileFolder = ").append(getGeneratorJarFileFolder()).append("\n");
        sb.append("  ").append("classRegistryPath = ").append(getClassRegistryPath()).append("\n");
        sb.append("  ").append("outputFolder = ").append(getOutputFolder()).append("\n");
        sb.append("  ").append("settings = ").append(getSettings());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1238307841;
        result = _isNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getName(), _name_METADATA.typ())) : result;
        result = _isGeneratorClassNameSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getGeneratorClassName(), _generatorClassName_METADATA.typ())) : result;
        result = _isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getGeneratorJarFileFolder(), _generatorJarFileFolder_METADATA.typ())) : result;
        result = _isClassRegistryPathSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getClassRegistryPath(), _classRegistryPath_METADATA.typ())) : result;
        result = _isOutputFolderSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getOutputFolder(), _outputFolder_METADATA.typ())) : result;
        result = _isSettingsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getSettings(), _settings_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (Generator.class != other.getClass()) return false;
        final Generator o = (Generator)other;
        return true
          && (_isNameSet(FieldSetDepth.SHALLOW) == o._isNameSet(FieldSetDepth.SHALLOW))
          && (_isGeneratorClassNameSet(FieldSetDepth.SHALLOW) == o._isGeneratorClassNameSet(FieldSetDepth.SHALLOW))
          && (_isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW) == o._isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW))
          && (_isClassRegistryPathSet(FieldSetDepth.SHALLOW) == o._isClassRegistryPathSet(FieldSetDepth.SHALLOW))
          && (_isOutputFolderSet(FieldSetDepth.SHALLOW) == o._isOutputFolderSet(FieldSetDepth.SHALLOW))
          && (_isSettingsSet(FieldSetDepth.SHALLOW) == o._isSettingsSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getName(), o.getName(), _name_METADATA.typ())
          && EqualityTester.areEqual(getGeneratorClassName(), o.getGeneratorClassName(), _generatorClassName_METADATA.typ())
          && EqualityTester.areEqual(getGeneratorJarFileFolder(), o.getGeneratorJarFileFolder(), _generatorJarFileFolder_METADATA.typ())
          && EqualityTester.areEqual(getClassRegistryPath(), o.getClassRegistryPath(), _classRegistryPath_METADATA.typ())
          && EqualityTester.areEqual(getOutputFolder(), o.getOutputFolder(), _outputFolder_METADATA.typ())
          && EqualityTester.areEqual(getSettings(), o.getSettings(), _settings_METADATA.typ());
    }

    @Override
    public Generator deepCopy() {
        final Generator out = new Generator();
        out.setName(DeepCopyer.deepCopy(getName(), _name_METADATA.typ()));
        out.setGeneratorClassName(DeepCopyer.deepCopy(getGeneratorClassName(), _generatorClassName_METADATA.typ()));
        out.setGeneratorJarFileFolder(DeepCopyer.deepCopy(getGeneratorJarFileFolder(), _generatorJarFileFolder_METADATA.typ()));
        out.setClassRegistryPath(DeepCopyer.deepCopy(getClassRegistryPath(), _classRegistryPath_METADATA.typ()));
        out.setOutputFolder(DeepCopyer.deepCopy(getOutputFolder(), _outputFolder_METADATA.typ()));
        out.setSettings(DeepCopyer.deepCopy(getSettings(), _settings_METADATA.typ()));
        out._setNameSet(_isNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setGeneratorClassNameSet(_isGeneratorClassNameSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setGeneratorJarFileFolderSet(_isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setClassRegistryPathSet(_isClassRegistryPathSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setOutputFolderSet(_isOutputFolderSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setSettingsSet(_isSettingsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getName(), _name_METADATA, _isNameSet(FieldSetDepth.SHALLOW));
        visitor.visit(getGeneratorClassName(), _generatorClassName_METADATA, _isGeneratorClassNameSet(FieldSetDepth.SHALLOW));
        visitor.visit(getGeneratorJarFileFolder(), _generatorJarFileFolder_METADATA, _isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW));
        visitor.visit(getClassRegistryPath(), _classRegistryPath_METADATA, _isClassRegistryPathSet(FieldSetDepth.SHALLOW));
        visitor.visit(getOutputFolder(), _outputFolder_METADATA, _isOutputFolderSet(FieldSetDepth.SHALLOW));
        visitor.visit(getSettings(), _settings_METADATA, _isSettingsSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_name_HASH_16BIT):
                setName((String)reader.readStringField(field, context));
                return true;
            case (_generatorClassName_HASH_16BIT):
                setGeneratorClassName((String)reader.readStringField(field, context));
                return true;
            case (_generatorJarFileFolder_HASH_16BIT):
                setGeneratorJarFileFolder((String)reader.readStringField(field, context));
                return true;
            case (_classRegistryPath_HASH_16BIT):
                setClassRegistryPath((String)reader.readStringField(field, context));
                return true;
            case (_outputFolder_HASH_16BIT):
                setOutputFolder((String)reader.readStringField(field, context));
                return true;
            case (_settings_HASH_16BIT):
                setSettings((java.util.HashMap<String, String>)reader.readMapField(field, context));
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

    public boolean _isNameSet(final FieldSetDepth fieldSetDepth) {
        return _m_name_isSet;
    }

    public boolean _isGeneratorClassNameSet(final FieldSetDepth fieldSetDepth) {
        return _m_generatorClassName_isSet;
    }

    public boolean _isGeneratorJarFileFolderSet(final FieldSetDepth fieldSetDepth) {
        return _m_generatorJarFileFolder_isSet;
    }

    public boolean _isClassRegistryPathSet(final FieldSetDepth fieldSetDepth) {
        return _m_classRegistryPath_isSet;
    }

    public boolean _isOutputFolderSet(final FieldSetDepth fieldSetDepth) {
        return _m_outputFolder_isSet;
    }

    public boolean _isSettingsSet(final FieldSetDepth fieldSetDepth) {
        return _m_settings_isSet;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_name_HASH_16BIT):
                return _isNameSet(depth);
            case (_generatorClassName_HASH_16BIT):
                return _isGeneratorClassNameSet(depth);
            case (_generatorJarFileFolder_HASH_16BIT):
                return _isGeneratorJarFileFolderSet(depth);
            case (_classRegistryPath_HASH_16BIT):
                return _isClassRegistryPathSet(depth);
            case (_outputFolder_HASH_16BIT):
                return _isOutputFolderSet(depth);
            case (_settings_HASH_16BIT):
                return _isSettingsSet(depth);
            default:
                return false;
        }
    }

    public Generator _setNameSet(final boolean state, final FieldSetDepth depth) {
        _m_name_isSet = state;
        if (!state)
            m_name = null;
        return this;
    }

    public Generator _setGeneratorClassNameSet(final boolean state, final FieldSetDepth depth) {
        _m_generatorClassName_isSet = state;
        if (!state)
            m_generatorClassName = null;
        return this;
    }

    public Generator _setGeneratorJarFileFolderSet(final boolean state, final FieldSetDepth depth) {
        _m_generatorJarFileFolder_isSet = state;
        if (!state)
            m_generatorJarFileFolder = null;
        return this;
    }

    public Generator _setClassRegistryPathSet(final boolean state, final FieldSetDepth depth) {
        _m_classRegistryPath_isSet = state;
        if (!state)
            m_classRegistryPath = null;
        return this;
    }

    public Generator _setOutputFolderSet(final boolean state, final FieldSetDepth depth) {
        _m_outputFolder_isSet = state;
        if (!state)
            m_outputFolder = null;
        return this;
    }

    public Generator _setSettingsSet(final boolean state, final FieldSetDepth depth) {
        _m_settings_isSet = state;
        if (!state)
            m_settings = null;
        return this;
    }

    public Generator _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setNameSet(state, depth);
        _setGeneratorClassNameSet(state, depth);
        _setGeneratorJarFileFolderSet(state, depth);
        _setClassRegistryPathSet(state, depth);
        _setOutputFolderSet(state, depth);
        _setSettingsSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isNameSet(FieldSetDepth.SHALLOW)
                && _isGeneratorClassNameSet(FieldSetDepth.SHALLOW)
                && _isGeneratorJarFileFolderSet(FieldSetDepth.SHALLOW)
                && _isClassRegistryPathSet(FieldSetDepth.SHALLOW)
                && _isOutputFolderSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && _isNameSet(FieldSetDepth.DEEP)
                && _isGeneratorClassNameSet(FieldSetDepth.DEEP)
                && _isGeneratorJarFileFolderSet(FieldSetDepth.DEEP)
                && _isClassRegistryPathSet(FieldSetDepth.DEEP)
                && _isOutputFolderSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isNameSet(fieldSetDepth) ? 1 : 0;
        out += _isGeneratorClassNameSet(fieldSetDepth) ? 1 : 0;
        out += _isGeneratorJarFileFolderSet(fieldSetDepth) ? 1 : 0;
        out += _isClassRegistryPathSet(fieldSetDepth) ? 1 : 0;
        out += _isOutputFolderSet(fieldSetDepth) ? 1 : 0;
        out += _isSettingsSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_name_HASH_16BIT):
                return _name_METADATA;
            case (_generatorClassName_HASH_16BIT):
                return _generatorClassName_METADATA;
            case (_generatorJarFileFolder_HASH_16BIT):
                return _generatorJarFileFolder_METADATA;
            case (_classRegistryPath_HASH_16BIT):
                return _classRegistryPath_METADATA;
            case (_outputFolder_HASH_16BIT):
                return _outputFolder_METADATA;
            case (_settings_HASH_16BIT):
                return _settings_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_name_HASH_32BIT):
                return _name_METADATA;
            case (_generatorClassName_HASH_32BIT):
                return _generatorClassName_METADATA;
            case (_generatorJarFileFolder_HASH_32BIT):
                return _generatorJarFileFolder_METADATA;
            case (_classRegistryPath_HASH_32BIT):
                return _classRegistryPath_METADATA;
            case (_outputFolder_HASH_32BIT):
                return _outputFolder_METADATA;
            case (_settings_HASH_32BIT):
                return _settings_METADATA;
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
		  

    public static final Field _name_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "name", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _generatorClassName_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "generatorClassName", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _generatorJarFileFolder_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "generatorJarFileFolder", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _classRegistryPath_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "classRegistryPath", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _outputFolder_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "outputFolder", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));
    public static final Field _settings_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Generator", "settings", new se.culvertsoft.mgen.api.model.impl.MapTypeImpl(se.culvertsoft.mgen.api.model.StringType.INSTANCE, se.culvertsoft.mgen.api.model.StringType.INSTANCE), java.util.Arrays.asList(""));

    public static final short _name_HASH_16BIT = -28058;
    public static final short _generatorClassName_HASH_16BIT = -12217;
    public static final short _generatorJarFileFolder_HASH_16BIT = -5367;
    public static final short _classRegistryPath_HASH_16BIT = -3281;
    public static final short _outputFolder_HASH_16BIT = 4745;
    public static final short _settings_HASH_16BIT = -23871;

    public static final int _name_HASH_32BIT = 1579384326;
    public static final int _generatorClassName_HASH_32BIT = -511907473;
    public static final int _generatorJarFileFolder_HASH_32BIT = -641263628;
    public static final int _classRegistryPath_HASH_32BIT = 768400042;
    public static final int _outputFolder_HASH_32BIT = 121432836;
    public static final int _settings_HASH_32BIT = -448421691;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.Generator";
    public static final short _TYPE_HASH_16BIT = -26942;
    public static final int _TYPE_HASH_32BIT = -437298436;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_name_METADATA);
        fields.add(_generatorClassName_METADATA);
        fields.add(_generatorJarFileFolder_METADATA);
        fields.add(_classRegistryPath_METADATA);
        fields.add(_outputFolder_METADATA);
        fields.add(_settings_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[1];
        _TYPE_HASHES_32BIT = new int[1];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = -26942;
        _TYPE_HASHES_32BIT[0] = -437298436;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Generator");
        base6416bit.add("lsI");
        base6432bit.add("5e9a/A");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
