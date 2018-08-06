package examples.scalaz

import examples.scalaz.utils.MyBox

import scalaz.Scalaz._
import scalaz._

/**
 * Applicative to funktor posiadajacy metode point (nazywana tez pure).
 * Applicative rozszerza Apply o pure (Apply rozszerza Functor, i definiuje ap, czyli <*>)
 * Pure bierze wartosc i zwraca Applicative z ta wartoscia.
 * Applicative musi spelniac warunek:
 * fMap = apply o pure (fMap - z funktora, apply - z applicative)
 */
object ScalazApplicative {

  implicit object MyBoxApplicative extends Applicative[MyBox] {
    override def point[A](a: => A): MyBox[A] = MyBox(a)

    override def ap[A, B](fa: => MyBox[A])(f: => MyBox[(A) => B]): MyBox[B] = MyBox(f.content(fa.content))

    // map (z funktora) jest zaimplementowane przy pomocy ap
  }

  def main(args: Array[String]) {
    // Applicative posiada point
    val intInList: List[Int] = 1.point[List]
    val intInOption: Option[Int] = 1.point[Option]
    val intInMyBox: MyBox[Int] = 1.point[MyBox]
    println(intInList, intInOption, intInMyBox)

    // posiada tez <*> zaimplementowane przy pomocy ap. Umozliwia wykorzystanie funkcji wieloargumentowych "opakowanych" w
    // ten sam "kontener" co argumenty
    val f = (x: Int) => (y: Int) => x + y + 10 // funkcja w postaci "curried"
    val applyToOption: Option[(Int) => Int] = Option(2) <*> Option(f) // otrzymamy funkcje jednoargumentowa
    val maybeInt: Option[Int] = Option(1) <*> (Option(2) <*> Option(f))
    val maybeInt2: MyBox[Int] = MyBox(1) <*> (MyBox(2) <*> MyBox(f)) // po zaimplementowaniu ap dla wlasnego typu
    println(maybeInt)
    println(maybeInt2)

    // zamiast "opakowywac" funkcje w kontener to mozemy uzyc ^ do zastosowania jej do dwoch wartosci opakowanych w kontenery
    // dla funkcji 3-argumentowej istnieje operator ^^, dla 4 - ^^^ itd. az do 7.
    val result: Option[Int] = ^(3.some, 5.some) {_ + _}
    // to samo przy uzyciu operatora |@|
    val result2: Option[Int] = (3.some |@| 5.some) {_ + _}
    val result3: Option[Int] = (3.some |@| 5.some |@| 6.some) {_ + _ + _}
    println(result, result2, result3)
  }
}
