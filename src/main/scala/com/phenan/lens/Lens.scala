package com.phenan.lens

trait Lens [A, B] { self =>
  def get (a: A): B
  def set (a: A)(b: B): A

  def modify (a: A)(f: B => B): A = set(a)(f(get(a)))

  def compose [C] (that: Lens[C, A]): Lens[C, B] = new Lens {
    def get (c: C): B = self.get(that.get(c))
    def set (c: C)(b: B): C = that.set(c)(self.set(that.get(c))(b))
  }
}
