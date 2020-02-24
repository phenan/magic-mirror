package com.phenan.classes

trait Semigroupal [F[_]] {
  def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B]
}
