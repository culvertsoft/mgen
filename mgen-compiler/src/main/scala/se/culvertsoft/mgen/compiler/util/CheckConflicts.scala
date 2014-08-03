package se.culvertsoft.mgen.compiler.util

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

import se.culvertsoft.mgen.api.exceptions.TypeConflictException
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project

object CheckConflicts {

  private class GatherModulesState {
    val projectsScanned = new HashSet[Project]
    val modulesFound = new HashSet[Module]
  }

  private def gatherModulesFromProject(project: Project): GatherModulesState = {
    val state = new GatherModulesState
    def gatherModulesInternal(project: Project, state: GatherModulesState) {
      if (!state.projectsScanned.contains(project)) {
        state.projectsScanned += project
        project.dependencies.foreach(gatherModulesInternal(_, state))
        state.modulesFound ++= project.modules
      }
    }
    gatherModulesInternal(project, state)
    state
  }

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

    val state = gatherModulesFromProject(project)

    val allModules = state.modulesFound.toSeq
    val allTypes = allModules.flatMap(_.types)

    assertNoDuplicates(allModules, (m: Module) => m.path) { (m1, m2) =>
      throw new TypeConflictException(s"Module defined twice with same path: ${m1.path}, on file paths: [${m1.absoluteFilePath}, ${m2.absoluteFilePath}]")
    }

    assertNoDuplicates(allTypes, (t: CustomType) => t.fullName) { (t1, t2) =>
      throw new TypeConflictException(s"Type defined twice with same class path: ${t1.fullName}")
    }

    assertNoDuplicates(allTypes, (t: CustomType) => t.typeId) { (t1, t2) =>
      throw new TypeConflictException(s"Conflicting 64 bit type IDs (wow you're unlucky!) for types: ${t1.fullName} and ${t2.fullName()}. Change one of their names.")
    }

    assertNoDuplicates(allTypes, (t: CustomType) => (if (t.hasSuperType) t.superType.typeId else null, t.typeId16Bit)) { (t1, t2) =>
      throw new TypeConflictException(s"Conflicting 16 bit type IDs with same super type. Types: ${t1.fullName} and ${t2.fullName()}, with super type ${t1.superType()}.")
    }

    for (t <- allTypes) {
      val fields = t.fields()
      assertNoDuplicates(fields, (f: Field) => f.name) { (f1, f2) =>
        throw new TypeConflictException(s"Conflicting field names for type ${t.fullName()}: ${f1} and ${f2}.")
      }
      assertNoDuplicates(fields, (f: Field) => f.id) { (f1, f2) =>
        throw new TypeConflictException(s"Conflicting field ids for type ${t.fullName()}: ${f1} and ${f2}.")
      }
    }

  }

}