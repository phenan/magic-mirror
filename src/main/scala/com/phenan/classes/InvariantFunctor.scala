package com.phenan.classes

trait InvariantFunctor [F[_]] {
  def xmap [A, B] (f: A <=> B): F[A] <=> F[B]
}
