package com.phenan.classes

trait Iso [=>:[_, _], A, B] {
  def from: A =>: B
  def to: B =>: A
}

object Iso {
  def apply[=>:[_, _], A, B](f: A =>: B)(g: B =>: A): Iso[=>:, A, B] = new Iso[=>:, A, B] {
    def from: A =>: B = f
    def to: B =>: A = g
  }
}

type <=> [A, B] = Iso[Function1, A, B]

given identityIsomorphism[A]: <=> [A, A] = new Iso[Function1, A, A] {
  def from: A => A = identity
  def to: A => A = identity
}
