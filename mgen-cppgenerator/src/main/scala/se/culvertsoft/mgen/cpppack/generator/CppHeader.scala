package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet

import CppTypeNames.getTypeName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.TypeEnum

object CppHeader extends CppSrcFileOrHeader(".h") {
   import CppTypeNames._

   override def mkIncludes(t: CustomType) {
      val types = t.getDirectDependencies() - t
      if (t.superType().typeEnum() == TypeEnum.MGEN_BASE)
         CppGenUtils.include("mgen/classes/MGenBase.h")
      for (tRef <- types)
         CppGenUtils.include(tRef)
      txtBuffer.endl()
   }

   override def mkIncludeGuardStart(module: Module, t: CustomType) {
      CppGenUtils.mkIncludeGuardStart(t.fullName())
   }

   override def mkIncludeGuardEnd() {
      CppGenUtils.mkIncludeGuardEnd()
   }

   override def mkEqOperator(t: CustomType) {
      txtBuffer.tabs(1).textln(s"bool operator==(const ${t.shortName()}& other) const;")
      txtBuffer.endl()
   }

   override def mkClassStart(t: CustomType) {
      CppGenUtils.mkClassStart(t.shortName(), getSuperTypeNameString(t))
   }

   override def mkPrivate() {
      txtBuffer.textln("private:")
   }

   override def mkPublic() {
      txtBuffer.textln("public:")
   }

   override def mkDefaultCtor(t: CustomType) {
      txtBuffer.tabs(1).textln(s"${t.shortName()}();")
   }

   override def mkAllMembersAsArgsCtor(t: CustomType) {
      val allFields = t.getAllFieldsInclSuper();
      if (allFields.nonEmpty) {
         txtBuffer.tabs(1).text(s"${t.name()}(")
         for (i <- 0 until allFields.size()) {
            val field = allFields.get(i)
            val isLastField = i + 1 == allFields.size()
            txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
            if (!isLastField) {
               txtBuffer.comma().endl()
            }
         }
         txtBuffer.textln(");")
      }
   }

   override def mkDestructor(t: CustomType) {
      txtBuffer.tabs(1).textln(s"virtual ~${t.shortName()}();")
      txtBuffer.endl()
   }

   override def mkGetters(t: CustomType) {

      for (field <- t.fields())
         txtBuffer
            .tabs(1)
            .textln(s"const ${getTypeName(field)}& ${get(field)} const;")

      if (t.fields().nonEmpty)
         txtBuffer.endl()

      for (field <- t.fields())
         txtBuffer
            .tabs(1)
            .textln(s"${getTypeName(field)}& ${getMutable(field)};")

      if (t.fields().nonEmpty)
         txtBuffer.endl()

   }

   override def mkSetters(t: CustomType) {

      for (field <- t.fields()) {

         // By-value-setters with 'const Polymorphic<T>&' in
         txtBuffer
            .tabs(1)
            .textln(s"${t.shortName()}& ${set(field, s"const ${getTypeName(field)}& ${field.name()}")};")

         // By-value-setters with 'const T&' in
         if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
            txtBuffer
               .tabs(1)
               .textln(s"${t.shortName()}& ${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")};")
         }

