# dotty-generic
Implementation of `Generic` in Dotty

In Scala2, [shapeless](https://github.com/milessabin/shapeless) provides `Generic`.
`Generic` is a type class that expresses interconversion between algebraic data type and simple data type that is expressed with product and coproduct.
Shapeless implements it with macros since Scala2 language does not provide any useful information about such interconversion.

Dotty supports `Mirror` type class for supporting type class derivation.
The `Mirror` provides information about how to construct algebraic data type from product of its elements, so we can implement `Generic` without macros by utilizing `Mirror`.

## Author
[@phenan](https://twitter.com/phenan)
