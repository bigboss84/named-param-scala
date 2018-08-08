package it.russoft.namedparam

/**
  * Provides helpers to replace named parameters
  * into a string according to the standard SQL syntax.
  */
object Implicits {

  type Converter = List[(Class[_], NamedParamConverter[Any])]

  implicit class NamedParam(s: String)
                           (implicit converters: Converter = Nil) {

    /**
      * Replaces all named parameters into this string with
      * corresponding values in `param`.
      *
      * @example {{{"select * from a where id = :id" << Map("id" -> 22)}}}
      * @param param Mapping for parameter names where the keys
      *              are the parameter names.
      * @return Returns new string with the replaced names with
      *         their corresponding values defined in `param`.
      */
    def <<(param: Map[String, Any]): String = {
      def replace(s: String, m: Map[String, Any]): String = {
        if (m.isEmpty) s
        else {
          val v = m.head._2
          val e = expand {
            if (v == null) v // null value will be handled in conversion
            else converters.find(_._1 == v.getClass).map(_._2.convert(v)).getOrElse(v)
          }
          replace(s.replaceAll(s":${m.head._1}", e), m.tail)
        }
      }

      replace(s, param)
    }

    /**
      * Replaces all named parameters into this string with
      * corresponding values in `p` fields.
      *
      * @example {{{
      *   case class A(id: Int)
      *   "select * from a where id = :id" << A(22)
      * }}}
      * @param p An instance of [[Product]] whose fields names
      *          are intended as parameter names.
      * @return Returns new string with the replaced names with
      *         their corresponding values defined in `p`.
      */
    def <<<(p: Product): String = {
      val values = p.productIterator
      s << p.getClass.getDeclaredFields
        .filterNot(_.isSynthetic).map(_.getName -> values.next).toMap
    }

    private def expand(a: Any): String = a match {
      // boxed values
      case s: Some[_] => expand(s.get)
      // null values
      case None | null => "null"
      // unquoted
      case _: Byte | _: Short | _: Int | _: Long | _: Float | _: Double | _: Boolean => a.toString
      // quoted
      case _: Char | _: String | _: Any => s"'$a'"
    }
  }

}
