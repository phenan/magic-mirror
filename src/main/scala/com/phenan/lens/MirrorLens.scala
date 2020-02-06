package com.phenan.lens

import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

object MirrorLens {
  def apply [T <: Product] (given mirror: Mirror.ProductOf[T]): MirrorLensBuilder[T, mirror.MirroredElemTypes, mirror.MirroredElemLabels] = new MirrorLensBuilder(mirror)

  class MirrorLensBuilder [T <: Product, ElemTypes <: Tuple, ElemLabels <: Tuple] (mirror: Mirror.ProductOf[T]) extends Dynamic {
    def selectDynamic [L <: Singleton] (label: L)(given proof: L <:< Tuples.UnionOf[ElemLabels], tupleLens: TupleLens[ElemTypes, Tuples.IndexOf[ElemLabels, L]]): MirrorLens[T, ElemTypes, Tuples.IndexOf[ElemLabels, L], Tuple.Elem[ElemTypes, Tuples.IndexOf[ElemLabels, L]]] = {
      new MirrorLens(mirror, tupleLens)
    }
  }
}

class MirrorLens [T, ElemTypes <: Tuple, I <: Int, ElemType] (mirror: Mirror.ProductOf[T], tupleLens: TupleLens[ElemTypes, I]) extends Lens[T, ElemType] with Dynamic {
  def get (t: T): ElemType = {
    tupleLens.get(Tuple.fromProduct(t.asInstanceOf[Product]).asInstanceOf[ElemTypes]).asInstanceOf[ElemType]
  }
  def set (t: T)(e: ElemType): T = {
    val tuple = tupleLens.set(Tuple.fromProduct(t.asInstanceOf[Product]).asInstanceOf[ElemTypes])(e.asInstanceOf)  
    mirror.fromProduct(tuple.asInstanceOf[Product])
  }

  def selectDynamic [L <: Singleton] (label: L)(given m: Mirror.ProductOf[ElemType], proof: L <:< Tuples.UnionOf[m.MirroredElemLabels], lens: TupleLens[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]): Lens[T, Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]] = {
    val mirrorLens: Lens[ElemType, Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]] = new MirrorLens[ElemType, m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L], Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]](m, lens)
    mirrorLens.compose(this)
  }
}
