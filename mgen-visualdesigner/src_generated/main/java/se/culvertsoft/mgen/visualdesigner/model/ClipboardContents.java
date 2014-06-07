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

public class ClipboardContents extends se.culvertsoft.mgen.javapack.classes.MGenBase {

    private java.util.ArrayList<Entity> m_items;
    private boolean _m_items_isSet;

    public ClipboardContents() {
        super();
        m_items = null;
        _m_items_isSet = false;
    }

    public ClipboardContents(final java.util.ArrayList<Entity> items) {
        m_items = items;
        _m_items_isSet = true;
    }

    public java.util.ArrayList<Entity> getItems() {
        return m_items;
    }

    public java.util.ArrayList<Entity> getItemsMutable() {
        _m_items_isSet = true;
        return m_items;
    }

    public boolean hasItems() {
        return _isItemsSet(FieldSetDepth.SHALLOW);
    }

    public ClipboardContents unsetItems() {
        _setItemsSet(false, FieldSetDepth.SHALLOW);
        return this;
    }

    public ClipboardContents setItems(final java.util.ArrayList<Entity> items) {
        m_items = items;
        _m_items_isSet = true;
        return this;
    }

    @Override
    public String toString() {
        final java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("se.culvertsoft.mgen.visualdesigner.model.ClipboardContents:\n");
        sb.append("  ").append("items = ").append(getItems());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = -318172674;
        result = _isItemsSet(FieldSetDepth.SHALLOW) ? (prime * result + FieldHasher.calc(getItems(), _items_METADATA.typ())) : result;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (ClipboardContents.class != other.getClass()) return false;
        final ClipboardContents o = (ClipboardContents)other;
        return true
          && (_isItemsSet(FieldSetDepth.SHALLOW) == o._isItemsSet(FieldSetDepth.SHALLOW))
          && EqualityTester.areEqual(getItems(), o.getItems(), _items_METADATA.typ());
    }

    @Override
    public ClipboardContents deepCopy() {
        final ClipboardContents out = new ClipboardContents();
        out.setItems(DeepCopyer.deepCopy(getItems(), _items_METADATA.typ()));
        out._setItemsSet(_isItemsSet(FieldSetDepth.SHALLOW), FieldSetDepth.SHALLOW);
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
        visitor.visit(getItems(), _items_METADATA, _isItemsSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            case (_items_HASH_16BIT):
                setItems((java.util.ArrayList<Entity>)reader.readListField(field, context));
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

    public boolean _isItemsSet(final FieldSetDepth fieldSetDepth) {
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return _m_items_isSet;
        } else {
            return _m_items_isSet && Validator.validateFieldDeep(getItems(), _items_METADATA.typ());
        }
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            case (_items_HASH_16BIT):
                return _isItemsSet(depth);
            default:
                return false;
        }
    }

    public ClipboardContents _setItemsSet(final boolean state, final FieldSetDepth depth) {
        _m_items_isSet = state;
        if (depth == FieldSetDepth.DEEP)
            Marker.setFieldSetDeep(getItems(), _items_METADATA.typ());
        if (!state)
            m_items = null;
        return this;
    }

    public ClipboardContents _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        _setItemsSet(state, depth);
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true
                && _isItemsSet(FieldSetDepth.SHALLOW);
        } else {
            return true
                && _isItemsSet(FieldSetDepth.DEEP);
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        out += _isItemsSet(fieldSetDepth) ? 1 : 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            case (_items_HASH_16BIT):
                return _items_METADATA;
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            case (_items_HASH_32BIT):
                return _items_METADATA;
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
		  

    public static final Field _items_METADATA = new Field("se.culvertsoft.mgen.visualdesigner.model.ClipboardContents", "items", new se.culvertsoft.mgen.api.model.impl.ListTypeImpl(new se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl("se.culvertsoft.mgen.visualdesigner.model.Entity")), java.util.Arrays.asList("required"));

    public static final short _items_HASH_16BIT = 27441;

    public static final int _items_HASH_32BIT = -518067891;

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.ClipboardContents";
    public static final short _TYPE_HASH_16BIT = -1866;
    public static final int _TYPE_HASH_32BIT = 1035304385;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        fields.add(_items_METADATA);
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[1];
        _TYPE_HASHES_32BIT = new int[1];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = -1866;
        _TYPE_HASHES_32BIT[0] = 1035304385;
        names.add("se.culvertsoft.mgen.visualdesigner.model.ClipboardContents");
        base6416bit.add("+LY");
        base6432bit.add("PbV9wQ");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}
