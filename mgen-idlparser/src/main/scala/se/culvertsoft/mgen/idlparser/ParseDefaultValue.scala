package se.culvertsoft.mgen.idlparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ItemLookup
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.ListOrArrayType
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.PrimitiveType
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type

object ParseDefaultValue {

  /**
   * Intended to be used by the compiler during the type linkage phase to
   * parse default value strings.
   */
  def apply(
    expectedType: Type,
    writtenString: String,
    referencedFrom: ClassType)(implicit lkup: ItemLookup): DefaultValue = {

    if (writtenString == null || writtenString.trim == "null")
      return null

    expectedType match {
      case expectedType: EnumType =>
        mkEnum(writtenString, expectedType, referencedFrom)
      case expectedType: BoolType =>
        mkBool(writtenString, referencedFrom)
      case expectedType: StringType =>
        mkString(writtenString, referencedFrom)
      case expectedType: PrimitiveType =>
        mkNumber(writtenString, expectedType, referencedFrom)
      case expectedType: ListOrArrayType =>
        mkListOrArray(expectedType, writtenString, referencedFrom)
      case expectedType: MapType =>
        mkMap(expectedType, writtenString, referencedFrom)
      case expectedType: ClassType =>
        mkObject(expectedType, writtenString, referencedFrom)
      case _ =>
        throw new AnalysisException("Unexpected field type provided to ParseDefaultValue(..): " + expectedType)
    }
  }

  def mkBool(txt: String, referencedFrom: ClassType): BoolDefaultValue = {
    new BoolDefaultValue(txt.toBoolean, referencedFrom)
  }

  def mkEnum(txt: String, typ: EnumType, referencedFrom: ClassType)(implicit lkup: ItemLookup): EnumDefaultValue = {
    val entry =
      typ.entries
        .find(_.name == txt.trim)
        .getOrElse(throw new AnalysisException(s"Don't know any enum value named $txt for enum type $typ"))

    new EnumDefaultValue(typ, entry, referencedFrom)
  }

  def mkString(txt: String, referencedFrom: ClassType): StringDefaultValue = {
    new StringDefaultValue(txt, referencedFrom)
  }

  def mkNumber(txt: String, expectedType: PrimitiveType, referencedFrom: ClassType): NumericDefaultValue = {

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
    new NumericDefaultValue(expectedType, v, referencedFrom)
  }

  def mkListOrArray(
    typ: ListOrArrayType,
    writtenString: String,
    referencedFrom: ClassType)(implicit lkup: ItemLookup): ListOrArrayDefaultValue = {
    val items = new java.util.ArrayList[DefaultValue]
    try {
      val src = JSONValue.parseWithException(writtenString);
      src match {
        case src: JSONArray =>
          for (o <- src.toArray()) {
            items.add(apply(
              typ.elementType,
              getString(o),
              referencedFrom));
          }
          new ListOrArrayDefaultValue(typ, items, referencedFrom)
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
    referencedFrom: ClassType)(implicit lkup: ItemLookup): MapDefaultValue = {
    val items = new java.util.LinkedHashMap[DefaultValue, DefaultValue];
    try {
      val src = JSONValue.parseWithException(writtenString);
      src match {
        case src: JSONObject =>
          for (e <- src.entrySet()) {
            val key = apply(typ.keyType, getString(e.getKey), referencedFrom);
            val value = apply(typ.valueType, getString(e.getValue), referencedFrom);
            items.put(key, value);
          }
          new MapDefaultValue(typ, items, referencedFrom)
        case _ =>
          throw new AnalysisException("Failed to parse default value '" + writtenString
            + "' as a JSON object.");
      }
    } catch {
      case e: Exception => throw new AnalysisException(e);
    }
  }

  def mkObject(
    expectedType: ClassType,
    writtenString: String,
    referencedFrom: ClassType)(implicit lkup: ItemLookup): ObjectDefaultValue = {

    val ovrdDefValues = new java.util.LinkedHashMap[Field, DefaultValue]();
    var actualType: ClassType = null

    try {

      val src = JSONValue.parseWithException(writtenString);
      src match {
        case jsonObject: JSONObject =>

          val optActualTypeName = String.valueOf(jsonObject.get("__TYPE"));

          if (optActualTypeName != "null") {

            actualType = lkup.getType(optActualTypeName, referencedFrom).asInstanceOf[ClassType];

            if (actualType == null) {
              throw new AnalysisException("Could not find specified default value type "
                + optActualTypeName + " for expected type " + expectedType);
            }

            if (expectedType != actualType
              && !actualType.typeHierarchy().contains(expectedType)) {
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
              .toString(), referencedFrom);
            ovrdDefValues.put(f, value);
          }

          new ObjectDefaultValue(expectedType, actualType, ovrdDefValues, referencedFrom)

        case _ =>
          throw new AnalysisException("Failed to parse default value '" + writtenString
            + "' as a JSON object.");
      }

    } catch {
      case e: Exception => throw new AnalysisException(e);
    }

  }

  def getString(o: Any): String = {
    if (o != null) o.toString() else null
  }

}