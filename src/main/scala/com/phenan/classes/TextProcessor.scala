package com.phenan.classes

trait TextProcessor [F[_], E] extends Semiringal[F] with InvariantFunctor[F] {
  def processString (string: String): F[Unit]
  def satisfy (condition: E => Boolean): F[E]
}
