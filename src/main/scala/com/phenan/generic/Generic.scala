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

  class FromProductMirror [T <: Product, R <: Product] (val mirror: ProductMirror[T, R]) extends Generic[T, R] {
    def from (r: R): T = {
      mirror.fromProduct(r)
    }
    def to (t: T): R = {
      Tuple.fromProductTyped(t)(given mirror)
    }
  }

  type SumMirror [T, R] = Mirror.SumOf[T] { type MirroredElemTypes = R }

  type ElementsOfCoproduct[C <: Coproduct] = C match {
    case CNil     => Product0
    case e :+: es => e *: ElementsOfCoproduct[es]
  }

  class FromSumMirror [T, R <: Coproduct] (val mirror: SumMirror[T, ElementsOfCoproduct[R]]) extends Generic[T, R] {
    def from (r: R): T = {
      r.getUnTypedValue.asInstanceOf[T]
    }
    def to (t: T): R = {
      val ordinal = mirror.ordinal(t.asInstanceOf[mirror.MirroredMonoType])
      Coproduct[R].applyUnsafe(ordinal, t)
    }
  }

  type SingletonMirror [T] = Mirror.ProductOf[T] { type MirroredElemTypes = Unit }

  class FromSingletonMirror [T] (val mirror: SingletonMirror[T]) extends Generic[T, Unit] {
    def from (r: Unit): T = {
      mirror.fromProduct(EmptyProduct)
    }
    def to (t: T): Unit = ()
  }
}

given [T] (given mirror: Generic.SingletonMirror[T]): Generic[T, Unit] = new Generic.FromSingletonMirror(mirror)
given [T <: Product, R <: Product] (given mirror: Generic.ProductMirror[T, R]): Generic[T, R] = new Generic.FromProductMirror(mirror)
given [T, R <: Coproduct] (given mirror: Generic.SumMirror[T, Generic.ElementsOfCoproduct[R]]): Generic[T, R] = new Generic.FromSumMirror(mirror)
