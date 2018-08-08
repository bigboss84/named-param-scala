package it.russoft.namedparam

/**
  * Allows to define custom conversion for a particular type.
  */
class NamedParamConverter[+T](f: T => Any) {
  def convert(a: Any): Any = f(a.asInstanceOf[T])
}
