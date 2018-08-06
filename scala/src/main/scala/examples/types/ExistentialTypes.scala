package examples.types

/**
 * Existential Types umozliwiaja ukrycie type variable uzywanej w konstruktorze typu.
 * Umozliwiaja wspolprace z wildcardami w Javie. Dzieki nim mozna zdefiniowac wymagania
 * dotyczace typu parametryzujacego typ generyczny (np. gorna lub dolna granica). W Scali zazwyczaj nie ma
 * potrzeby korzystania z nich gdyz mozna definiowac czy typ jest covariant/contravariant bezposrednio
 * w definicji klasy. Gdy nie ma takiej definicji (typ jest invariant) to mozna wlasnie uzyc Existential Types.
 */
object ExistentialTypes {
  def main(args: Array[String]) {
    val boxOfInts: Box[Int] = new Box(123)
    val boxOfStrings: Box[String] = new Box("hello")
    val boxOfAny: Box[Any] = new Box("any")

    printBox(boxOfInts)
    printBox(boxOfStrings)
    printBox(boxOfAny)

    // printCharBox(boxOfInts) // blad, Int nie jest podtypem CharSequence
    printCharBox(boxOfStrings)
    // printCharBox(boxOfAny) // blad, Any nie jest podtypem CharSequence

    printIntBox(boxOfInts)
    // printIntBox(boxOfStrings) // blad, String nie jest nadtypem Int
    printIntBox(boxOfAny)

    // printAnyBox(boxOfInts) // blad, wymagany jest Box[Any], a nie Box[Int]
    // printAnyBox(boxOfStrings) // blad, wymagany jest Box[Any], a nie Box[String]
    printAnyBox(boxOfAny)

    printStringBox(boxOfStrings)

    ////////////// Parametryzowanie typem parametryzowanym //////////////
    // 1. Lista klas konkretnego typu T
    var xx1: List[Class[T] forSome {type T}] = List(classOf[Int], classOf[String])
    var xx2: List[Class[T] forSome {type T}] = List(classOf[String], classOf[String])
    var xx3: List[Class[T] forSome {type T}] = List(classOf[Any], classOf[Any])

    // 2. To samo co wyzej:
    var xx4: List[Class[_]] = List(classOf[Int], classOf[String])
    var xx5: List[Class[_]] = List(classOf[String], classOf[String])
    var xx6: List[Class[_]] = List(classOf[Any], classOf[Any])

    // 3. Class[T forSome {type T}] oznacza Class[Any]
    // var xx7: List[Class[T forSome {type T}]] = List(classOf[Int], classOf[String]) // blad, to nie jest List[Class[Any]]
    // var xx8 : List[Class[T forSome{type T}]] = List(classOf[String],classOf[String]) // blad, to nie jest List[Class[Any]]
    var xx9: List[Class[T forSome {type T}]] = List(classOf[Any], classOf[Any])

    // 4. List[Class[T]] forSome {type T} - nadklasa wszystkich List[Class[T]] dla dowolnego T nie bedacego wildcardem ("_")
    // var xx10: List[Class[T]] forSome {type T} = List(classOf[Int], classOf[String]) // blad, bo to jest List[Class[_ >: String with Int]]
    var xx11: List[Class[T]] forSome {type T} = List(classOf[String], classOf[String]) // ok
    var xx12: List[Class[T]] forSome {type T} = List(classOf[Any], classOf[Any]) // ok
  }

  // Box[T] forSome {type T} - existential type oznaczajacy klase Box parametryzowana dowolnym typem,
  // mozna zapisac w skrocie jako: Box[_]
  // Box[T] forSome {type T} jest nadtypem dowolnego Boxa (np. Box[Int]).
  def printBox(box: Box[T] forSome {type T}) = println("box contains: " + box.value)

  // [T forSome {type T}] oznacza po prostu Any, czyli ta metoda przyjmuje tylko Box[Any]
  def printAnyBox(box: Box[T forSome {type T}]) = println("any box contains: " + box.value)

  // Box[T forSome {type T <: String}] oznacza, ze metoda przyjmuje tylko Box zawierajacy String
  def printStringBox(box: Box[T forSome {type T <: String}]) = println("string box contains: " + box.value)

  // Gorna granica dla Existential type - metoda przyjmuje dowolny Box parametryzowany CharSequence lub jego podklasa.
  // Mozna tez zapisac jako: Box[_ <: CharSequence]
  def printCharBox(box: Box[T] forSome {type T <: CharSequence}) = println("char box contains: " + box.value)

  // Dolna granica dla Existential type, mozna tez zapisac jako: Box[_ >: Int]
  def printIntBox(box: Box[T] forSome {type T >: Int}) = println("int box contains: " + box.value)

  class Box[T](val value: T)

}
