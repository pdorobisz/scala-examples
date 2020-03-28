package examples

object SpecialMethods {
  def main(args: Array[String]) {
    var test: Test = new Test

    // metoda apply
    test("hello")

    // metoda update
    test("abc") = 123

    // metoda field_= (setter)
    test.field = "some value"

    // metoda field (getter)
    test.field

    // metoda unary_+
    +test

    // to samo co (pod warunkiem ze nie jest zdefiniowana metoda +=): test = test.+(1)
    test += 1

    test match {
      // metoda unapply (do s zostanie przypisana wartosc zwrocona przez unapply w Some)
      case Test(s) => println(s)
    }
  }

  class Test {
    def apply(x: String) = println(s"apply($x)")

    def update(x: String, y: Int) = println(s"update($x, $y)")

    def field_=(x: String) {
      println(s"field_=($x)")
    }

    def field = {
      println(s"field")
      "field value"
    }

    def +(x: Int) = {
      println("+")
      this
    }

    // operator, ktory moze byc uzyty jako prefiks, tak samo dla: unary_-, unary_!, unary_~
    def unary_+() {
      println("unary_+")
    }
  }

  object Test {
    // metoda uzywana przy pattern matchingu
    def unapply(t: Test) = {
      println("unapply")
      Some("pattern matched value")
    }

    // aby dopasowac zmienna liczbe wartosci uzywa sie unapplySeq zamiast unapply
    //    def unapplySeq(t: Test) = {
    //      println("unapplySeq")
    //      Some(List("value1", "value2", "value3"))
    //    }
  }

}
