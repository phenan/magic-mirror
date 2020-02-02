package com.phenan.coproduct

sealed trait Coproduct {
  def getUnTypedValue: Any
}

sealed trait CNil extends Coproduct {
  def getUnTypedValue: Any = throw new RuntimeException("getUnTypedValue from CNil")
}

sealed trait +: [E, ES <: Coproduct] extends Coproduct
case class InL [E, ES <: Coproduct] (left: E) extends +:[E, ES] {
  def getUnTypedValue: Any = left
}

case class InR [E, ES <: Coproduct] (right: ES) extends +:[E, ES] {
  def getUnTypedValue: Any = right.getUnTypedValue
}

object Coproduct {
  def apply [C <: Coproduct]: CoproductBuilder[C] = new CoproductBuilder[C]

  class CoproductBuilder [C <: Coproduct] {
    def apply[E] (e: E)(given inject: Inject[C, E]): C = inject(e)

    def applyUnsafe (ordinal: Int, v: Any): C = {
      buildUnsafe(ordinal, v).asInstanceOf[C]
    }

    private def buildUnsafe (ordinal: Int, v: Any): Any = {
      if (ordinal == 0) InL(v)
      else InR(buildUnsafe(ordinal - 1, v).asInstanceOf[Coproduct])
    }
  }
}

trait Inject [C <: Coproduct, E] {
  def apply (e: E): C
}

given  [C <: Coproduct, E] : Inject[E +: C, E] {
  def apply (e: E): E +: C = InL(e)
}

given [T, C <: Coproduct, E] (given inject: Inject[C, E]) : Inject[T +: C, E] {
  def apply (e: E): T +: C = InR(inject(e))
}
