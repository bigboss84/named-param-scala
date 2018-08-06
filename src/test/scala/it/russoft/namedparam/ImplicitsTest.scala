package it.russoft.namedparam

import it.russoft.namedparam.Implicits._
import org.scalatest._

class ImplicitsTest extends FlatSpec with Matchers {

  /*
   * Expansion from Map[String, Any]
   */

  "byte value" should "be unquoted" in {
    ":byte" << Map("byte" -> 1.toByte) shouldBe "1"
  }

  "short value" should "be unquoted" in {
    ":short" << Map("short" -> 1.toShort) shouldBe "1"
  }

  "int value" should "be unquoted" in {
    ":int" << Map("int" -> 1) shouldBe "1"
  }

  "long value" should "be unquoted" in {
    ":long" << Map("long" -> 1L) shouldBe "1"
  }

  "float value" should "be unquoted" in {
    ":float" << Map("float" -> 1.2f) shouldBe "1.2"
  }

  "double value" should "be unquoted" in {
    ":double" << Map("double" -> 1.2) shouldBe "1.2"
  }

  "char value" should "be quoted" in {
    ":char" << Map("char" -> 'c') shouldBe "'c'"
  }

  "string value" should "be quoted" in {
    ":string" << Map("string" -> "abc") shouldBe "'abc'"
  }

  "boolean true value" should "be true" in {
    ":boolean" << Map("boolean" -> true) shouldBe "true"
  }

  "boolean false value" should "be false" in {
    ":boolean" << Map("boolean" -> false) shouldBe "false"
  }

  "some value" should "be evaluated" in {
    ":someOfInt" << Map("someOfInt" -> Some(1)) shouldBe "1"
    ":someOfChar" << Map("someOfChar" -> Some('c')) shouldBe "'c'"
  }

  "none value" should "be null" in {
    ":none" << Map("none" -> None) shouldBe "null"
  }

  "null value" should "be null" in {
    ":null" << Map("null" -> null) shouldBe "null"
  }

  "any value" should "be equal to its string representation" in {
    class Foo {
      override def toString: String = "foo"
    }
    ":any" << Map("any" -> new Foo) shouldBe "'foo'"
  }

  /*
   * Expansion from Product
   */

  "byte value in product" should "be unquoted" in {
    case class Model(byte: Byte = 1)
    ":byte" <<< new Model shouldBe "1"
  }

  "short value in product" should "be unquoted" in {
    case class Model(short: Short = 1.toShort)
    ":short" <<< new Model shouldBe "1"
  }

  "int value in product" should "be unquoted" in {
    case class Model(int: Int = 1)
    ":int" <<< new Model shouldBe "1"
  }

  "long value in product" should "be unquoted" in {
    case class Model(long: Long = 1L)
    ":long" <<< new Model shouldBe "1"
  }

  "float value in product" should "be unquoted" in {
    case class Model(float: Float = 0.1f)
    ":float" <<< new Model shouldBe "0.1"
  }

  "double value in product" should "be unquoted" in {
    case class Model(double: Double = 0.1)
    ":double" <<< new Model shouldBe "0.1"
  }

  "char value in product" should "be quoted" in {
    case class Model(char: Char = 'c')
    ":char" <<< new Model shouldBe "'c'"
  }

  "string value in product" should "be quoted" in {
    case class Model(string: String = "abc")
    ":string" <<< new Model shouldBe "'abc'"
  }

  "boolean true value in product" should "be true" in {
    case class Model(boolean: Boolean = true)
    ":boolean" <<< new Model shouldBe "true"
  }

  "boolean false value in product" should "be false" in {
    case class Model(boolean: Boolean = false)
    ":boolean" <<< new Model shouldBe "false"
  }

  "some-of-int value in product" should "be unquoted" in {
    case class Model(someOfInt: Some[Int] = Some(1))
    ":someOfInt" <<< new Model shouldBe "1"
  }

  "some-of-string value in product" should "be unquoted" in {
    case class Model(someOfString: Option[String] = Some("foo"))
    ":someOfString" <<< new Model shouldBe "'foo'"
  }

  "none value in product" should "be unquoted" in {
    case class Model(none: Option[Int] = None)
    ":none" <<< new Model shouldBe "null"
  }

  "null value in product" should "be unquoted" in {
    case class Model(nullAny: Any = null)
    ":nullAny" <<< new Model shouldBe "null"
  }

  "any value in product" should "be unquoted" in {
    class Foo {
      override def toString: String = "foo"
    }
    case class Model(any: Foo = new Foo)
    ":any" <<< new Model shouldBe "'foo'"
  }

  "multiple any values in product" should "be as expected" in {
    class Foo {
      override def toString: String = "foo"
    }
    case class Model(int: Int = 7, char: Char = 'e', any: Foo = new Foo)
    ":int :char :any" <<< new Model shouldBe "7 'e' 'foo'"
  }
}
