package com.phenan.util

object Union {
  type Of [T <: Tuple] = T match {
    case e *: es => UnionImpl[e, es]
    case Unit    => Void
  }
  type UnionImpl [A, T <: Tuple] = T match {
    case e *: es => A | UnionImpl[e, es]
    case Unit    => A
  }
}
