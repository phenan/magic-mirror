package com.phenan.std

import com.phenan.classes._

given MonadPlus[Option] {
  def flatMap [A, B] (f: A => Option[B]): Option[A] => Option[B] = _.flatMap(f)
  def pure [A] (a: => A): Option[A] = Some(a)
  def plus [A] (a: => Option[A], b: => Option[A]): Option[A] = a.orElse(b)
  def empty [A]: Option[A] = None
}
