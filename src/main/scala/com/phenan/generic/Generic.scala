package com.phenan.generic

import com.phenan.classes._
import com.phenan.util._

import scala.deriving._

trait Generic [T, U] {
  def fromUnderlying (underlying: U): T
  def toUnderlying (value: T): U

  def toIso: T <=> U = Iso[Function1, T, U](toUnderlying)(fromUnderlying)
}

class ProductGeneric [T <: Product, U <: Tuple] (val mirror: Mirror.ProductOf[T])(using mirror.MirroredElemTypes =:= U) extends Generic[T, U] {
  override def fromUnderlying (underlying: U): T = underlying match {
    case p: Product => mirror.fromProduct(p)
    case ()         => mirror.fromProduct(EmptyProduct)
    case t          => mirror.fromProduct(new ArrayProduct(t.toArray))
  }
  override def toUnderlying (value: T): U = Tuple.fromProductTyped(value)(using mirror).asInstanceOf[U]
}

class UnionGeneric [T, U <: Tuple] (val mirror: Mirror.SumOf[T])(using mirror.MirroredElemTypes =:= U) extends Generic[T, Union.Of[U]] {
  override def fromUnderlying (underlying: Union.Of[U]): T = underlying.asInstanceOf[T]
  override def toUnderlying (value: T): Union.Of[U] = value.asInstanceOf[Union.Of[U]]
}

class CoproductGeneric [T, U <: Tuple] (val mirror: Mirror.SumOf[T])(using mirror.MirroredElemTypes =:= U) extends Generic[T, Coproduct.Of[U]] {
  override def fromUnderlying (underlying: Coproduct.Of[U]): T = Coproduct.toUnion[U](underlying).asInstanceOf[T]
  override def toUnderlying (value: T): Coproduct.Of[U] = Coproduct.fromUnion[U](value.asInstanceOf[Union.Of[U]], mirror.ordinal(value))
}

given productGeneric [T <: Product, U <: Tuple] (using mirror: Mirror.ProductOf[T], proof: mirror.MirroredElemTypes =:= U): ProductGeneric[T, U] = new ProductGeneric(mirror)
given unionGeneric [T, U <: Tuple] (using mirror: Mirror.SumOf[T], proof: mirror.MirroredElemTypes =:= U): UnionGeneric[T, U] = new UnionGeneric(mirror)
given coproductGeneric [T, U <: Tuple] (using mirror: Mirror.SumOf[T], proof: mirror.MirroredElemTypes =:= U): CoproductGeneric[T, U] = new CoproductGeneric(mirror)
