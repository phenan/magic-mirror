package com.phenan.lens

import com.phenan.classes._
import com.phenan.data._

trait Lens [A, B] {
  def runLens [F[_] : Functor] (a: A, f: B => F[B]): F[A]

  def get (a: A): B = Const.runConst(runLens[[X] =>> Const[B, X]](a, b => Const[B, B](b)))
  def set (a: A)(b: B): A = modify(a)(_ => b)

  def modify (a: A)(f: B => B): A = Identity.runIdentity(runLens[Identity](a, b => Identity(f(b))))

  def compose [C] (that: Lens[C, A]): Lens[C, B] = new ComposedLens(that, this)
}

class StandardLens [A, B] (getter: A => B, setter: A => B => A) extends Lens[A, B] {
  override def runLens [F[_] : Functor] (a: A, f: B => F[B]): F[A] = f(getter(a)).map(setter(a))

  override def get (a: A): B = getter(a)
  override def set (a: A)(b: B): A = setter(a)(b)
}

class ComposedLens [A, B, C] (lens1: Lens[A, B], lens2: Lens[B, C]) extends Lens[A, C] {
  override def runLens [F[_] : Functor] (a: A, f: C => F[C]): F[A] = {
    lens1.runLens(a, lens2.runLens(_, f))
  }
}
