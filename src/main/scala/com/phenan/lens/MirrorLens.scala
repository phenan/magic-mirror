package com.phenan.lens

import com.phenan.util._

import scala.deriving._

object MirrorLens {
  def apply [T <: Product, E] (given mirror: Mirror.ProductOf[T], evidence: E <:< Tuples.UnionOf[mirror.MirroredElemLabels]) : MirrorLensBuilder[T, Tuples.IndexOf[mirror.MirroredElemLabels, E], Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, E]]] = {
    new MirrorLensBuilder(mirror)
  }

  class MirrorLensBuilder [T <: Product, I <: Int, E] (val mirror: Mirror.ProductOf[T]) {
    def apply ()(given valueOf: ValueOf[I]): MirrorLens[T, I, E] = new MirrorLens(mirror, valueOf)
  }
}

class MirrorLens [T <: Product, I <: Int, E] (mirror: Mirror.ProductOf[T], valueOf: ValueOf[I]) extends Lens[T, E] {
  def get (t: T): E = {
    productElement[E](t, valueOf.value)
  }
  def set (t: T)(e: E): T = {
    val tuple = update(Tuple.fromProduct(t), valueOf.value, e)
    mirror.fromProduct(tuple.asInstanceOf[Product])
  }

  private def update (tuple: Tuple, index: Int, value: Any): Tuple = tuple match {
    case e *: es =>
      if (index == 0) value *: es
      else e *: update(es, index - 1, value)
    case null =>
      throw new RuntimeException("invalid member index")
  }
}


