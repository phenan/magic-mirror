package com.phenan.syntax.textprocessor

import com.phenan.classes._
import com.phenan.generic._
import com.phenan.hkd._
import com.phenan.syntax.invariant._

import MonoidalInvariantFunctorSyntax._

def [F[_], R <: Product] (sc: StringContext) w ()(using TextProcessor[F, _], ProductGeneric[R, Unit]): F[R] = {
  buildProcessor(sc, HKTuple[Unit, F](()))
}

def [F[_], T1, R <: Product] (sc: StringContext) w (t1: F[T1])(using TextProcessor[F, _], ProductGeneric[R, Tuple1[T1]]): F[R] = {
  buildProcessor(sc, HKTuple[Tuple1[T1], F](Tuple1(t1)))
}

def [F[_], T1, T2, R <: Product] (sc: StringContext) w (t1: F[T1], t2: F[T2])(using TextProcessor[F, _], ProductGeneric[R, (T1, T2)]): F[R] = {
  buildProcessor(sc, HKTuple[(T1, T2), F](t1, t2))
}

def [F[_], T1, T2, T3, R <: Product] (sc: StringContext) w (t1: F[T1], t2: F[T2], t3: F[T3])(using TextProcessor[F, _], ProductGeneric[R, (T1, T2, T3)]): F[R] = {
  buildProcessor(sc, HKTuple[(T1, T2, T3), F](t1, t2, t3))
}

def [F[_], T1, T2, T3, T4, R <: Product] (sc: StringContext) w (t1: F[T1], t2: F[T2], t3: F[T3], t4: F[T4])(using TextProcessor[F, _], ProductGeneric[R, (T1, T2, T3, T4)]): F[R] = {
  buildProcessor(sc, HKTuple[(T1, T2, T3, T4), F](t1, t2, t3, t4))
}

def [F[_], T1, T2, T3, T4, T5, R <: Product] (sc: StringContext) w (t1: F[T1], t2: F[T2], t3: F[T3], t4: F[T4], t5: F[T5])(using TextProcessor[F, _], ProductGeneric[R, (T1, T2, T3, T4, T5)]): F[R] = {
  buildProcessor(sc, HKTuple[(T1, T2, T3, T4, T5), F](t1, t2, t3, t4, t5))
}

def [F[_], T1, T2, T3, T4, T5, T6, R <: Product] (sc: StringContext) w (t1: F[T1], t2: F[T2], t3: F[T3], t4: F[T4], t5: F[T5], t6: F[T6])(using TextProcessor[F, _], ProductGeneric[R, (T1, T2, T3, T4, T5, T6)]): F[R] = {
  buildProcessor(sc, HKTuple[(T1, T2, T3, T4, T5, T6), F](t1, t2, t3, t4, t5, t6))
}

private def buildProcessor [F[_], T <: Tuple, R <: Product] (sc: StringContext, ts: HKTuple[T, F])(using processor: TextProcessor[F, _], productGeneric: ProductGeneric[R, T], joiner: TextProcessorJoiner[T, F]): F[R] = {
  val parts = sc.parts.map(processor.processString)
  val joined = joiner(ts, parts.tail)
  discardLeft(parts.head, processor.xmap(productGeneric.toIso).to(joined))
}

opaque type TextProcessorJoiner [T <: Tuple, F[_]] = (HKTuple[T, F], Seq[F[Unit]]) => F[T]

given unitTextProcessorJoiner [F[_]] (using processor: TextProcessor[F, _]) : TextProcessorJoiner[Unit, F] = {
  (_, _) => processor.pure(())
}

given consTextProcessorJoiner [F[_], H, T <: Tuple] (using processor: TextProcessor[F, _], restJoiner: TextProcessorJoiner[T, F]) : TextProcessorJoiner[H *: T, F] = {
  (ts: HKTuple[H *: T, F], parts: Seq[F[Unit]]) => {
    processor.product(discardRight(HKTuple.headOf(ts), parts.head), restJoiner(HKTuple.tailOf(ts), parts.tail))
  }
}
