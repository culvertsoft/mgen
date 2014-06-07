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

public class Placement extends se.culvertsoft.mgen.javapack.classes.MGenBase {

    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    private boolean _m_x_isSet;
    private boolean _m_y_isSet;
    private boolean _m_width_isSet;
    private boolean _m_height_isSet;

    public Placement() {
        super();
        m_x = 0;
        m_y = 0;
        m_width = 0;
        m_height = 0;
        _m_x_isSet = false;
        _m_y_isSet = false;
        _m_width_isSet = false;
        _m_height_isSet = false;
    }

    public Placement(final int x,
                final int y,
                final int width,
                final int height) {
        m_x = x;
        m_y = y;
        m_width = width;
        m_height = height;
        _m_x_isSet = true;
        _m_y_isSet = true;
        _m_width_isSet = true;
        _m_height_isSet = true;
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
    }

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }

    public boolean hasX() {
        return _isXSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasY() {
        return _isYSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasWidth() {
        return _isWidthSet(FieldSetDepth.SHALLOW);
    }

    public boolean hasHeight() {
        return _isHeightSet(FieldSetDepth.SHALLOW);
    }

    public Placement unsetX() {
        _setXSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Placement unsetY() {
        _setYSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Placement unsetWidth() {
        _setWidthSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Placement unsetHeight() {
        _setHeightSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public Placement setX(final int x) {
        m_x = x;
        _m_x_isSet = true;
        return this;
    }

    public Placement setY(final int y) {
        m_y = y;
        _m_y_isSet = true;
        return this;
    }

    public Placement setWidth(final int width) {
        m_width = width;
        _m_width_isSet = true;
        return this;
    }

    public Placement setHeight(final int height) {
        m_height = height;
        _m_height_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.Placement:\n");
        sb.append("  ").append("x = ").append(getX()).append("\n");
        sb.append("  ").append("y = ").append(getY()).append("\n");
        sb.append("  ").append("width = ").append(getWidth()).append("\n");
        sb.append("  ").append("height = ").append(getHeight());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -1550676973;
        result = _isXSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getX(), _x_METADATA.typ())) : result;
        result = _isYSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getY(), _y_METADATA.typ())) : result;
        result = _isWidthSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getWidth(), _width_METADATA.typ())) : result;
        result = _isHeightSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getHeight(), _height_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (Placement.class != other.getClass()) return false;
        final Placement o = (Placement)other;
        return true
          && (_isXSet(FieldSetDepth.SHALLOW) == o._isXSet(FieldSetDepth.SHALLOW))
          && (_isYSet(FieldSetDepth.SHALLOW) == o._isYSet(FieldSetDepth.SHALLOW))
          && (_isWidthSet(FieldSetDepth.SHALLOW) == o._isWidthSet(FieldSetDepth.SHALLOW))
          && (_isHeightSet(FieldSetDepth.SHALLOW) == o._isHeightSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getX(), o.getX(), _x_METADATA.typ())
          && EqualityTester.areEqual(getY(), o.getY(), _y_METADATA.typ())
          && EqualityTester.areEqual(getWidth(), o.getWidth(), _width_METADATA.typ())
          && EqualityTester.areEqual(getHeight(), o.getHeight(), _height_METADATA.typ());
    }

    @Override
    public Placement deepCopy() {
        final Placement out = new Placement();
        out.setX(DeepCopyer.deepCopy(getX(), _x_METADATA.typ()));
        out.setY(DeepCopyer.deepCopy(getY(), _y_METADATA.typ()));
        out.setWidth(DeepCopyer.deepCopy(getWidth(), _width_METADATA.typ()));
        out.setHeight(DeepCopyer.deepCopy(getHeight(), _height_METADATA.typ()));
        out._setXSet(_isXSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setYSet(_isYSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setWidthSet(_isWidthSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
        out._setHeightSet(_isHeightSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getX(), _x_METADATA, _isXSet(FieldSetDepth.SHALLOW));
        visitor.visit(getY(), _y_METADATA, _isYSet(FieldSetDepth.SHALLOW));
        visitor.visit(getWidth(), _width_METADATA, _isWidthSet(FieldSetDepth.SHALLOW));
        visitor.visit(getHeight(), _height_METADATA, _isHeightSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_x_HASH_16BIT):
                setX((int)reader.readInt32Field(field, context));
                return true;
            case (_y_HASH_16BIT):
                setY((int)reader.readInt32Field(field, context));
                return true;
            case (_width_HASH_16BIT):
                setWidth((int)reader.readInt32Field(field, context));
                return true;
            case (_height_HASH_16BIT):
                setHeight((int)reader.readInt32Field(field, context));
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

    public boolean _isXSet(final FieldSetDepth fieldSetDepth) {
        return _m_x_isSet;
    }

    public boolean _isYSet(final FieldSetDepth fieldSetDepth) {
        return _m_y_isSet;
    }

    public boolean _isWidthSet(final FieldSetDepth fieldSetDepth) {
        return _m_width_isSet;
    }

    public boolean _isHeightSet(final FieldSetDepth fieldSetDepth) {
        return _m_height_isSet;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_x_HASH_16BIT):
                return _isXSet(depth);
            case (_y_HASH_16BIT):
                return _isYSet(depth);
            case (_width_HASH_16BIT):
                return _isWidthSet(depth);
            case (_height_HASH_16BIT):
                return _isHeightSet(depth);
            default:
                return false;
        }
    }

    public Placement _setXSet(final boolean state, final FieldSetDepth depth) {
        _m_x_isSet = state;
        if (!state)
            m_x = 0;
        return this;
    }

    public Placement _setYSet(final boolean state, final FieldSetDepth depth) {
        _m_y_isSet = state;
        if (!state)
            m_y = 0;
        return this;
    }

    public Placement _setWidthSet(final boolean state, final FieldSetDepth depth) {
        _m_width_isSet = state;
        if (!state)
            m_width = 0;
        return this;
    }

    public Placement _setHeightSet(final boolean state, final FieldSetDepth depth) {
        _m_height_isSet = state;
        if (!state)
            m_height = 0;
        return this;
    }

    public Placement _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setXSet(state, depth);
        _setYSet(state, depth);
        _setWidthSet(state, depth);
        _setHeightSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isXSet(fieldSetDepth) ? 1 : 0;
        out += _isYSet(fieldSetDepth) ? 1 : 0;
        out += _isWidthSet(fieldSetDepth) ? 1 : 0;
        out += _isHeightSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_x_HASH_16BIT):
                return _x_METADATA;
            case (_y_HASH_16BIT):
                return _y_METADATA;
            case (_width_HASH_16BIT):
                return _width_METADATA;
            case (_height_HASH_16BIT):
                return _height_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_x_HASH_32BIT):
                return _x_METADATA;
            case (_y_HASH_32BIT):
                return _y_METADATA;
            case (_width_HASH_32BIT):
                return _width_METADATA;
            case (_height_HASH_32BIT):
                return _height_METADATA;
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
		  

    public static final Field _x_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Placement", "x", se.culvertsoft.mgen.api.model.Int32Type.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _y_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Placement", "y", se.culvertsoft.mgen.api.model.Int32Type.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _width_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Placement", "width", se.culvertsoft.mgen.api.model.Int32Type.INSTANCE, java.util.Arrays.asList(""));
    public static final Field _height_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.Placement", "height", se.culvertsoft.mgen.api.model.Int32Type.INSTANCE, java.util.Arrays.asList(""));

    public static final short _x_HASH_16BIT = 7791;
    public static final short _y_HASH_16BIT = 3662;
    public static final short _width_HASH_16BIT = -20637;
    public static final short _height_HASH_16BIT = 28175;

    public static final int _x_HASH_32BIT = -1931733373;
    public static final int _y_HASH_32BIT = -69523947;
    public static final int _width_HASH_32BIT = -1944435409;
    public static final int _height_HASH_32BIT = -179444465;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.Placement";
    public static final short _TYPE_HASH_16BIT = 8590;
    public static final int _TYPE_HASH_32BIT = 1738167426;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_x_METADATA);
        fields.add(_y_METADATA);
        fields.add(_width_METADATA);
        fields.add(_height_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[1];
        _TYPE_HASHES_32BIT = new int[1];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 8590;
        _TYPE_HASHES_32BIT[0] = 1738167426;
        names.add("se.culvertsoft.mgen.visualdesigner.model.Placement");
        base6416bit.add("IY4");
        base6432bit.add("Z5pUgg");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
