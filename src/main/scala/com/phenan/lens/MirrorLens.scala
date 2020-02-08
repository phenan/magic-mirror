package com.phenan.lens

import com.phenan.classes._
import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

object MirrorLens {
  def apply [T <: Product]: MirrorLens[T, T] = new MirrorLens(new StandardLens[T, T] (identity, _ => identity))
}

class MirrorLens [T, E] (lens: Lens[T, E]) extends Lens[T, E] with Dynamic {
  export lens._

  def selectDynamic [L <: Singleton] (label: L)(using m: Mirror.ProductOf[E], contain: Tuples.Contain[m.MirroredElemLabels, L], tupleLens: TupleLens[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]): MirrorLens[T, Tuple.Elem[m.MirroredElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]] = {
    type ElemTypes = m.MirroredElemTypes
    type ElemType = Tuple.Elem[ElemTypes, Tuples.IndexOf[m.MirroredElemLabels, L]]

    def toTuple (a: E): ElemTypes = Tuple.fromProduct(a.asInstanceOf[Product]).asInstanceOf[ElemTypes]
    def fromTuple (e: ElemTypes): E = m.fromProduct(e.asInstanceOf[Product])

    def get (t: E): ElemType = tupleLens.get(toTuple(t)).asInstanceOf[ElemType]
    def set (t: E)(e: ElemType): E = fromTuple(tupleLens.set(toTuple(t))(e.asInstanceOf))
    
    new MirrorLens(new StandardLens[E, ElemType](get, set).compose(this))  
  }
}
