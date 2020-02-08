package com.phenan.lens

import com.phenan.classes._
import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

object MirrorLens {
  def apply [T <: Product] (given mirror: Mirror.ProductOf[T]): MirrorLensBuilder[T, mirror.MirroredElemTypes, mirror.MirroredElemLabels] = new MirrorLensBuilder(mirror)

  class MirrorLensBuilder [T <: Product, ElemTypes <: Tuple, ElemLabels <: Tuple] (mirror: Mirror.ProductOf[T]) extends Dynamic {
    def selectDynamic [L <: Singleton] (label: L)(given proof: L <:< UnionOrVoid[ElemLabels], tupleLens: TupleLens[ElemTypes, Tuples.IndexOf[ElemLabels, L]]): MirrorLens[T, ElemTypes, Tuples.IndexOf[ElemLabels, L], Tuple.Elem[ElemTypes, Tuples.IndexOf[ElemLabels, L]]] = {
      new MirrorLens(mirror, tupleLens)
    }
  }
}

class MirrorLens [T, ElemTypes <: Tuple, I <: Int, ElemType] (mirror: Mirror.ProductOf[T], tupleLens: TupleLens[ElemTypes, I]) extends Lens[T, ElemType] with Dynamic {
  override def runLens [F[_] : Functor] (a: T, f: ElemType => F[ElemType]): F[T] = {
    tupleLens.runLens(toTuple(a), f.asInstanceOf[Tuple.Elem[ElemTypes, I] => F[Tuple.Elem[ElemTypes, I]]]).map(fromTuple)
  }

  override def get (t: T): ElemType = {
    tupleLens.get(toTuple(t)).asInstanceOf[ElemType]
  }
  override def set (t: T)(e: ElemType): T = {
    fromTuple(tupleLens.set(toTuple(t))(e.asInstanceOf))
  }

  def selectDynamic [L <: Singleton] (label: L)(given m: Mirror.ProductOf[ElemType], proof: L <:< UnionOrVoid[m.MirroredElemLabels], lens: TupleLens[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]): Lens[T, Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]] = {
    val mirrorLens: Lens[ElemType, Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]] = new MirrorLens[ElemType, m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L], Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]](m, lens)
    mirrorLens.compose(this)
  }

  private def toTuple (a: T): ElemTypes = Tuple.fromProduct(a.asInstanceOf[Product]).asInstanceOf[ElemTypes]
  private def fromTuple (e: ElemTypes): T = mirror.fromProduct(e.asInstanceOf[Product])
}
