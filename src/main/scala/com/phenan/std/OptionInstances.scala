package com.phenan.std

import com.phenan.classes._
import com.phenan.util._

given MonadPlus[Option] {
  def flatMap [A, B] (f: A => Option[B]): Option[A] => Option[B] = _.flatMap(f)
  def pure [A] (a: => A): Option[A] = Some(a)
  def branch [A, B <: Tuple, R] (a: => Option[A], b: => Option[Coproduct.Of[B]], f: Coproduct.Of[A *: B] => R): Option[R] = {
    a.map(x => f(InLeft(x))).orElse(b.map(y => f(InRight(y))))
  }
  def empty [A]: Option[A] = None
}
