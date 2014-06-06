package se.culvertsoft.mgen.visualdesigner.util

import java.util.IdentityHashMap

import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.mutable.ArrayBuffer

trait Observable[ObserverType] {

  private val observers = new IdentityHashMap[ObserverType, ObserverType]
  private val status = new OperationStatus
  private val tempList = new ArrayBuffer[ObserverType]

  def addObserver(o: ObserverType) {
    observers.put(o, o)
  }

  def removeObserver(o: ObserverType) {
    observers.remove(o)
  }

  def clearObservers() {
    observers.clear()
  }

  def triggerObservers(f: ObserverType => Unit) {
    status.active {
      tempList addAll observers.values
      tempList foreach f
      tempList clear
    }
  }

  def isTriggeringObservers(): Boolean = {
    status.isActive()
  }

}