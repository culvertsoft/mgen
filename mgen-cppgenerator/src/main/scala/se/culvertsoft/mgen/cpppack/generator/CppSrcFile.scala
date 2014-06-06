package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import CppTypeNames.getTypeName
import se.culvertsoft.mgen.api.types.CustomType

object CppSrcFile extends CppSrcFileOrHeader(".cpp") {

   import CppTypeNames._

   override def mkIncludes(t: CustomType) {
      CppGenUtils.include(t)
      CppGenUtils.include("mgen/util/hash.h")
      CppGenUtils.include("mgen/util/validation.h")
      txtBuffer.endl()
   }

   override def mkDefaultCtor(t: CustomType) {

      txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}()")

      if (t.fields().nonEmpty) {
         txtBuffer.textln(" : ")
         for ((field, i) <- t.fields().zipWithIndex) {
            txtBuffer.tabs(2).textln(s"m_${field.name()}(${CppConstruction.defaultConstruct(field)}),")
         }

         for ((field, i) <- t.fields().zipWithIndex) {
            txtBuffer.tabs(2).text(s"${isSetName(field)}(false)")
            if (i + 1 < t.fields().size())
               txtBuffer.comma().endl()
         }
      }
      txtBuffer.tabs(0).textln(" {")
      txtBuffer.tabs(0).textln("}").endl()
   }

   override def mkRequiredMembersCtor(t: CustomType) {
      val reqAndOptFields = t.getAllFieldsInclSuper().toBuffer
      val reqFields = t.getAllFieldsInclSuper().filter(_.isRequired())
      if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
         txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}(")
         for (i <- 0 until reqFields.size) {
            val field = reqFields(i)
            val isLastField = i + 1 == reqFields.size
            txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
            if (!isLastField) {
               txtBuffer.comma().endl()
            }
         }
         txtBuffer.textln(") : ")

         val fieldsToSuper = reqFields -- t.fields
         if (fieldsToSuper.nonEmpty) {
            txtBuffer.tabs(2).text(s"${CppGenUtils.getSuperTypeString(t)}(")
            for (i <- 0 until fieldsToSuper.size) {
               val field = fieldsToSuper(i)
               val isLastField = i + 1 == fieldsToSuper.size
               txtBuffer.text(field.name())
               if (!isLastField) {
                  txtBuffer.text(", ")
               }
            }
            txtBuffer.text(")")
         }

         if (fieldsToSuper.nonEmpty && t.fields().nonEmpty)
            txtBuffer.textln(",")

         for (field <- t.fields()) {
            if (field.isRequired())
               txtBuffer.tabs(2).textln(s"m_${field.name()}(${field.name()}),")
            else
               txtBuffer.tabs(2).textln(s"m_${field.name()}(${CppConstruction.defaultConstruct(field)}),")
         }

         for ((field, i) <- t.fields().zipWithIndex) {
            if (field.isRequired())
               txtBuffer.tabs(2).text(s"${isSetName(field)}(true)")
            else
               txtBuffer.tabs(2).text(s"${isSetName(field)}(false)")
            if (i + 1 < t.fields().size()) {
               txtBuffer.comma().endl()
            }
         }
         txtBuffer.textln("{")
         txtBuffer.tabs(0).textln("}").endl()
      }
   }

   override def mkAllMembersAsArgsCtor(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()
      if (allFields.nonEmpty) {
         txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}(")
         for (i <- 0 until allFields.size()) {
            val field = allFields.get(i)
            val isLastField = i + 1 == allFields.size()
            txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
            if (!isLastField) {
               txtBuffer.comma().endl()
            }
         }
         txtBuffer.textln(") : ")

         val fieldsToSuper = allFields -- t.fields
         if (fieldsToSuper.nonEmpty) {
            txtBuffer.tabs(2).text(s"${CppGenUtils.getSuperTypeString(t)}(")
            for (i <- 0 until fieldsToSuper.size()) {
               val field = fieldsToSuper.get(i)
               val isLastField = i + 1 == fieldsToSuper.size()
               txtBuffer.text(field.name())
               if (!isLastField) {
                  txtBuffer.text(", ")
               }
            }
            txtBuffer.text(")")
         }

         if (t.fields().nonEmpty) {
            if (fieldsToSuper.nonEmpty) {
               txtBuffer.textln(",")
            }

            for ((field, i) <- t.fields().zipWithIndex) {
               txtBuffer.tabs(2).textln(s"m_${field.name()}(${field.name()}),")
            }

            for ((field, i) <- t.fields().zipWithIndex) {
               txtBuffer.tabs(2).text(s"${isSetName(field)}(true)")
               if (i + 1 < t.fields().size()) {
                  txtBuffer.comma().endl()
               }
            }

         }
         txtBuffer.tabs(0).textln(" {")
         txtBuffer.tabs(0).textln("}").endl()
      }
   }

   override def mkFieldBy16BitHash(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      txtBuffer.tabs(0).textln(s"const mgen::Field * ${t.shortName()}::_fieldBy16BitHash(const short hash) const {")
      txtBuffer.tabs(1).textln(s"switch (hash) {")
      for (field <- allFields) {
         txtBuffer.tabs(1).textln(s"case ${hash16(field)}:")
         txtBuffer.tabs(2).textln(s"return &${meta(field)};")
      }
      txtBuffer.tabs(1).textln(s"default:")
      txtBuffer.tabs(2).textln(s"return 0;");
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkFieldBy32BitHash(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      txtBuffer.tabs(0).textln(s"const mgen::Field * ${t.shortName()}::_fieldBy32BitHash(const int hash) const {")
      txtBuffer.tabs(1).textln(s"switch (hash) {")
      for (field <- allFields) {
         txtBuffer.tabs(1).textln(s"case ${hash32(field)}:")
         txtBuffer.tabs(2).textln(s"return &${meta(field)};")
      }
      txtBuffer.tabs(1).textln(s"default:")
      txtBuffer.tabs(2).textln(s"return 0;");
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkFieldByName(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      txtBuffer.tabs(0).textln(s"const mgen::Field * ${t.shortName()}::_fieldByName(const std::string& name) const {")
      txtBuffer.tabs(1).textln(s"return _fieldBy16BitHash(mgen::hash::calc16bit(name));")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkEquals(t: CustomType) {
      val allFields = t.getAllFieldsInclSuper()
      txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::operator==(const ${t.shortName()}& other) const {")
      txtBuffer.tabs(1).text("return true")
      for (field <- allFields) {
         txtBuffer.endl().tabs(2).text(s" && ${isFieldSet(field, "mgen::SHALLOW")} == other.${isFieldSet(field, "mgen::SHALLOW")}")
      }
      for (field <- allFields) {
         txtBuffer.endl().tabs(2).text(s" && ${get(field)} == other.${get(field)}")
      }
      txtBuffer.textln(";")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::_equals(const mgen::MGenBase& other) const {")
      txtBuffer.tabs(1).textln(s"return other._typeName() == _typeName() && ((${t.shortName()}&)other) == *this;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkDeepCopy(t: CustomType) {
      txtBuffer.tabs(0).textln(s"${t.shortName()} * ${t.shortName()}::_deepCopy() const {")
      txtBuffer.tabs(1).textln(s"return new ${t.shortName()}(*this);")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

   override def mkDestructor(t: CustomType) {
      txtBuffer.tabs(0).textln(s"${t.shortName()}::~${t.shortName()}() {")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

   override def mkGetters(t: CustomType) {

      for (field <- t.fields()) {
         txtBuffer.tabs(0).textln(s"const ${getTypeName(field)}& ${t.shortName()}::${get(field)} const {")
         txtBuffer.tabs(1).textln(s"return m_${field.name()};")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

      for (field <- t.fields()) {
         txtBuffer.tabs(0).textln(s"${getTypeName(field)}& ${t.shortName()}::${getMutable(field)} {")
         txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
         txtBuffer.tabs(1).textln(s"return m_${field.name()};")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

   }

   override def mkSetters(t: CustomType) {

      // By-value-setters with 'const Polymorphic<T>&' in
      for (field <- t.fields()) {
         txtBuffer
            .tabs(0)
            .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field)}& ${field.name()}")} {")
         txtBuffer.tabs(1).textln(s"m_${field.name()} = ${field.name()};")
         txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
         txtBuffer.tabs(1).textln(s"return *this;")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

      // By-value-setters with 'const T&' in
      for (field <- t.fields()) {
         if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
            txtBuffer
               .tabs(0)
               .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")} {")
            txtBuffer.tabs(1).textln(s"return ${set(field, s"${field.name()}._deepCopy(), true")};")
            txtBuffer.tabs(0).textln(s"}")
            txtBuffer.endl()
         }
      }

      for (field <- t.fields()) {
         if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
            txtBuffer
               .tabs(0)
               .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr")} {")
            txtBuffer.tabs(1).textln(s"m_${field.name()}.set(${field.name()}, managePtr);")
            txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
            txtBuffer.tabs(1).textln(s"return *this;")
            txtBuffer.tabs(0).textln(s"}")
            txtBuffer.endl()
         }
      }

      if (t.fields().nonEmpty)
         txtBuffer.endl()

   }

   override def mkNewInstance(t: CustomType) {
      txtBuffer.tabs(0).textln(s"mgen::MGenBase * ${t.shortName()}::_newInstance() {")
      txtBuffer.tabs(1).textln(s"return new ${t.shortName()};")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

   override def mkMetadataGetters(t: CustomType) {
      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_typeName() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_NAME();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const short ${t.shortName()}::_typeHash16bit() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASH_16BIT;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const int ${t.shortName()}::_typeHash32bit() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASH_32BIT;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<short>& ${t.shortName()}::_typeHashes16bit() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASHES_16BIT();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<int>& ${t.shortName()}::_typeHashes32bit() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASHES_32BIT();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_typeHash16bitBase64() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASH_16BIT_BASE64();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_typeHash32bitBase64() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASH_32BIT_BASE64();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_typeNames() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_NAMES();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_typeHashes16bitBase64() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASHES_16BIT_BASE64();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_typeHashes32bitBase64() const {")
      txtBuffer.tabs(1).textln(s"return _TYPE_HASHES_32BIT_BASE64();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<mgen::Field>& ${t.shortName()}::_fields() const {")
      txtBuffer.tabs(1).textln(s"return _FIELDS();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkMetaDataFields(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      // Own type data
      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_TYPE_NAME() {")
      txtBuffer.tabs(1).textln(s"static const std::string out = ${quote(t.fullName())};")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<short>& ${t.shortName()}::_TYPE_HASHES_16BIT() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<short> out = _TYPE_HASHES_16BIT_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<int>& ${t.shortName()}::_TYPE_HASHES_32BIT() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<int> out = _TYPE_HASHES_32BIT_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_TYPE_NAMES() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<std::string> out = _TYPE_NAMES_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_TYPE_HASHES_16BIT_BASE64() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<std::string> out = _TYPE_HASHES_16BIT_BASE64_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_TYPE_HASHES_32BIT_BASE64() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<std::string> out = _TYPE_HASHES_32BIT_BASE64_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_TYPE_HASH_16BIT_BASE64() {")
      txtBuffer.tabs(1).textln(s"static const std::string out = ${quote(t.typeHash16bitBase64String())};")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_TYPE_HASH_32BIT_BASE64() {")
      txtBuffer.tabs(1).textln(s"static const std::string out = ${quote(t.typeHash32bitBase64String())};")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      // Field type data
      txtBuffer.tabs(0).textln(s"const std::vector<mgen::Field>& ${t.shortName()}::_FIELDS() {")
      txtBuffer.tabs(1).textln(s"static const std::vector<mgen::Field> out = _FIELDS_make();")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      // Fields metadata implementation
      for (field <- t.fields()) {
         val enumString = field.typ().typeEnum().toString()

         txtBuffer.tabs(0).textln(s"const mgen::Field& ${t.shortName()}::${meta(field)} {")
         txtBuffer.tabs(1).textln(s"static const std::vector<std::string>& flags = ${meta(field, false)}_FLAGS_make();")
         txtBuffer.tabs(1).textln(
            s"static const mgen::Field out(${field.fieldHash16bit()}, ${field.fieldHash32bit()}, ${quote(field.name())}, mgen::Type(mgen::Type::ENUM_$enumString, mgen::Type::TAG_$enumString, flags), flags);")
         txtBuffer.tabs(1).textln(s"return out;")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

   }

   override def mkMetaDataFieldMakers(t: CustomType) {

      for (field <- t.getAllFieldsInclSuper()) {
         txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::${meta(field, false)}_FLAGS_make() {")
         txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
         for (tag <- field.flags())
            txtBuffer.tabs(1).textln(s"out.push_back(${quote(tag)});")
         txtBuffer.tabs(1).textln(s"return out;")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

      txtBuffer.tabs(0).textln(s"std::vector<short> ${t.shortName()}::_TYPE_HASHES_16BIT_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<short> out;")
      for (t <- t.typeHierarchy())
         txtBuffer.tabs(1).textln(s"out.push_back(${t.typeHash16bit()});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"std::vector<int> ${t.shortName()}::_TYPE_HASHES_32BIT_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<int> out;")
      for (t <- t.typeHierarchy())
         txtBuffer.tabs(1).textln(s"out.push_back(${t.typeHash32bit()});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::_TYPE_NAMES_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
      for (t <- t.typeHierarchy())
         txtBuffer.tabs(1).textln(s"out.push_back(${quote(t.fullName())});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::_TYPE_HASHES_16BIT_BASE64_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
      for (t <- t.typeHierarchy())
         txtBuffer.tabs(1).textln(s"out.push_back(${quote(t.typeHash16bitBase64String())});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::_TYPE_HASHES_32BIT_BASE64_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
      for (t <- t.typeHierarchy())
         txtBuffer.tabs(1).textln(s"out.push_back(${quote(t.typeHash32bitBase64String())});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(0).textln(s"std::vector<mgen::Field> ${t.shortName()}::_FIELDS_make() {");
      txtBuffer.tabs(1).textln(s"std::vector<mgen::Field> out;")
      for (field <- t.getAllFieldsInclSuper())
         txtBuffer.tabs(1).textln(s"out.push_back(${meta(field)});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   def quote(s: String): String = {
      '"' + s + '"'
   }

   override def mkSetFieldsSet(t: CustomType) {

      val fields = t.fields()
      val allFields = t.getAllFieldsInclSuper()

      for (field <- fields) {
         txtBuffer.tabs(0).textln(s"${t.shortName()}& ${t.shortName()}::${setFieldSet(field, "const bool state, const mgen::FieldSetDepth depth")} {")
         if (!field.typ().containsMgenCreatedType()) {
            txtBuffer.tabs(1).textln(s"${isSetName(field)} = state;")
         } else {
            txtBuffer.tabs(1).textln(s"${isSetName(field)} = state;")
            txtBuffer.tabs(1).textln(s"if (depth == mgen::DEEP)")
            txtBuffer.tabs(2).textln(s"mgen::validation::setFieldSetDeep(m_${field.name()});")
         }
         txtBuffer.tabs(1).textln(s"return *this;")
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

      txtBuffer.tabs(0).textln(s"${t.shortName()}& ${t.shortName()}::_setAllFieldsSet(const bool state, const mgen::FieldSetDepth depth) { ")
      for (field <- allFields)
         txtBuffer.tabs(2).textln(s"${setFieldSet(field, "state, depth")};")
      txtBuffer.tabs(1).textln(s"return *this;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

   }

   override def mkNFieldsSet(t: CustomType) {
      val allFields = t.getAllFieldsInclSuper()
      txtBuffer.tabs(0).textln(s"int ${t.shortName()}::_nFieldsSet(const mgen::FieldSetDepth depth) const {")
      txtBuffer.tabs(1).textln(s"int out = 0;")
      for (field <- allFields)
         txtBuffer.tabs(1).textln(s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln("}").endl()
   }

   override def mkValidate(t: CustomType) {
      txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::_validate(const mgen::FieldSetDepth depth) const { ")
      txtBuffer.tabs(1).textln(s"if (depth == mgen::SHALLOW) {")
      txtBuffer.tabs(2).text(s"return true")
      for (field <- t.getAllFieldsInclSuper().filter(_.isRequired()))
         txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, "mgen::SHALLOW")}")
      txtBuffer.textln(s";")
      txtBuffer.tabs(1).textln(s"} else {")
      txtBuffer.tabs(2).text(s"return true")
      for (field <- t.getAllFieldsInclSuper()) {
         if (field.isRequired())
            txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, "mgen::DEEP")}")
         else if (field.typ().containsMgenCreatedType())
            txtBuffer.endl().tabs(4).text(s"&& (!${isFieldSet(field, "mgen::SHALLOW")} || ${isFieldSet(field, "mgen::DEEP")})")
      }
      txtBuffer.textln(s";")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

   override def mkFieldStatusGetters(t: CustomType) {

      txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::_isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const {")
      txtBuffer.tabs(1).textln(s"switch(field.hash16bit()) {")
      for (field <- t.getAllFieldsInclSuper()) {
         txtBuffer.tabs(2).textln(s"case (${hash16(field)}):")
         txtBuffer.tabs(3).textln(s"return ${isFieldSet(field, "depth")};")
      }
      txtBuffer.tabs(2).textln(s"default:")
      txtBuffer.tabs(3).textln(s"return false;")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()

      for (field <- t.fields()) {
         txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::${isFieldSet(field, "const mgen::FieldSetDepth depth")} const {")
         if (field.typ().containsMgenCreatedType()) {
            txtBuffer.tabs(1).textln(s"if (depth == mgen::SHALLOW) {")
            txtBuffer.tabs(2).textln(s"return ${isSetName(field)};")
            txtBuffer.tabs(1).textln(s"} else {")
            txtBuffer.tabs(2).textln(s"return ${isSetName(field)} && mgen::validation::validateFieldDeep(${get(field)});")
            txtBuffer.tabs(1).textln(s"}")
         } else {
            txtBuffer.tabs(1).textln(s"return ${isSetName(field)};")
         }
         txtBuffer.tabs(0).textln(s"}")
         txtBuffer.endl()
      }

   }

}