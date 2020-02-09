package com.phenan.classes

trait Semiringal [F[_]] extends Monoidal[F] {
  def sum [A, B] (a: => F[A], b: => F[B]): F[A | B]
  def zero: F[Void] 
}