         // By-reference-setters with 'T *' in
         if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
            txtBuffer
               .tabs(1)
               .textln(s"${t.shortName()}& ${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr = true")};")
         }

      }

      if (t.fields().nonEmpty)
         txtBuffer.endl()

   }

   override def mkMembers(t: CustomType) {
      for (field <- t.fields())
         txtBuffer
            .tabs(1)
            .textln(s"${getTypeName(field)} m_${field.name()};")

      for (field <- t.fields())
         txtBuffer.tabs(1).textln(s"bool ${isSetName(field)};")

      if (t.fields().nonEmpty)
         txtBuffer.endl()
   }

   override def mkEquals(t: CustomType) {
      txtBuffer.tabs(1).textln(s"bool _equals(const mgen::MGenBase& other) const;")
      txtBuffer.endl()
   }

   override def mkMetaDataFields(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      // Own type data
      txtBuffer.tabs(1).textln("static const std::string& _TYPE_NAME();")
      txtBuffer.tabs(1).textln("static const std::vector<short>& _TYPE_HASHES_16BIT();")
      txtBuffer.tabs(1).textln("static const std::vector<std::string>& _TYPE_NAMES();")
      txtBuffer.tabs(1).textln("static const std::vector<std::string>& _TYPE_HASHES_16BIT_BASE64();")
      txtBuffer.endl()
      txtBuffer.tabs(1).textln(s"enum _TYPE_HASHES {")
      txtBuffer.tabs(2).textln(s"_TYPE_HASH_16BIT = ${t.typeHash16bit()},")
      txtBuffer.tabs(1).textln(s"};")
      txtBuffer.endl()
      txtBuffer.tabs(1).textln("static const std::string& _TYPE_HASH_16BIT_BASE64();")
      txtBuffer.endl()

      // Field type data
      txtBuffer.tabs(1).textln(s"static const std::vector<mgen::Field>& _FIELDS();")
      txtBuffer.endl()

      for (field <- t.fields())
         txtBuffer.tabs(1).textln(s"static const mgen::Field& ${meta(field)};")
      txtBuffer.endl()

      // 16 bit hashes
      txtBuffer.tabs(1).textln(s"enum _TYPE_HASHES_16BIT {")
      for ((field, i) <- allFields.zipWithIndex) {
         txtBuffer.tabs(2).text(s"${hash16(field)} = ${field.fieldHash16bit()}")
         txtBuffer.textln(if (i + 1 == allFields.size) "" else ",")
      }
      txtBuffer.tabs(1).textln(s"};")
      txtBuffer.endl()

   }

   override def mkReadField(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      txtBuffer.tabs(1).textln(s"template<typename ReaderType, typename ReadContextType>")
      txtBuffer.tabs(1).textln(s"void _readField(const short fieldId, ReadContextType& context, ReaderType& reader) {")
      txtBuffer.tabs(2).textln(s"switch (fieldId) {")
      for (field <- allFields) {
         txtBuffer.tabs(2).textln(s"case ${hash16(field)}:")
         txtBuffer.tabs(3).textln(s"reader.readField(${meta(field)}, context, ${getMutable(field)});")
         txtBuffer.tabs(3).textln(s"break;")
      }
      txtBuffer.tabs(2).textln(s"default:")
      txtBuffer.tabs(3).textln(s"reader.handleUnknownField(fieldId, context);");
      txtBuffer.tabs(2).textln(s"}")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()

   }

   override def mkAcceptVisitor(t: CustomType) {

      val allFields = t.getAllFieldsInclSuper()

      txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
      txtBuffer.tabs(1).textln(s"void _accept(VisitorType& visitor) const {")
      txtBuffer.tabs(2).textln(s"visitor.beginVisit(*this, _nFieldsSet(mgen::SHALLOW));")
      for (field <- allFields) {
         txtBuffer.tabs(2).textln(s"visitor.visit(${get(field)}, ${meta(field)}, ${isFieldSet(field, "mgen::SHALLOW")});")
      }
      txtBuffer.tabs(2).textln(s"visitor.endVisit();")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()

      txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
      txtBuffer.tabs(1).textln(s"void _accept(VisitorType& visitor) {")
      txtBuffer.tabs(2).textln(s"visitor.beginVisit(*this, _nFieldsSet(mgen::SHALLOW));")
      for (field <- allFields) {
         txtBuffer.tabs(2).textln(s"visitor.visit(${getMutable(field)}, ${meta(field)}, ${isFieldSet(field, "mgen::SHALLOW")});")
      }
      txtBuffer.tabs(2).textln(s"visitor.endVisit();")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()

   }

   override def mkFieldBy16BitHash(t: CustomType) {
      txtBuffer.tabs(1).textln(s"const mgen::Field * _fieldBy16BitHash(const short hash) const;")
   }

   override def mkFieldByName(t: CustomType) {
      txtBuffer.tabs(1).textln(s"const mgen::Field * _fieldByName(const std::string& name) const;")
      txtBuffer.endl()
   }

   override def mkMetadataGetters(t: CustomType) {
      txtBuffer.tabs(1).textln(s"const std::string& _typeName() const;")
      txtBuffer.tabs(1).textln(s"const short _typeHash16bit() const;")
      txtBuffer.tabs(1).textln(s"const std::string& _typeHash16bitBase64() const;")
      txtBuffer.endl()
      txtBuffer.tabs(1).textln(s"const std::vector<short>& _typeHashes16bit() const;")
      txtBuffer.tabs(1).textln(s"const std::vector<std::string>& _typeNames() const;")
      txtBuffer.tabs(1).textln(s"const std::vector<std::string>& _typeHashes16bitBase64() const;")
      txtBuffer.endl()

      txtBuffer.tabs(1).textln(s"const std::vector<mgen::Field>& _fields() const;")
      txtBuffer.endl()

      txtBuffer.tabs(1).textln(s"bool _isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const;")
      txtBuffer.endl()

   }

   override def mkNewInstance(t: CustomType) {
      txtBuffer.tabs(1).textln(s"static mgen::MGenBase * _newInstance();")
   }

   override def mkDeepCopy(t: CustomType) {
      txtBuffer.tabs(1).textln(s"${t.shortName()} * _deepCopy() const;")
   }

   override def mkUsingStatements(t: CustomType) {
      txtBuffer.tabs(0).textln(s"using mgen::Polymorphic;")
      txtBuffer.endl()
   }

   override def mkMetaDataFieldMakers(t: CustomType) {
      for (field <- t.getAllFieldsInclSuper())
         txtBuffer.tabs(1).textln(s"static std::vector<std::string> ${meta(field, false)}_FLAGS_make();")
      txtBuffer.tabs(1).textln("static std::vector<short> _TYPE_HASHES_16BIT_make();");
      txtBuffer.tabs(1).textln("static std::vector<std::string> _TYPE_NAMES_make();");
      txtBuffer.tabs(1).textln("static std::vector<std::string> _TYPE_HASHES_16BIT_BASE64_make();");
      txtBuffer.tabs(1).textln("static std::vector<mgen::Field> _FIELDS_make();");
      txtBuffer.endl()

   }

   override def mkSetFieldsSet(t: CustomType) {

      val fields = t.fields()
      val allFields = t.getAllFieldsInclSuper()

      for (field <- fields)
         txtBuffer.tabs(1).textln(s"${t.shortName()}& ${setFieldSet(field, "const bool state, const mgen::FieldSetDepth depth")};")
      if (fields.nonEmpty)
         txtBuffer.endl()

      txtBuffer.tabs(1).textln(s"${t.shortName()}& _setAllFieldsSet(const bool state, const mgen::FieldSetDepth depth);")
      txtBuffer.endl()

   }

   override def mkValidate(t: CustomType) {
      txtBuffer.tabs(1).textln(s"bool _validate(const mgen::FieldSetDepth depth) const;")
   }

   override def mkRequiredMembersCtor(t: CustomType) {

      val reqAndOptFields = t.getAllFieldsInclSuper().toBuffer
      val reqFields = t.getAllFieldsInclSuper().filter(_.isRequired())

      if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
         txtBuffer.tabs(1).text(s"${t.name()}(")
         for (i <- 0 until reqFields.size) {
            val field = reqFields(i)
            val isLastField = i + 1 == reqFields.size
            txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
            if (!isLastField) {
               txtBuffer.comma().endl()
            }
         }
         txtBuffer.textln(");")
      }
   }

   override def mkNFieldsSet(t: CustomType) {
      txtBuffer.tabs(1).textln(s"int _nFieldsSet(const mgen::FieldSetDepth depth) const;")
      txtBuffer.endl()
   }

   override def mkClassEnd(t: CustomType) {
      CppGenUtils.mkClassEnd(t.shortName())
   }

   override def mkFieldStatusGetters(t: CustomType) {

      for (field <- t.fields())
         txtBuffer.tabs(1).textln(s"bool ${isFieldSet(field, "const mgen::FieldSetDepth depth")} const;")

      if (t.fields().nonEmpty)
         txtBuffer.endl()

   }

}