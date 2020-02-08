package com.phenan.util

import scala.compiletime._

object Tuples {
  type IndexOf [T <: Tuple, E] <: Int = T match {
    case e *: es => IfEq[e, E, 0, S[IndexOf[es, E]]]
    case Unit    => 1
  }
  
  sealed trait Contain [T <: Tuple, E]
}

given containAsHead [T <: Tuple, E] : Tuples.Contain[E *: T, E] {}
given containInTail [H, T <: Tuple, E] (using tailContain: Tuples.Contain[T, E]) : Tuples.Contain[H *: T, E] {}
