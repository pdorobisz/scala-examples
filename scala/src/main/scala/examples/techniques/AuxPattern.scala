package examples.techniques

/**
  * Umozliwia zdefiniowanie relacji miedzy typami i obejscie ograniczen narzucanych przez kompilator.
  */
object AuxPattern {

  // trait wewnatrz ktorego znajduje sie type alias
  trait Foo[A] {
    type B // type alias

    def value: B
  }

  implicit def fi = new Foo[Int] {
    type B = String
    val value = "Foo"
  }

  implicit def fs = new Foo[String] {
    type B = Boolean
    val value = false
  }

  // mozna uzyc f.B jako typu parametryzujacego typ na innej liscie argumentow
  def test[A](f: Foo[A])(o: Option[f.B]): f.B = f.value

  // nie mozna jednak uzyc w tej samej liscie argumentow
  //  def test2[A](f: Foo[A], o: Option[f.B]): f.B = f.value

  // aby ominac to ograniczenie nalezy zdefiniowac pomocniczy typ Aux (najlepiej wewnatrz Foo)

  type Aux[AA, BB] = Foo[AA] {type B = BB}

  def test2[A, B](f: Aux[A, B], o: Option[B]): B = f.value

  def main(args: Array[String]): Unit = {
    val a: Aux[Int, String] = fi
    test2(fi, Some(fi.value))
  }
}
