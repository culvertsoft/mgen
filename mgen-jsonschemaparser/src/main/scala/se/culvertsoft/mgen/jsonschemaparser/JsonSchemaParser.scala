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
import se.culvertsoft.mgen.api.model.EnumEntry
import com.sun.codemodel.JFieldVar
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Type
import com.sun.codemodel.JPrimitiveType
import com.sun.codemodel.JType
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.UserDefinedType
import se.culvertsoft.mgen.api.model.UnlinkedType

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

    val absoluteDir = new File(parent.absoluteFilePath()).getParent()

    for (pkg <- codeModel.packages()) {
      val fileName = pkg.name() + ".xml"
      val module = parent.getOrCreateModule(pkg.name(), fileName, absoluteDir + "/" + fileName, settings);
      for (typ <- pkg.classes()) {
        if (typ.getClassType() == com.sun.codemodel.ClassType.ENUM) {
          module.addEnum(createEnum(typ, module, pkg))
        } else if (typ.getClassType() == com.sun.codemodel.ClassType.CLASS) {
          module.addClass(createClass(typ, module, pkg))
        } else {
          println("Warning, Mgen does not support " + typ.getClassType());
        }
      }
    }

  }

  def createEnum(typ: JDefinedClass, module: Module, pkg: JPackage): EnumType = {
    val mgenEnum = new EnumType(typ.name(), pkg.name() + "." + typ.name(), module)
    mgenEnum.setEntries(typ.getEnumConstantsByName().map( x=> new EnumEntry(x._1, null)).toSeq);
    mgenEnum
  }

  def createClass(typ: JDefinedClass, module: Module, pkg: JPackage): ClassType = {
    val mgenClass = new ClassType(typ.name(), module, null)
    for(jField <- typ.fields()){
      val jType = jField._2.`type`()
      val mgenType = createType(jType);
      val mgenField = new Field(typ.name(), jType.name(), mgenType, null)
      mgenClass.addField(mgenField)
    }
    mgenClass
  }
  
  def createType(jType: JType) : Type = {
    
    if(jType.isPrimitive()) {
    	createPrimitiveType(jType);
    } else if(jType.isArray()){
    	createArrayType(jType)
    } else if(jType.isReference()){
    	createReferenceType(jType)
    } else {
      throw new TypeNotPresentException(jType.name(), null)
    }
    
  }
  
  def createArrayType(jType: JType): Type = {
    new ArrayType(createType(jType.elementType()))
  }
  
  def createReferenceType(jType: JType): Type = {
    if(jType.fullName() == "java.lang.String"){
    	StringType.INSTANCE
    } else {
      new UnlinkedType(jType.fullName());
    }
  }
  
  def createPrimitiveType(jType: JType): Type = {
	val typeName = jType.name();
	typeName match {
	  case "boolean" => BoolType.INSTANCE
	  case "byte" => Int8Type.INSTANCE
	  case "short" => Int16Type.INSTANCE
	  case "char" => Int16Type.INSTANCE
	  case "int" => Int32Type.INSTANCE
	  case "long" => Int64Type.INSTANCE
	  case "float" => Float32Type.INSTANCE
	  case "double" => Float64Type.INSTANCE
	  case x :String => throw new TypeNotPresentException(x, null);
	}
  }

}