package com.phenan.data

import com.phenan.classes._

opaque type Const[C, T] = C

object Const {
  def apply [C, T] (value: C): Const[C, T] = value

  def runConst [C, T] (c: Const[C, T]): C = c
}

given ConstFunctor[C]: Functor[[X] =>> Const[C, X]] {
  def map [A, B] (f: A => B): Const[C, A] => Const[C, B] = identity
}
