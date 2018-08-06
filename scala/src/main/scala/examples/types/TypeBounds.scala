package examples.types

object TypeBounds {

  // gorne ograniczenie dla typu
  def upperTypeBound[BB <: B](x: BB): B = x

  // to samo co wyzej z uzyciem generalized type constraints:
  def upperTypeBound2[AA](x: AA)(implicit ev: AA <:< B): B = x

  // Dolne ograniczenie dla typu. BB stanie sie typem bedacym wspolnym nadtypem typu B i typu argumentu x.
  def lowerTypeBound[BB >: B](x: BB) = x

  // to samo co wyzej z uzyciem generalized type constraints:
  def lowerTypeBound2[BB](x: BB)(implicit ev: B <:< BB) = x

  // dla typu AA musi byc zdefiniowana konwersja do typu A ( operator <% jest deprecated)
  def viewBound[AA](x: AA)(implicit converter: AA => A): A = converter(x)

  // Oznacza, ze dla typu X istnieje typ M[A], aby dostac sie do argumentu typu M[A] nalezy uzyc implicitly[M[A]]
  def contextBound[X: M](x: X) = x

  // to samo co wyzej:
  def contextBound2[X](x: X)(implicit ev: M[X]) = x

  def main(args: Array[String]) {
    val a: A = new A
    val b: B = new B
    val c: C = new C
    val d: D = new D
    implicit val ma: M[A] = new M[A] {}

    // upperTypeBound(a) // nie skompiluje sie
    upperTypeBound(b)
    upperTypeBound(c)
    upperTypeBound2(c)

    val lowerBound1: B = lowerTypeBound(c) // wspolna nadklasa B i C to B
    // lowerTypeBound2(c) // nie skompiluje sie
    val lowerBound2: B = lowerTypeBound(b) // wspolna nadklasa B i B to B
    val lowerBound3: A = lowerTypeBound(a) // wspolna nadklasa B i A to A
    val lowerBound4: Object = lowerTypeBound(d) // wspolna nadklasa B i D to Object
    // lowerTypeBound2(d) // nie skompiluje sie
    val lowerBound5: Any = lowerTypeBound(123) // wspolna nadklasa B i Int to Any
    // lowerTypeBound2(123) // nie skompiluje sie

    viewBound(d)

    contextBound(a)
  }

  trait M[X]

  class A

  class B extends A

  class C extends B

  class D

  implicit def d2a(d: D): A = new A

}
