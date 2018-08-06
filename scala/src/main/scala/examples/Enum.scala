package examples

object Enum {

  def main(args: Array[String]) {
    // WeekDay
    println(WeekDay.withName("Wednesday"))
    println(WeekDay.values)
    println(WeekDay.Fri.toString)

    // WeekDay2
    println(WeekDay2.withName("Wed"))
    println(WeekDay2.Fri.toString)
  }
}

object WeekDay extends Enumeration {
  // definicja typu WeekDay
  type WeekDay = Value

  val Mon = Value("Mon")
  val Tue = Value("Tue")
  val Wed = Value("Wednesday")
  val Thu = Value("Thu")
  val Fri = Value("Fri")
  val Sat = Value("Sat")
  val Sun = Value("Sun")
}

object WeekDay2 extends Enumeration {
  type WeekDay = Value
  val Sun, Mon, Tue, Wed, Thu, Fri, Sat = Value
}


