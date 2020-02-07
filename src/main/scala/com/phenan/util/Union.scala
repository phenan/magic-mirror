package com.phenan.util

type Union[T <: NonEmptyTuple] = Union.UnionOf[Tuple.Head[T], Tuple.Tail[T]]

type UnionOrVoid[T <: Tuple] = T match {
  case e *: es => Union.UnionOf[e, es]
  case Unit    => Void
}

object Union {
  type UnionOf [A, T <: Tuple] = T match {
    case e *: es => A | UnionOf[e, es]
    case Unit    => A
  }
}
