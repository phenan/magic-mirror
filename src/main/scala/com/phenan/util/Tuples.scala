package com.phenan.util

import scala.compiletime._

object Tuples {
  type IndexOf [T <: Tuple, E] <: Int = T match {
    case e *: es => IfEq[e, E, 0, S[IndexOf[es, E]]]
    case Unit    => 1
  }

  type UnionOf [T <: Tuple] = T match {
    case e *: es => e | UnionOf[es]
    case Unit    => Void
  }
}
