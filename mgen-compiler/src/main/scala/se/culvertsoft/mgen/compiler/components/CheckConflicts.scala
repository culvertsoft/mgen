package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.exceptions.TypeConflictException
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Project

object CheckConflicts {

  private def assertNoDuplicates[T <: AnyRef, A](
    ts: Seq[T],
    f: T => A)(conflictHandler: (T, T) => Unit) {
    val store = new HashMap[A, T]
    for (t <- ts) {
      val a = f(t)
      if (store.contains(a) && !(t eq store(a)))
        conflictHandler(t, store(a))
      store.put(a, t)
    }
  }

  def apply(project: Project) {

    val allModules = project.allModulesRecursively
    val allTypes = allModules.flatMap(_.classes)
    val allEnums = allModules.flatMap(_.enums)

    assertNoDuplicates(allTypes, (t: ClassType) => t.fullName) { (t1, t2) =>
      throw new TypeConflictException(s"Class defined twice with same class path: ${t1.fullName}")
    }

    assertNoDuplicates(allEnums, (t: EnumType) => t.fullName) { (t1, t2) =>
      throw new TypeConflictException(s"Enum defined twice with same class path: ${t1.fullName}")
    }

    assertNoDuplicates(allTypes, (t: ClassType) => t.typeId) { (t1, t2) =>
      throw new TypeConflictException(s"Conflicting 64 bit type IDs (wow you're unlucky!) for types: ${t1.fullName} and ${t2.fullName()}. Change one of their names.")
    }

    assertNoDuplicates(allTypes, (t: ClassType) => (if (t.hasSuperType) t.superType.asInstanceOf[ClassType].typeId else null, t.typeId16Bit)) { (t1, t2) =>
      throw new TypeConflictException(s"Conflicting 16 bit type IDs with same super type. Types: ${t1.fullName} and ${t2.fullName()}, with super type ${t1.superType()}.")
    }

    for (t <- allTypes) {
      val fields = t.fieldsInclSuper()
      assertNoDuplicates(fields, (f: Field) => f.name) { (f1, f2) =>
        throw new TypeConflictException(s"Conflicting field names for type ${t.fullName()}: ${f1} and ${f2}.")
      }
      assertNoDuplicates(fields, (f: Field) => f.id) { (f1, f2) =>
        throw new TypeConflictException(s"Conflicting field ids for type ${t.fullName()}: ${f1} and ${f2}.")
      }
    }

  }

}