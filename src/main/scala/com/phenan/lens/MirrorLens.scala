package com.phenan.lens

import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

object MirrorLens {
  def apply [T <: Product] (given mirror: Mirror.ProductOf[T]): MirrorLensBuilder[T, mirror.MirroredElemTypes, mirror.MirroredElemLabels] = new MirrorLensBuilder(mirror)

  class MirrorLensBuilder [T <: Product, ElemTypes <: Tuple, ElemLabels <: Tuple] (val mirror: Mirror.ProductOf[T]) extends Dynamic {
    def selectDynamic [L <: Singleton] (label: L)(given proof: L <:< Tuples.UnionOf[ElemLabels], tupleLens: TupleLens[ElemTypes, Tuples.IndexOf[ElemLabels, L]]): MirrorLens[T, ElemTypes, Tuples.IndexOf[ElemLabels, L]] = {
      new MirrorLens(mirror, tupleLens)
    }
  }
}

class MirrorLens [T <: Product, ElemTypes <: Tuple, I <: Int] (val mirror: Mirror.ProductOf[T], tupleLens: TupleLens[ElemTypes, I]) extends Lens[T, Tuple.Elem[ElemTypes, I]] {
  def get (t: T): Tuple.Elem[ElemTypes, I] = {
    tupleLens.get(Tuple.fromProduct(t).asInstanceOf[ElemTypes])
  }
  def set (t: T)(e: Tuple.Elem[ElemTypes, I]): T = {
    val tuple = tupleLens.set(Tuple.fromProduct(t).asInstanceOf[ElemTypes])(e)  
    mirror.fromProduct(tuple.asInstanceOf[Product])
  }
}
