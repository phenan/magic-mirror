package com.phenan.util

import scala.compiletime.ops.int._

type OrdinalUnion [T <: NonEmptyTuple] = OrdinalUnion.OrdinalUnionImpl[Union[T]]

object OrdinalUnion {
  case class OrdinalUnionImpl [+U] (val value: U, val reverseOrdinal: Int)

  def apply [T <: NonEmptyTuple]: OrdinalUnionBuilder[T] = new OrdinalUnionBuilder[T]
  
  def buildUnsafe [T <: NonEmptyTuple] (union: Union[T], ordinal: Int, length: Int): OrdinalUnion[T] = {
    new OrdinalUnionImpl[Union[T]](union, length - ordinal)
  }

  class OrdinalUnionBuilder [T <: NonEmptyTuple] {
    def apply [E] (e: E)(using proof: Tuples.Contain[T, E], valueOf: ValueOf[Tuple.Size[T] - Tuples.IndexOf[T, E]]): OrdinalUnion[T] = {
      new OrdinalUnionImpl[Union[T]](e.asInstanceOf[Union[T]], valueOf.value)
    }
  }
}

def [T <: NonEmptyTuple] (union: OrdinalUnion[T]) getAsNthUntyped (n: Int)(using length: ValueOf[Tuple.Size[T]]): Any = {
  if (length.value - union.reverseOrdinal == n) Some(union.value)
  else None
}

def [H, T <: NonEmptyTuple, R] (union: OrdinalUnion[H *: T]) fold (f: H => R)(g: OrdinalUnion[T] => R)(using length: ValueOf[Tuple.Size[H *: T]]): R = {
  union.getAsNthUntyped(0)(using length) match {
    case Some(value) => f(value.asInstanceOf[H])
    case None        => g(union.asInstanceOf[OrdinalUnion[T]])
  }
}
