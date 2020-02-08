package com.phenan.data

import com.phenan.classes._

opaque type Identity[T] = T

object Identity {
  def apply [T] (value: T): Identity[T] = value

  def runIdentity [T] (id: Identity[T]): T = id
}

given IdentityFunctor : Functor[Identity] {
  def map [A, B] (f: A => B): Identity[A] => Identity[B] = id => Identity(f(id))
}
