package com.phenan.classes

import com.phenan.util._

trait Monoidal [F[_]] {
  def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B]
  def pure [A] (a: => A): F[A]
}
