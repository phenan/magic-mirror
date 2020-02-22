package com.phenan.hkd

import com.phenan.lens._
import com.phenan.util._

import scala.deriving._
import scala.language.dynamics

type HKD [R <: Product, F[_]] = HKStruct[R, F]

object HKD {
  def apply[R <: Product, F[_]] (using mirror: Mirror.ProductOf[R])(value: Tuple.Map[mirror.MirroredElemTypes, F]): HKD[R, F] = {
    new HKTupledStruct[R, mirror.MirroredElemTypes, F](value)
  }
}

trait HKStruct [R <: Product, F[_]] extends Dynamic {
  def selectDynamic [L <: Singleton] (tag: L)(using mirror: Mirror.ProductOf[R], contains: Tuples.Contain[mirror.MirroredElemLabels, L], tupleLens: TupleLens[Tuple.Map[mirror.MirroredElemTypes, F], Tuples.IndexOf[mirror.MirroredElemLabels, L]]): F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]]
}

class HKTupledStruct [R <: Product, T <: Tuple, F[_]] (underlying: Tuple.Map[T, F]) extends HKStruct[R, F] {
  def selectDynamic [L <: Singleton] (tag: L)(using mirror: Mirror.ProductOf[R], contains: Tuples.Contain[mirror.MirroredElemLabels, L], tupleLens: TupleLens[Tuple.Map[mirror.MirroredElemTypes, F], Tuples.IndexOf[mirror.MirroredElemLabels, L]]): F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]] = {
    tupleLens.get(underlying.asInstanceOf[Tuple.Map[mirror.MirroredElemTypes, F]]).asInstanceOf[F[Tuple.Elem[mirror.MirroredElemTypes, Tuples.IndexOf[mirror.MirroredElemLabels, L]]]]
  }
}
