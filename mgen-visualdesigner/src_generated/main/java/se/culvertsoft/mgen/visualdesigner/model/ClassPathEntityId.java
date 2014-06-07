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

public class ClassPathEntityId extends EntityIdBase {

    private String m_path;
    private boolean _m_path_isSet;

    public ClassPathEntityId() {
        super();
        m_path = null;
        _m_path_isSet = false;
    }

    public ClassPathEntityId(final String path) {
        m_path = path;
        _m_path_isSet = true;
    }

    public String getPath() {
        return m_path;
    }

    public boolean hasPath() {
        return _isPathSet(FieldSetDepth.SHALLOW);
    }

    public ClassPathEntityId unsetPath() {
        _setPathSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public ClassPathEntityId setPath(final String path) {
        m_path = path;
        _m_path_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.ClassPathEntityId:\n");
        sb.append("  ").append("path = ").append(getPath());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -1977530263;
        result = _isPathSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getPath(), _path_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (ClassPathEntityId.class != other.getClass()) return false;
        final ClassPathEntityId o = (ClassPathEntityId)other;
        return true
          && (_isPathSet(FieldSetDepth.SHALLOW) == o._isPathSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getPath(), o.getPath(), _path_METADATA.typ());
    }

    @Override
    public ClassPathEntityId deepCopy() {
        final ClassPathEntityId out = new ClassPathEntityId();
        out.setPath(DeepCopyer.deepCopy(getPath(), _path_METADATA.typ()));
        out._setPathSet(_isPathSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getPath(), _path_METADATA, _isPathSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_path_HASH_16BIT):
                setPath((String)reader.readStringField(field, context));
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

    public boolean _isPathSet(final FieldSetDepth fieldSetDepth) {
        return _m_path_isSet;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_path_HASH_16BIT):
                return _isPathSet(depth);
            default:
                return false;
        }
    }

    public ClassPathEntityId _setPathSet(final boolean state, final FieldSetDepth depth) {
        _m_path_isSet = state;
        if (!state)
            m_path = null;
        return this;
    }

    public ClassPathEntityId _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setPathSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isPathSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && _isPathSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isPathSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_path_HASH_16BIT):
                return _path_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_path_HASH_32BIT):
                return _path_METADATA;
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
		  

    public static final Field _path_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.ClassPathEntityId", "path", se.culvertsoft.mgen.api.model.StringType.INSTANCE, java.util.Arrays.asList("required"));

    public static final short _path_HASH_16BIT = 17373;

    public static final int _path_HASH_32BIT = 190089999;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.ClassPathEntityId";
    public static final short _TYPE_HASH_16BIT = -18744;
    public static final int _TYPE_HASH_32BIT = 680130927;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_path_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[2];
        _TYPE_HASHES_32BIT = new int[2];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = -25632;
        _TYPE_HASHES_32BIT[0] = -146605438;
        names.add("se.culvertsoft.mgen.visualdesigner.model.EntityIdBase");
        base6416bit.add("m+A");
        base6432bit.add("90L6gg");
        _TYPE_HASHES_16BIT[1] = -18744;
        _TYPE_HASHES_32BIT[1] = 680130927;
        names.add("se.culvertsoft.mgen.visualdesigner.model.ClassPathEntityId");
        base6416bit.add("tsg");
        base6432bit.add("KIn5bw");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
