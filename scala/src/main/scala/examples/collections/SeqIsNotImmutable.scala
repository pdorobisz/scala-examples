package examples.collections

/**
  * Scala definiuje aliasy dla typow uzywajac zazwyczaj wersji immutable dla kolekcji. Dzieki temu mozna napisac
  * Set(1,2,3) bez podawania pakietu. Wyjatkiem jest jednak Seq
  */
object SeqIsNotImmutable extends App {
  // Set jest aliasem dla immutable.Set
  def useSet(s: Set[Int]): Int = s.size

  println(useSet(scala.collection.immutable.Set(1, 2, 3)))
  // nie kompiluje sie gdy zly typ
  //println(useSet(scala.collection.mutable.Set(1, 2, 3)))

  // Seq jest aliasem dla scala.collection.Seq
  def useSeq(s: Seq[Int]): Int = s.size

  // mozna wiec uzyc wersji immutable
  println(useSeq(scala.collection.immutable.Seq(1, 2, 3, 4)))
  // jak i mutable (np. ArrayBufer):
  println(useSeq(scala.collection.mutable.Seq(1, 2, 3, 4)))
}
