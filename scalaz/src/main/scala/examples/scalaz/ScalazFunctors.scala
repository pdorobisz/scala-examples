package examples.scalaz

import examples.scalaz.utils.MyBox

import scala.language.higherKinds
import scalaz.Scalaz._
import scalaz._

/**
 * Funktory pozwalaja na zastosowanie funkcji do wartosci znajdujacej sie wewnatrz jakiegos kontenera
 * (np. Int wewnatrz Option).
 */
object ScalazFunctors {

  // Funktor dostarcza metode 'map' ktora umozliwia zastosowanie funkcji A=>B na typie M[A] czego wynikiem jest M[B].
  implicit object MyBoxFunctor extends Functor[MyBox] {
    override def map[A, B](fa: MyBox[A])(f: (A) => B): MyBox[B] = MyBox(f(fa.content))
  }

  def main(args: Array[String]) {
    // Scalaz definiuje funktory dla wielu typow, m.in. dla tuple (funkcja stosowana tylko do ostaniego elementu!)
    val mappedTuple: (Int, Int, Int) = (1, 2, 3) map (_ + 1)
    println(mappedTuple)

    // dla innych typow trzeba zdefiniowac funktor jako implicit
    val mappedBox: MyBox[String] = MyBox(123) map ("string<" + _.toString + ">")
    println(mappedBox)

    // zostanie przekazany funktor zdefiniowany w Scalaz
    val stringOption: Option[String] = useFunctor(Option(123), (i: Int) => i.toString)
    println(stringOption)

    // utworzenie funkcji MyBox[Int] => MyBox[String]
    val f: (Int) => String = "string<" + _.toString + ">"
    val intBoxToStrBox: (MyBox[Int]) => MyBox[String] = MyBoxFunctor.map[Int, String](_)(f)
    println(intBoxToStrBox(MyBox(1234)))

    // komponowanie funktorow - mozna stworzyc funktor, ktory odnosi sie do wartosci w Option znajdujacych sie w List
    val composed = Functor[List] compose Functor[Option]
    val composedResult: List[Option[Int]] = composed.map(List(Some(1), None, Some(3)))(_ + 1)
    println(composedResult)
  }

  def useFunctor[A, B, M[_]](a: M[A], f: A => B)(implicit functor: Functor[M]): M[B] = functor.map(a)(f)
}
