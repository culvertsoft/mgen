package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.ListOrArrayType
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.PrimitiveType
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.impl.BoolDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.EnumDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.ListOrArrayDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.MapDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.NumericDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.ObjectDefaultValueImpl
import se.culvertsoft.mgen.api.model.impl.StringDefaultValueImpl

object ParseDefaultValue {

  /**
   * Intended to be used by the compiler during the type linkage phase to
   * parse default value strings.
   */
  def apply(
    expectedType: Type,
    writtenString: String,
    currentModule: Module): DefaultValue = {

    if (writtenString == null || writtenString.trim == "null")
      return null

    expectedType match {
      case expectedType: EnumType =>
        mkEnum(writtenString, expectedType, currentModule)
      case expectedType: BoolType =>
        mkBool(writtenString)
      case expectedType: StringType =>
        mkString(writtenString)
      case expectedType: PrimitiveType =>
        mkNumber(writtenString, expectedType)
      case expectedType: ListOrArrayType =>
        mkListOrArray(expectedType, writtenString, currentModule)
      case expectedType: MapType =>
        mkMap(expectedType, writtenString, currentModule)
      case expectedType: CustomType if (expectedType.isLinked) =>
        mkObject(expectedType, writtenString, currentModule)
      case _ =>
        throw new AnalysisException("Unexpected type enum: " + expectedType.typeEnum())
    }
  }

  def mkBool(txt: String): BoolDefaultValue = {
    new BoolDefaultValueImpl(txt.toBoolean)
  }

  def mkEnum(txt: String, typ: EnumType, module: Module): EnumDefaultValue = {
    val entry =
      typ.entries
        .find(_.name == txt.trim)
        .getOrElse(throw new AnalysisException(s"Don't know any enum value named $txt for enum type $typ"))

    new EnumDefaultValueImpl(typ, entry, module)
  }

  def mkString(txt: String): StringDefaultValue = {
    new StringDefaultValueImpl(txt)
  }

  def mkNumber(txt: String, expectedType: PrimitiveType): NumericDefaultValue = {

    var v: java.lang.Number = null;
    try {
      v = java.lang.Long.decode(txt);
    } catch {
      case e1: NumberFormatException =>
        try {
          v = java.lang.Double.parseDouble(txt);
        } catch {
          case e2: NumberFormatException =>
            throw new AnalysisException("Failed to parse default value number from "
              + txt, e2);
        }
    }
    new NumericDefaultValueImpl(expectedType, v)
  }

  def mkListOrArray(
    typ: ListOrArrayType,
    writtenString: String,
    currentModule: Module): ListOrArrayDefaultValue = {
    val items = new java.util.ArrayList[DefaultValue]
    try {
      val src = JSONValue.parseWithException(writtenString);
      src match {
        case src: JSONArray =>
          for (o <- src.toArray()) {
            items.add(apply(
              typ.elementType,
              getString(o),
              currentModule));
          }
          new ListOrArrayDefaultValueImpl(typ, items)
        case _ =>
          throw new AnalysisException("Failed to parse default value '" + writtenString
            + "' as a JSON array.");
      }
    } catch {
      case e: Exception => throw new AnalysisException(e);
    }
  }

  def mkMap(
    typ: MapType,
    writtenString: String,
    currentModule: Module): MapDefaultValue = {
    val items = new java.util.LinkedHashMap[DefaultValue, DefaultValue];
    try {
      val src = JSONValue.parseWithException(writtenString);
      src match {
        case src: JSONObject =>
          for (e <- src.entrySet()) {
            val key = apply(typ.keyType, getString(e.getKey), currentModule);
            val value = apply(typ.valueType, getString(e.getValue), currentModule);
            items.put(key, value);
          }
          new MapDefaultValueImpl(typ, items)
        case _ =>
          throw new AnalysisException("Failed to parse default value '" + writtenString
            + "' as a JSON object.");
      }
    } catch {
      case e: Exception => throw new AnalysisException(e);
    }
  }

  def mkObject(
    expectedType: CustomType,
    writtenString: String,
    currentModule: Module): ObjectDefaultValue = {

    val ovrdDefValues = new java.util.LinkedHashMap[Field, DefaultValue]();
    var actualType: CustomType = null

    try {

      val src = JSONValue.parseWithException(writtenString);
      src match {
        case jsonObject: JSONObject =>

          val optActualTypeName = String.valueOf(jsonObject.get("__TYPE"));

          if (optActualTypeName != "null") {

            actualType = findType(optActualTypeName, currentModule);

            if (actualType == null) {
              throw new AnalysisException("Could not find specified default value type "
                + optActualTypeName + " for expected type " + expectedType);
            }

            if (expectedType != actualType
              && !actualType.superTypeHierarchy().contains(expectedType)) {
              throw new AnalysisException(
                "Specified default value type "
                  + optActualTypeName
                  + " does not qualify for defaultvalue. It is not the same type or a subtype of "
                  + expectedType);
            }
            jsonObject.remove("__TYPE");
          } else {
            actualType = expectedType;
          }

          for (e <- jsonObject.entrySet()) {
            val fieldName = getString(e.getKey());

            val f = actualType.findField(fieldName);
            if (f == null) {
              throw new AnalysisException("Failed to set default value. No field named "
                + fieldName + " was found on type " + actualType);
            }

            val value = apply(f.typ(), e
              .getValue()
              .toString(), currentModule);
            ovrdDefValues.put(f, value);
          }

          new ObjectDefaultValueImpl(expectedType, actualType, currentModule, ovrdDefValues)

        case _ =>
          throw new AnalysisException("Failed to parse default value '" + writtenString
            + "' as a JSON object.");
      }

    } catch {
      case e: Exception => throw new AnalysisException(e);
    }

  }

  def findType(name: String, currentModule: Module): CustomType = {
    val t = currentModule.findType(name).asInstanceOf[CustomType]
    if (t != null) t else currentModule.parent().findType(name).asInstanceOf[CustomType]
  }

  def getString(o: Any): String = {
    if (o != null) o.toString() else null
  }

}