package com.phenan.std

import com.phenan.classes._
import com.phenan.util.{given _, _}

given optionSemiringalFunctor : Semiringal[Option] with Functor[Option] {
  def map [A, B] (f: A => B): Option[A] => Option[B] = _.map(f)
  def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for (x <- a; y <- b) yield x *: y
  def pure [A] (a: => A): Option[A] = Some(a)
  def sum [A, B <: Tuple] (fa: => Option[A], fb: => Option[Coproduct.Of[B]]): Option[Coproduct.Of[A *: B]] = {
    fa.map(a => Coproduct.Of[A *: B](a)).orElse(fb.map(InRight(_)))
  }
  def zero: Option[CNil] = None
}
