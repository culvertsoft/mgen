package se.culvertsoft.mgen.jspack.generator

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.ListOrArrayType
import se.culvertsoft.mgen.api.model.MGenBaseType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

class JavascriptGenerator extends Generator {

   var currentModule: Module = null
   val txtBuffer = new SuperStringBuffer

   override def generate(modules: java.util.List[Module], settings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {
      val out = new java.util.ArrayList[GeneratedSourceFile]

      val folder = settings.getOrElse("output_path", "")
      val fileName = settings.getOrElse("output_filename", "mGen.js")
      val classregistryName = settings.getOrElse("classregistry_name", "mgen_registry")
      val sourceCode = "";

      txtBuffer.clear().setTabString("\t")
      mkIntro(classregistryName);
      for (module <- modules) generateModule(module)
      mkOutro(classregistryName);
      out += new GeneratedSourceFile(folder + fileName, txtBuffer.toString())
      out
   }

   def generateModule(module: Module) {
      for ((typeName, t) <- module.types()) generateClassSourceCode(t)
   }

   def mkIntro(classregistryName: String) {
       txtBuffer.text("/* Autogenerated code by MGen for JavaScript */").endl2()
       txtBuffer.text("/*jshint -W069 */ // Turn of lint warnings for accessing a property using a string literal with square bracket notation.").endl2()
       txtBuffer.text("var " + classregistryName + " = {}; // this variable name can be changed with the setting: classregistry_name").endl2()
       txtBuffer.text("(function(registry){").endl()
       txtBuffer.tabs(1).text("\"use strict\";").endl2()
       txtBuffer.tabs(1).text("registry.classRegistry = {};").endl()
       txtBuffer.tabs(1).text("registry.hashRegistry = {};").endl2()
   }

   
   def mkOutro(classregistryName: String) {
       txtBuffer.text("})(" + classregistryName + ");").endl()
   }

   
   def getTypeName(typ: Type): String = {
      typ.typeEnum() match {
         case TypeEnum.BOOL => "boolean"
         case TypeEnum.INT8 => "int8"
         case TypeEnum.INT16 => "int16"
         case TypeEnum.INT32 => "int32"
         case TypeEnum.INT64 => "int64"
         case TypeEnum.FLOAT32 => "float32"
         case TypeEnum.FLOAT64 => "float64"
         case TypeEnum.STRING => "string"
         case TypeEnum.MAP =>
            val t = typ.asInstanceOf[MapType]
            s"map:${getTypeName(t.keyType())}:${getTypeName(t.valueType())}"
         case TypeEnum.LIST | TypeEnum.ARRAY =>
            val t = typ.asInstanceOf[ListOrArrayType]
            s"list:${getTypeName(t.elementType())}"
         case TypeEnum.CUSTOM =>
            val t = typ.asInstanceOf[CustomType]
            t.fullName()
         case TypeEnum.MGEN_BASE => MGenBaseType.INSTANCE.fullName()
         case x => throw new GenerationException(s"Don't know how to handle type $x")
      }
   }

   def generateClassSourceCode(t: CustomType) {
      mkStart(t)
      mkmgType(t)
      mkMembers(t)
      mkEnd(t)
   }

   def mkStart(t: CustomType) {
      txtBuffer.tabs(1).text("registry.classRegistry[\"" + t.fullName + "\"] = ").braceBegin().endl()
   }

   def mkmgType(t: CustomType) {
      var hashes = t.superTypeHierarchy().map(x => "\"" + x.typeId16BitBase64().toString() + "\"").mkString(", ")
      txtBuffer.tabs(2).text("\"mGenTypeHash\": [" + hashes + "],").endl()
   }

   def mkMembers(t: CustomType) {
      txtBuffer.text(t.getAllFieldsInclSuper().map(field => {
         "\t\t\"" + field.name + "\": {\n" +
            "\t\t\t\"flags\": [" + field.flags().map(x => "\"" + x + "\"").mkString(",") + "],\n" +
            "\t\t\t\"type\": \"" + getTypeName(field.typ()) + "\",\n" +
            "\t\t\t\"hash\": \"" + field.idBase64() + "\"\n" +
            "\t\t}"
      }).mkString(",\n").+("\n"))
   }
   
   def mkEnd(t: CustomType) {
      txtBuffer.tabs(1).braceEnd().text(";").endl2()
   }
}