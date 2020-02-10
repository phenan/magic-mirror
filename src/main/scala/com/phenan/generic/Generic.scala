package com.phenan.generic

import com.phenan.classes._
import com.phenan.util._

import scala.deriving._

trait Generic [T, R] extends <=>[R, T]

given genericFromProductMirror [T <: Product, R <: Tuple] (using mirror: Mirror.ProductOf[T], proof: mirror.MirroredElemTypes =:= R): Generic[T, R] {
  def from: R => T = {
    case p: Product => mirror.fromProduct(p)
    case ()         => mirror.fromProduct(EmptyProduct)
    case t          => mirror.fromProduct(new ArrayProduct(t.toArray))
  }
  def to: T => R = t => Tuple.fromProductTyped(t).asInstanceOf[R]
}

given genericFromSumMirror [T, R <: NonEmptyTuple] (using mirror: Mirror.SumOf[T], proof: mirror.MirroredElemTypes =:= R): Generic[T, Union[R]] {
  def from: Union[R] => T = _.asInstanceOf[T]
  def to: T => Union[R] = _.asInstanceOf[Union[R]]
}
