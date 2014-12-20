package se.culvertsoft.mgen.idlgenerator

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.xml.PrettyPrinter
import scala.xml.Text

import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.ClassType
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
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UserDefinedType
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.idlgenerator.util.IdlGenUtil

class IdlGenerator extends Generator {

  override def generate(
    project: Project,
    generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {

    generate(project, generatorSettings.get("output_path"))

  }

  def generate(
    project: Project,
    prependPath: String): java.util.List[GeneratedSourceFile] = {

    val prefix = prependPath match {
      case p if (p.trim.nonEmpty) => p + "/"
      case _ => ""
    }

    generate(project).map(_.transformPrependPath(prefix))

  }

  def generate(project: Project): java.util.List[GeneratedSourceFile] = {
    generateImpl(project)
  }

  /**
   * ********************************
   *
   * 		IMPLEMENTATION
   *
   * *******************************
   */

  private case class XmlSourceFile(path: String, xml: scala.xml.Node)

  private def generateImpl(project: Project): Seq[GeneratedSourceFile] = {

    val projectXml =
      <Project>
        { project.generators map generator2xml }
        { project.dependencies map dependency2xmlReference }
        <Sources>
          { project.modules.filter(_.classes.nonEmpty) map { x => <Source>{ x.filePath }</Source> } }
        </Sources>
      </Project>

    val sources = Nil ++
      Seq(XmlSourceFile(project.absoluteFilePath, projectXml)) ++
      (project.modules.filter(_.classes.nonEmpty) map module2xmlSource)

    convert(sources)

  }

  private def convert(sources: Seq[XmlSourceFile]): Seq[GeneratedSourceFile] = {

    val printer = new PrettyPrinter(120, 4)

    sources.map { source =>

      val sourceCode = printer.format(source.xml)
      new GeneratedSourceFile(source.path, sourceCode)

    }

  }

  private def generator2xml(generator: GeneratorDescriptor): Seq[scala.xml.Node] = {

    val settings =
      generator.getGeneratorSettings().asScala -
        "jar_file_folder" -
        "class_path" -
        "output_path" -
        "classregistry_path"

    <Generator name={ generator.getGeneratorName }>
      <class_path>{ generator.getGeneratorClassPath }</class_path>
      <output_path>{ generator.getGeneratorSettings().get("output_path") }</output_path>
      <classregistry_path>{ generator.getGeneratorSettings().get("classregistry_path") }</classregistry_path>
      { settings.map(x => settingNode2xml(x._1, x._2)) }
    </Generator>

  }

  private def dependency2xmlReference(dependency: Project): scala.xml.Node = {
    <Depend>{ dependency.filePath }</Depend>
  }

  private def settingNode2xml(key: String, value: String): scala.xml.Node = {
    <setting>{ value }</setting>.copy(label = key)
  }

  private def module2xmlSource(module: Module): XmlSourceFile = {

    implicit val _module = module

    val xml =
      <Module>
        <Enums>
          { module.enums map enum2xml }
        </Enums>
        <Types>
          { module.classes map type2xml }
        </Types>
      </Module>

    XmlSourceFile(module.absoluteFilePath(), xml)
  }

  private def enum2xml(typ: EnumType)(implicit module: Module): scala.xml.Node = {
    <EnumType>{ typ.entries map enumentry2xml }</EnumType>.copy(label = typ.shortName)
  }

  private def enumentry2xml(entry: EnumEntry)(implicit module: Module): scala.xml.Node = {
    <entry>{ entry.constant } </entry>.copy(label = entry.name)
  }

  private def type2xml(typ: ClassType)(implicit module: Module): scala.xml.Node = {

    val autoId = CRC16.calc(typ.fullName)
    val idString = if (typ.typeId16Bit != autoId) typ.typeId16Bit.toString else null

    val xml =
      (if (typ.hasSuperType())
        <CustomType extends={ type2string(typ.superType()) } id={ idString }>{ typ.fields map field2xml } </CustomType>
      else
        <CustomType id={ idString }>{ typ.fields map field2xml } </CustomType>)
        .copy(label = typ.shortName)

    xml

  }

  private def type2string(t: Type)(implicit module: Module): String = {
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
      case t: UserDefinedType => if (t.module == module) t.shortName else t.fullName
    }
  }

  private def field2xml(field: Field)(implicit module: Module): scala.xml.Node = {

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
        xmlT.copy(label = field.name, child = Text(IdlGenUtil.defaultVal2String(field.defaultValue)))
      } else {
        xmlT.copy(label = field.name)
      }

    xml
  }

}