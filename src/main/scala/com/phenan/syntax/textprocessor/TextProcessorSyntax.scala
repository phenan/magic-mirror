package com.phenan.syntax.textprocessor

import com.phenan.classes._
import com.phenan.syntax.invariant._
import com.phenan.syntax.monoidal._

import MonoidalInvariantFunctorSyntax._

def [F[_], R] (sc: StringContext) w ()(using processor: TextProcessor[F], iso: Unit <=> R): F[R] = {
  val Seq(part0) = sc.parts
  discardLeft(processor.processString(part0), construct[F, Unit, R](()))
}

def [F[_], T1, R] (sc: StringContext) w (t1: F[T1])(using processor: TextProcessor[F], iso: Tuple1[T1] <=> R): F[R] = {
  val Seq(part0, part1) = sc.parts
  val f1 = discardRight(t1, processor.processString(part1))
  discardLeft(processor.processString(part0), construct[F, Tuple1[T1], R](Tuple1(f1)))
}

def [F[_], T <: Tuple, R] (sc: StringContext) w2 (ts: Tuple.Map[T, F])(using processor: TextProcessor[F], iso: T <=> R, joiner: TextProcessorJoiner[T, F]): F[R] = {
  val parts = sc.parts.map(processor.processString)
  val joined = joiner(ts, parts.tail)
  discardLeft(parts.head, processor.xmap(iso).from(joined))
}

opaque type TextProcessorJoiner [T <: Tuple, F[_]] = (Tuple.Map[T, F], Seq[F[Unit]]) => F[T]

given unitTextProcessorJoiner [F[_]] (using processor: TextProcessor[F]) : TextProcessorJoiner[Unit, F] = {
  (_, _) => processor.pure(())
}

given consTextProcessorJoiner [F[_], H, T <: Tuple] (using processor: TextProcessor[F], restJoiner: TextProcessorJoiner[T, F]) : TextProcessorJoiner[H *: T, F] = {
  (ts: Tuple.Map[H *: T, F], parts: Seq[F[Unit]]) => {
    processor.product(discardRight(ts.head, parts.head), restJoiner(ts.tail, parts.tail))
  }
}
