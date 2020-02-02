package com.phenan.generic

import com.phenan.coproduct._
import com.phenan.coproduct.given

import scala.deriving.{EmptyProduct, Mirror}

trait Generic [T, R] {
  def from (r: R): T
  def to (t: T): R
}

object Generic {
  type ProductMirror [T, R <: Product] = Mirror.ProductOf[T] { type MirroredElemTypes = R }

  type SumMirror [T, R] = Mirror.SumOf[T] { type MirroredElemTypes = R }

  type SingletonMirror [T] = Mirror.ProductOf[T] { type MirroredElemTypes = Unit }

  type ElementsOfCoproduct[C <: Coproduct] = C match {
    case CNil     => Product0
    case e :+: es => e *: ElementsOfCoproduct[es]
  }
}

given genericFromSingletonMirror [T] (given mirror: Generic.SingletonMirror[T]): Generic[T, Unit] {
  def from (r: Unit): T = {
    mirror.fromProduct(EmptyProduct)
  }
  def to (t: T): Unit = ()
}

given genericFromProductMirror [T <: Product, R <: Product] (given mirror: Generic.ProductMirror[T, R]): Generic[T, R] {
  def from (r: R): T = {
    mirror.fromProduct(r)
  }
  def to (t: T): R = {
    Tuple.fromProductTyped(t)(given mirror)
  }
}

given genericFromSumMirror [T, R <: Coproduct] (given mirror: Generic.SumMirror[T, Generic.ElementsOfCoproduct[R]]): Generic[T, R] {
  def from (r: R): T = {
    r.getUnTypedValue.asInstanceOf[T]
  }
  def to (t: T): R = {
    val ordinal = mirror.ordinal(t.asInstanceOf[mirror.MirroredMonoType])
    Coproduct[R].applyUnsafe(ordinal, t)
  }
}
