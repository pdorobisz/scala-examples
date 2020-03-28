package examples

import scala.language.implicitConversions

/**
 * Implicits - feature that enables compiler to automatically pass values or automatically convert from one type to other.
 * There are different use cases:
 * - implicit conversion
 * - implicit parameters
 * - context bounds
 */
object Implicits extends App {

  /**
   * Implicit conversion enables automatically converting one type to the other to make some expression valid. For example:
   * E.x, where E - some expression of type T, x - field/method not defined in T compiler will automatically insert conversion I
   * so I(E).x is valid (I converts expression of type T to different type that has x defined)
   */
  def implicitConversionDemo() = {
    // Implicit conversion pattern is implemented by implicit method with non-implicit parameters:
    implicit def intToString(d: Int) = new String("conversion of " + d)

    // compiler automatically inserts conversion to make it compile:
    // intToString(67).toUpperCase
    println(67.toUpperCase)

    def concat(s1: String, s2: String) = s1 + "::" + s2

    // implicit conversion is also used to convert method parameters:
    println(concat(12, 34))

    /**
     * Implicit class - conversion can be also implemented as a class, main constructor of this class becomes a method that
     * does conversion. Limitations:
     * - can be defined only inside trait/class/object
     * - accepts exactly one non-implicit argument
     * - other method/field/object with same name can't be defined in the scope (case class can't be implicit class because
     * companion object with same name is automatically created for it)
     */
    implicit class ImprovedString(s: String) {
      println("creating ImprovedString for: " + s)

      def double = s + s
    }

    // double is defined in ImprovedString so compiler automatically adds code that creates ImprovedString instance from given string:
    // new ImprovedString("hello").double
    println("hello".double)
  }

  /**
   * Implicit parameters - compiler automatically tries to fill in parameters defined as implicit
   */
  def implicitParametersDemo(): Unit = {
    def add[T](x: Int)(implicit y: Int): Unit = {
      println(x + y)
    }

    implicit val i = 10

    // compiler will automatically use i as a second argument
    add(2)
  }

  /**
   * Implicit parameter can be used in implicit conversion
   */
  def implicitConversionAsImplicitParameterDemo(): Unit = {
    // compiler will use conv function to implicitly convert input parameter to Seq so it can call indexOf method on it
    def getIndex[T, I](input: I, value: T)(implicit conv: I => Seq[T]) = input.indexOf(value)

    // I is String so compiler automatically inserts wrapString (defined in Predef) that converts String to WrappedString
    // which implements Seq
    getIndex("abc", 'a')
  }

  /**
   * Context bound describes implicit value of some parameterized type (F[_]) for method's type parameter T (F[T] instance).
   * Often used with type class pattern
   */
  def contextBoundsDemo(): Unit = {
    class A[T](val x: T)
    // An instance of A[T] has to be defined in the scope. This is a syntactic sugar for:
    // f[T]()(implicit ev: A[T])
    def f[T: A]() = {
      val value = implicitly[A[T]] // we have to use implicitly to access implicit parameter
      println(value.x)
    }

    implicit val aForString = new A("test A")
    // f requires implicit A[String] instance so aForString is inserted
    f[String]()

    // To access implicit parameter defined by context bound we have to use implicitly which looks ugly. Context bounds
    // are useful when we have to pass implicit value to other method.
    class B[T](val x: T)
    def h[T](implicit b: B[T]) = println(b.x)

    // g doesn't use B[T] instance (so no need to call implicitly) but it makes it available to h which requires implicit
    // parameter of type B[T]
    def g[T: B]() = h

    implicit val bForString = new B("test B")
    g[String]()

  }

  implicitConversionDemo()
  implicitParametersDemo()
  implicitConversionAsImplicitParameterDemo()
  contextBoundsDemo()
}
