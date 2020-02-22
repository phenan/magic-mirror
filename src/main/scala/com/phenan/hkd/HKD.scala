package com.phenan.hkd

import com.phenan.classes._
import com.phenan.generic._
import com.phenan.lens._
import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

type HKD [R <: Product, F[_]] = HKStruct[R, F]

object HKD {
  def apply[R <: Product, F[_]] (using mirror: Mirror.ProductOf[R])(value: Tuple.Map[mirror.MirroredElemTypes, F]): HKD[R, F] = {
    new HKTupledStruct[R, mirror.MirroredElemTypes, F](HKTuple(value))
  }
}

trait HKStruct [R <: Product, F[_]] extends Dynamic {
  def selectDynamic [L <: Singleton] (tag: L)(using mirror: Mirror.ProductOf[R], contains: Tuples.Contain[mirror.MirroredElemLabels, L], tupleLens: HKTupleLens[mirror.MirroredElemTypes, F, Tuples.IndexOf[mirror.MirroredElemLabels, L]]): F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]]
  def build (using mirror: Mirror.ProductOf[R], foldable: HKTupleFoldable[mirror.MirroredElemTypes], monoidalInv: MonoidalInvariantFunctor[F]): F[R]
}

class HKTupledStruct [R <: Product, T <: Tuple, F[_]] (underlying: HKTuple[T, F]) extends HKStruct[R, F] {
  def selectDynamic [L <: Singleton] (tag: L)(using mirror: Mirror.ProductOf[R], contains: Tuples.Contain[mirror.MirroredElemLabels, L], tupleLens: HKTupleLens[mirror.MirroredElemTypes, F, Tuples.IndexOf[mirror.MirroredElemLabels, L]]): F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]] = {
    tupleLens.get(underlying.asInstanceOf[HKTuple[mirror.MirroredElemTypes, F]]).asInstanceOf[F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]]]
  }
  def build (using mirror: Mirror.ProductOf[R], foldable: HKTupleFoldable[mirror.MirroredElemTypes], monoidalInv: MonoidalInvariantFunctor[F]): F[R] = {
    val generic = ProductGeneric[R, mirror.MirroredElemTypes](mirror)
    HKTuple.construct[mirror.MirroredElemTypes, R, F](underlying.asInstanceOf[HKTuple[mirror.MirroredElemTypes, F]])(using foldable, monoidalInv, generic)
  }
}
