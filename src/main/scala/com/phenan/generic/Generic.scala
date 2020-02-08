package com.phenan.generic

import com.phenan.classes._
import com.phenan.util._

import scala.deriving.EmptyProduct

trait Generic [T, R] extends <=>[R, T]

given genericFromSingletonMirror [T] (using mirror: SingletonMirror[T]): Generic[T, Unit] {
  def from: Unit => T = _ => mirror.fromProduct(EmptyProduct)
  def to: T => Unit = _ => ()
}

given genericFromProductMirror [T <: Product, R <: Product] (using mirror: ProductMirror[T, R]): Generic[T, R] {
  def from: R => T = mirror.fromProduct
  def to: T => R = Tuple.fromProductTyped
}

given genericFromSumMirror [T, R <: NonEmptyTuple] (using mirror: SumMirror[T, R]): Generic[T, Union[R]] {
  def from: Union[R] => T = _.asInstanceOf[T]
  def to: T => Union[R] = _.asInstanceOf[Union[R]]
}
