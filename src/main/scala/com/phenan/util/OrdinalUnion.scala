package com.phenan.util

class OrdinalUnion [T <: NonEmptyTuple] private (val union: Union[T], ordinal: Int) {
  def getAs [E] (using proof: Tuples.Contain[T, E], valueOf: ValueOf[Tuples.IndexOf[T, E]]): Option[E] = {
    if (valueOf.value == ordinal) Some(union.asInstanceOf[E])
    else None
  }
}

object OrdinalUnion {
  def apply [T <: NonEmptyTuple, E] (e: E)(using proof: Tuples.Contain[T, E], valueOf: ValueOf[Tuples.IndexOf[T, E]]): OrdinalUnion[T] = {
    new OrdinalUnion[T](e.asInstanceOf[Union[T]], valueOf.value)
  }

  def buildUnsafe [T <: NonEmptyTuple] (union: Union[T], ordinal: Int): OrdinalUnion[T] = {
    new OrdinalUnion[T](union, ordinal)
  }
}
