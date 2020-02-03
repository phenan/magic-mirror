package com.phenan.generic

import com.phenan.coproduct._
import com.phenan.mirror._

import com.phenan.coproduct.given

import scala.deriving.EmptyProduct

trait Generic [T, R] {
  def from (r: R): T
  def to (t: T): R
}

given genericFromSingletonMirror [T] (given mirror: SingletonMirror[T]): Generic[T, Unit] {
  def from (r: Unit): T = {
    mirror.fromProduct(EmptyProduct)
  }
  def to (t: T): Unit = ()
}

given genericFromProductMirror [T <: Product, R <: Product] (given mirror: ProductMirror[T, R]): Generic[T, R] {
  def from (r: R): T = {
    mirror.fromProduct(r)
  }
  def to (t: T): R = {
    Tuple.fromProductTyped(t)(given mirror)
  }
}

given genericFromSumMirror [T, R <: Coproduct] (given mirror: SumMirror[T, Coproduct.Elements[R]]): Generic[T, R] {
  def from (r: R): T = {
    r.getUnTypedValue.asInstanceOf[T]
  }
  def to (t: T): R = {
    val ordinal = mirror.ordinal(t.asInstanceOf[mirror.MirroredMonoType])
    Coproduct[R].applyUnsafe(ordinal, t)
  }
}
