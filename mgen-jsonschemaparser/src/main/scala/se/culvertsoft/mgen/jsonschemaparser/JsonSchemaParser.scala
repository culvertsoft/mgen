package se.culvertsoft.mgen.jsonschemaparser

import java.io.File
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions._
import scala.collection.JavaConversions.seqAsJavaList
import org.apache.commons.lang3.StringUtils.defaultString
import org.jsonschema2pojo.SchemaGenerator
import org.jsonschema2pojo.SchemaMapper
import org.jsonschema2pojo.cli.Arguments
import com.sun.codemodel.JCodeModel
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser
import se.culvertsoft.mgen.jsonschemaparser.utils.JavaUtils.createRuleFactory
import se.culvertsoft.mgen.jsonschemaparser.utils.JavaUtils.generateRecursive
import se.culvertsoft.mgen.jsonschemaparser.utils.JavaUtils.getAnnotator
import se.culvertsoft.mgen.jsonschemaparser.utils.JavaUtils.getNodeName
import se.culvertsoft.mgen.jsonschemaparser.utils.JavaUtils.removeOldOutput
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import com.sun.codemodel.JPackage
import com.sun.codemodel.JDefinedClass
import se.culvertsoft.mgen.api.model.Module

/**
 * Contains several snippets extracted from the class Jsonschema2Pojo from the github apache2 project with the same name
 */
class JsonSchemaParser extends Parser {

  override def parse(
    sources: java.util.List[File],
    settings: java.util.Map[String, String],
    parent: Project) {

    val config = new Arguments().parse(Array[String]());

    val annotator = getAnnotator(config);

    val ruleFactory = createRuleFactory(config);

    ruleFactory.setAnnotator(annotator);
    ruleFactory.setGenerationConfig(config);

    val mapper = new SchemaMapper(ruleFactory, new SchemaGenerator());

    val codeModel = new JCodeModel();

    if (config.isRemoveOldOutput()) {
      removeOldOutput(config.getTargetDirectory());
    }

    for (source <- sources) {
      mapper.generate(codeModel, getNodeName(source), defaultString(config.getTargetPackage()), source.toURI().toURL());
    }

    val absoluteDir = new File(parent.absoluteFilePath()).getParent();
    //
    //    	public EnumType(
    //			final String shortName,
    //			final String fullName,
    //			final Module module) {
    //		super(TypeEnum.ENUM, module)

    for (pkg <- codeModel.packages()) {
      val fileName = pkg.name() + ".xml"
      val module = parent.getOrCreateModule(pkg.name(), fileName, absoluteDir + fileName, settings);
      for (typ <- pkg.classes()) {
        if (typ.getClassType() == com.sun.codemodel.ClassType.ENUM) {
          module.addEnum(createEnum(typ, module, pkg));
        } else {
          module.addClass(createClass(typ, module, pkg))
        }
      }
    }

  }

  def createEnum(typ: JDefinedClass, module: Module, pkg: JPackage): EnumType = {
    val e = new EnumType(typ.name(), pkg.name() + "." + typ.name(), module)
    
    e
  }

  def createClass(typ: JDefinedClass, module: Module, pkg: JPackage): ClassType = {
	val c = new ClassType(typ.name(), module, null)
	c
  }

}