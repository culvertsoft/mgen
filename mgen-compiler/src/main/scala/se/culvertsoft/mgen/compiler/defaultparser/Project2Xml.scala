package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable.ArrayBuffer
import scala.xml.PrettyPrinter
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
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
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.api.plugins.GeneratorDescriptor
import se.culvertsoft.mgen.api.util.CRC16

object Project2Xml {

  case class XmlSourceFile(path: String, xml: scala.xml.Node)

  def apply(project: Project): Seq[GeneratedSourceFile] = {

    val sources = new ArrayBuffer[XmlSourceFile]

    val projectXml =
      <Project>
        { project.generators map generator2xml }
        { project.dependencies map dependency2xmlReference }
        { project.modules.filter(_.types.nonEmpty) map { x => <Module>{ x.filePath() }</Module> } }
      </Project>

    sources += XmlSourceFile(project.absoluteFilePath, projectXml)
    sources ++= project.modules.filter(_.types.nonEmpty) map module2xmlSource

    convert(sources)

  }

  def convert(sources: Seq[XmlSourceFile]): Seq[GeneratedSourceFile] = {

    val printer = new PrettyPrinter(120, 4)

    sources.map { source =>

      val sourceCode = printer.format(source.xml)
      new GeneratedSourceFile(source.path, sourceCode)

    }

  }

  def generator2xml(generator: GeneratorDescriptor): Seq[scala.xml.Node] = {
    <Generator name={ generator.getGeneratorName }>
      <generator_class_path>{ generator.getGeneratorClassPath }</generator_class_path>
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
        <Types>
          { module.types map type2xml }
        </Types>
      </Module>

    XmlSourceFile(module.absoluteFilePath(), xml)
  }

  def type2xml(typ: CustomType)(implicit currentModule: Module): scala.xml.Node = {

    val autoId = CRC16.calc(typ.fullName)
    val idString = if (typ.typeId16Bit != autoId) typ.typeId16Bit.toString else null
    
    val xml =
      (if (typ.hasSuperType())
        <CustomType extends={ type2string(typ.superType()) } id ={ idString }>{ typ.fields map field2xml } </CustomType>
      else
        <CustomType id ={ idString }>{ typ.fields map field2xml } </CustomType>)
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

    val xml =
      (if (flags.nonEmpty)
        <fieldname type={ typeString } flags={ flagsString } id ={ idString }/>
      else
        <fieldname type={ typeString } id ={ idString }/>)
        .copy(label = field.name)

    xml
  }

}