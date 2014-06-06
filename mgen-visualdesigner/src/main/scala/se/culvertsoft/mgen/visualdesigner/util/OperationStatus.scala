package se.culvertsoft.mgen.visualdesigner.util

class OperationStatus {

   private var _active = false

   def active(f: => Unit) {
      val prev = isActive
      enable()
      try {
         f
      } finally {
         set(prev)
      }
   }
   
   def enable() {
      set(true)
   }
   
   def disable() {
      set(false)
   }
   
   def set(state: Boolean) {
      _active = state
   }

   def isActive(): Boolean = {
      _active
   }

}