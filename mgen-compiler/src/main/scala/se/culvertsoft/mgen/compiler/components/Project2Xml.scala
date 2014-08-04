package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap
import scala.xml.PrettyPrinter
import scala.xml.Text
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.EnumEntry
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.GeneratorDescriptor
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.impl.GeneratedSourceFileImpl
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.idlparser.IdlParser

object Project2Xml {

  case class XmlSourceFile(path: String, xml: scala.xml.Node)

  def apply(project: Project): Seq[GeneratedSourceFile] = {

    val projectXml =
      <Project>
        { project.generators map generator2xml }
        { project.dependencies map dependency2xmlReference }
        <Sources parser={ s"${classOf[IdlParser].getName}" }>
          { project.modules.filter(_.types.nonEmpty) map { x => <Source>{ x.filePath }</Source> } }
        </Sources>
      </Project>

    val sources = Nil ++
      Seq(XmlSourceFile(project.absoluteFilePath, projectXml)) ++
      (project.modules.filter(_.types.nonEmpty) map module2xmlSource)

    convert(sources)

  }

  def convert(sources: Seq[XmlSourceFile]): Seq[GeneratedSourceFile] = {

    val printer = new PrettyPrinter(120, 4)

    sources.map { source =>

      val sourceCode = printer.format(source.xml)
      new GeneratedSourceFileImpl(source.path, sourceCode)

    }

  }

  def generator2xml(generator: GeneratorDescriptor): Seq[scala.xml.Node] = {
    <Generator name={ generator.getGeneratorName }>
      <class_path>{ generator.getGeneratorClassPath }</class_path>
      <output_path>{ generator.getGeneratorSettings().get("output_path") }</output_path>
      <classregistry_path>{ generator.getGeneratorSettings().get("classregistry_path") }</classregistry_path>
    </Generator>
  }

  def dependency2xmlReference(dependency: Project): scala.xml.Node = {
    <Depend>{ dependency.filePath }</Depend>
  }

  def module2xmlSource(module: Module): XmlSourceFile = {

    implicit val _module = module

    val xml =
      <Module>
        <Enums>
          { module.enums map enum2xml }
        </Enums>
        <Types>
          { module.types map type2xml }
        </Types>
      </Module>

    XmlSourceFile(module.absoluteFilePath(), xml)
  }

  def enum2xml(typ: EnumType)(implicit currentModule: Module): scala.xml.Node = {
    <EnumType>{ typ.entries map enumentry2xml }</EnumType>.copy(label = typ.shortName)
  }

  def enumentry2xml(entry: EnumEntry)(implicit currentModule: Module): scala.xml.Node = {
    <entry>{ entry.constant } </entry>.copy(label = entry.name)
  }

  def type2xml(typ: CustomType)(implicit currentModule: Module): scala.xml.Node = {

    val autoId = CRC16.calc(typ.fullName)
    val idString = if (typ.typeId16Bit != autoId) typ.typeId16Bit.toString else null

    val xml =
      (if (typ.hasSuperType())
        <CustomType extends={ type2string(typ.superType()) } id={ idString }>{ typ.fields map field2xml } </CustomType>
      else
        <CustomType id={ idString }>{ typ.fields map field2xml } </CustomType>)
        .copy(label = typ.name)

    xml

  }

  def type2string(t: Type)(implicit currentModule: Module): String = {
    t match {
      case t: BoolType => "bool"
      case t: Int8Type => "int8"
      case t: Int16Type => "int16"
      case t: Int32Type => "int32"
      case t: Int64Type => "int64"
      case t: Float32Type => "float32"
      case t: Float64Type => "float64"
      case t: StringType => "string"
      case t: ListType => s"list[${type2string(t.elementType)}]"
      case t: ArrayType => s"array[${type2string(t.elementType)}]"
      case t: MapType => s"map[${type2string(t.keyType)}, ${type2string(t.valueType)}]"
      case t: CustomType => if (t.module == currentModule) t.shortName else t.fullName
    }
  }

  def field2xml(field: Field)(implicit currentModule: Module): scala.xml.Node = {

    val flags = field.flags().map(_.trim).filter(_.nonEmpty)
    val typeString = type2string(field.typ)
    val flagsString = s"${flags.mkString(", ")}"
    val idString = if (field.hasIdOverride()) field.id().toString else null

    val xmlT =
      (if (flags.nonEmpty)
        <fieldname type={ typeString } flags={ flagsString } id={ idString }/>
      else
        <fieldname type={ typeString } id={ idString }/>)

    val xml =
      if (field.hasDefaultValue) {
        xmlT.copy(label = field.name, child = Text(defaultVal2String(field.defaultValue)))
      } else {
        xmlT.copy(label = field.name)
      }

    xml
  }

  def defaultVal2String(v: DefaultValue): String = {

    if (v == null)
      return null

    v match {
      case v: EnumDefaultValue => getQuotedStringOrNull(v.value.name)
      case v: BoolDefaultValue => getString(v.value)
      case v: StringDefaultValue => getQuotedStringOrNull(v.value)
      case v: NumericDefaultValue =>
        v.expectedType match {
          case _: Int8Type => getString(v.fixedPtValue)
          case _: Int16Type => getString(v.fixedPtValue)
          case _: Int32Type => getString(v.fixedPtValue)
          case _: Int64Type => getString(v.fixedPtValue)
          case _: Float32Type => getString(v.floatingPtValue)
          case _: Float64Type => getString(v.floatingPtValue)
        }
      case v: ListOrArrayDefaultValue => s"[${v.values.map(defaultVal2String).mkString(", ")}]"
      case v: MapDefaultValue =>
        val entries = v.values.map(e => (getQuotedStringOrNull(defaultVal2String(e._1)), defaultVal2String(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")
        s"{$entriesString}"

      case v: ObjectDefaultValue =>
        val entries = v.overriddenDefaultValues.map(e => (getQuotedStringOrNull(e._1.name), defaultVal2String(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")

        if (v.isDefaultTypeOverriden) {
          if (v.isCurrentModule) {
            s"{ ${quote("__TYPE")}: ${quote(v.actualType.shortName)}, $entriesString}"
          } else {
            s"{ ${quote("__TYPE")}: ${quote(v.actualType.fullName)}, $entriesString}"
          }
        } else {
          s"{$entriesString}"
        }

      case _ => throw new GenerationException(s"Don't know how to handle default value $v")
    }
  }

  def quote(s: String): String = {
    '"' + s + '"'
  }

  def getQuotedStringOrNull(o: String): String = {

    if (o != null && o.startsWith("\""))
      return o

    if (o != null) ('"' + o.toString + '"') else null
  }

  def getString(o: Any): String = {
    if (o != null) o.toString else null
  }

}