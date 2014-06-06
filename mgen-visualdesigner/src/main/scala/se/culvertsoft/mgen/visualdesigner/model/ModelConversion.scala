package se.culvertsoft.mgen.visualdesigner.model

object ModelConversion {

  type ApiEntity = Object
  type ApiProject = se.culvertsoft.mgen.api.model.Project
  type ApiModule = se.culvertsoft.mgen.api.model.Module
  type ApiClass = se.culvertsoft.mgen.api.model.CustomType
  type ApiField = se.culvertsoft.mgen.api.model.Field
  type ApiGenerator = se.culvertsoft.mgen.api.plugins.GeneratorDescriptor
  type ApiProjectImpl = se.culvertsoft.mgen.api.model.impl.ProjectImpl
  type ApiModuleImpl = se.culvertsoft.mgen.api.model.impl.ModuleImpl
  type ApiClassImpl = se.culvertsoft.mgen.api.model.impl.CustomTypeImpl
  type ApiType = se.culvertsoft.mgen.api.model.Type

  type VdModel = se.culvertsoft.mgen.visualdesigner.model.Model
  type VdEntity = se.culvertsoft.mgen.visualdesigner.model.Entity
  type VdProject = se.culvertsoft.mgen.visualdesigner.model.Project
  type VdModule = se.culvertsoft.mgen.visualdesigner.model.Module
  type VdClass = se.culvertsoft.mgen.visualdesigner.model.CustomType
  type VdField = se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
  type VdFieldType = se.culvertsoft.mgen.visualdesigner.model.FieldType
  type VdGenerator = se.culvertsoft.mgen.visualdesigner.model.Generator

  type VdBoolType = se.culvertsoft.mgen.visualdesigner.model.BoolType
  type VdInt8Type = se.culvertsoft.mgen.visualdesigner.model.Int8Type
  type VdInt16Type = se.culvertsoft.mgen.visualdesigner.model.Int16Type
  type VdInt32Type = se.culvertsoft.mgen.visualdesigner.model.Int32Type
  type VdInt64Type = se.culvertsoft.mgen.visualdesigner.model.Int64Type
  type VdFloat32Type = se.culvertsoft.mgen.visualdesigner.model.Float32Type
  type VdFloat64Type = se.culvertsoft.mgen.visualdesigner.model.Float64Type
  type VdStringType = se.culvertsoft.mgen.visualdesigner.model.StringType
  type VdListType = se.culvertsoft.mgen.visualdesigner.model.ListType
  type VdArrayType = se.culvertsoft.mgen.visualdesigner.model.ArrayType
  type VdMapType = se.culvertsoft.mgen.visualdesigner.model.MapType
  type VdCustomTypeRef = se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef

  type ApiBoolType = se.culvertsoft.mgen.api.model.BoolType
  type ApiInt8Type = se.culvertsoft.mgen.api.model.Int8Type
  type ApiInt16Type = se.culvertsoft.mgen.api.model.Int16Type
  type ApiInt32Type = se.culvertsoft.mgen.api.model.Int32Type
  type ApiInt64Type = se.culvertsoft.mgen.api.model.Int64Type
  type ApiFloat32Type = se.culvertsoft.mgen.api.model.Float32Type
  type ApiFloat64Type = se.culvertsoft.mgen.api.model.Float64Type
  type ApiStringType = se.culvertsoft.mgen.api.model.StringType
  type ApiListType = se.culvertsoft.mgen.api.model.ListType
  type ApiArrayType = se.culvertsoft.mgen.api.model.ArrayType
  type ApiMapType = se.culvertsoft.mgen.api.model.MapType
  type ApiCustomType = se.culvertsoft.mgen.api.model.CustomType

  type ApiBoolTypeImpl = se.culvertsoft.mgen.api.model.impl.BoolTypeImpl
  type ApiInt8TypeImpl = se.culvertsoft.mgen.api.model.impl.Int8TypeImpl
  type ApiInt16TypeImpl = se.culvertsoft.mgen.api.model.impl.Int16TypeImpl
  type ApiInt32TypeImpl = se.culvertsoft.mgen.api.model.impl.Int32TypeImpl
  type ApiInt64TypeImpl = se.culvertsoft.mgen.api.model.impl.Int64TypeImpl
  type ApiFloat32TypeImpl = se.culvertsoft.mgen.api.model.impl.Float32TypeImpl
  type ApiFloat64TypeImpl = se.culvertsoft.mgen.api.model.impl.Float64TypeImpl
  type ApiStringTypeImpl = se.culvertsoft.mgen.api.model.impl.StringTypeImpl
  type ApiListTypeImpl = se.culvertsoft.mgen.api.model.impl.ListTypeImpl
  type ApiArrayTypeImpl = se.culvertsoft.mgen.api.model.impl.ArrayTypeImpl
  type ApiMapTypeImpl = se.culvertsoft.mgen.api.model.impl.MapTypeImpl
  type ApiCustomTypeImpl = se.culvertsoft.mgen.api.model.impl.CustomTypeImpl

  val ApiBoolTypeInstance = se.culvertsoft.mgen.api.model.BoolType.INSTANCE
  val ApiInt8TypeInstance = se.culvertsoft.mgen.api.model.Int8Type.INSTANCE
  val ApiInt16TypeInstance = se.culvertsoft.mgen.api.model.Int16Type.INSTANCE
  val ApiInt32TypeInstance = se.culvertsoft.mgen.api.model.Int32Type.INSTANCE
  val ApiInt64TypeInstance = se.culvertsoft.mgen.api.model.Int64Type.INSTANCE
  val ApiFloat32TypeInstance = se.culvertsoft.mgen.api.model.Float32Type.INSTANCE
  val ApiFloat64TypeInstance = se.culvertsoft.mgen.api.model.Float64Type.INSTANCE
  val ApiStringTypeInstance = se.culvertsoft.mgen.api.model.StringType.INSTANCE
  val ApiCustomTypeInstance = se.culvertsoft.mgen.api.model.CustomType.INSTANCE

  def vd2Api(model: VdModel): ApiProjectImpl = {
    Vd2Api(model)
  }

  def api2Vd(project: ApiProject): VdModel = {
    Api2Vd(project)
  }

}