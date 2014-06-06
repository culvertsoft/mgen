package se.culvertsoft.mgen.visualdesigner.control

import scala.collection.mutable.Stack

class UndoBuffer[T](_state0: T, sizer: T => Int) {

  private val prevStates = new Stack[T]
  private val futureStates = new Stack[T]
  prevStates.push(_state0)

  def pushState(state: T, allowDuplicate: Boolean) {
    if (allowDuplicate || prevStates.head != state) {
      futureStates.clear()
      prevStates.push(state)
    }
  }

  def optPrevious(): Option[T] = {
    prevStates.headOption
  }

  def optFuture(): Option[T] = {
    futureStates.headOption
  }
  
  def goBack(current: T): Option[T] = {

    while (prevStates.nonEmpty) {

      val isLast = prevStates.size == 1
      val move = if (isLast) prevStates.head else prevStates.pop()

      if (futureStates.isEmpty || futureStates.head != move) {
        futureStates.push(move)
      }

      if (move != current) {
        return Some(move)
      }

      if (isLast)
        return None

    }

    return None
  }

  def goForward(current: T): Option[T] = {

    while (futureStates.nonEmpty) {
      val move = futureStates.pop()
      if (prevStates.isEmpty || prevStates.head != move) {
        prevStates.push(move)
      }
      if (move != current) {
        return Some(move)
      }
    }

    return None

  }

  def size(): Int = {
    prevStates.map(sizer(_)).sum
  }

  def dropLast() {
    prevStates.dropRight(1)
  }

  def clear(_state0: T) {
    futureStates.clear()
    prevStates.clear()
    prevStates.push(_state0)
  }

}