package com.phenan.generic

import com.phenan.util._

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

given genericFromSumMirror [T, R <: NonEmptyTuple] (given mirror: SumMirror[T, R]): Generic[T, Union[R]] {
  def from (r: Union[R]): T = {
    r.asInstanceOf[T]
  }
  def to (t: T): Union[R] = {
    t.asInstanceOf[Union[R]]
  }
}
