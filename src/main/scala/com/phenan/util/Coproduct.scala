package com.phenan.util

sealed trait Coproduct {
  private[util] def getValueUnsafe: Any
}

sealed trait CNil extends Coproduct {
  private[util] def getValueUnsafe: Any = throw new RuntimeException("unreachable")
}

sealed trait |: [A, B <: Coproduct] extends Coproduct {
  def fold [R] (f: A => R)(g: B => R): R
}

case class InLeft [A, B <: Coproduct] (left: A) extends |: [A, B] {
  def fold [R] (f: A => R)(g: B => R): R = f(left)

  private[util] def getValueUnsafe: Any = left
}

case class InRight [A, B <: Coproduct] (right: B) extends |: [A, B] {
  def fold [R] (f: A => R)(g: B => R): R = g(right)

  private[util] def getValueUnsafe: Any = right.getValueUnsafe
}

object Coproduct {
  type Of [T <: Tuple] <: Coproduct = T match {
    case e *: es => e |: Of[es]
    case Unit    => CNil
  }

  object Of {
    def apply [T <: Tuple]: Builder[Of[T]] = new Builder
  }

  def toUnion [T <: Tuple] (c: Coproduct.Of[T]): Union.Of[T] = c.getValueUnsafe.asInstanceOf[Union.Of[T]]
  
  def fromUnion [T <: Tuple] (union: Union.Of[T], ordinal: Int): Coproduct.Of[T] = {
    fromUnionUntyped(union, ordinal).asInstanceOf[Coproduct.Of[T]]
  }  

  private def fromUnionUntyped (union: Any, ordinal: Int): Coproduct = {
    if (ordinal == 0) InLeft(union)
    else InRight(fromUnionUntyped(union, ordinal - 1))
  }

  class Builder [C <: Coproduct] {
    def apply [A] (a: A)(using inject: Inject[A, C]): C = inject(a)
  }
}

trait Inject [A, C <: Coproduct] {
  def apply (a: A): C
}

given leftInject [A, C <: Coproduct]: Inject[A, A |: C] {
  def apply (a: A): A |: C = InLeft(a)
}

given rightInject [A, B, C <: Coproduct] (using inject: Inject[A, C]): Inject[A, B |: C] {
  def apply (a: A): B |: C = InRight(inject(a))
}
