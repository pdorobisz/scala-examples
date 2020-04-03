package examples.techniques

/**
 * ADT - Algebraic Data Type - way of structuring data, it's a sum of product types.
 * Sum type - composed of different possible values, in Scala represented by sealed trait and case classes inheriting from it
 * Product type - multiple values stuck together, can be represented by tuples or classes (multiple fields)
 *
 * GADT - Generalized Algebraic Data Type, it's ADT parameterized by some phantom type
 *
 */
object Gadt extends App {

  ////// ADT
  // Shape - sum of two classes: Circle and Rectangle
  sealed trait Shape

  final case class Circle(radius: Double) extends Shape

  // Rectangle - product of two doubles
  final case class Rectangle(width: Double, height: Double) extends Shape

  // it can be easily used in pattern matching:
  def area(s: Shape): Double = s match {
    case Circle(radius) => Math.PI * radius * radius
    case Rectangle(width, height) => width * height
  }


  ////// GADT
  // It's ADT but parameterized by phantom type (there's no instance of it, it's there just to help compiler)
  sealed trait Expr[T]

  case class IntExpr(i: Int) extends Expr[Int]

  case class BoolExpr(b: Boolean) extends Expr[Boolean]

  def eval[T](e: Expr[T]): T = e match {
    case IntExpr(i) => i
    case BoolExpr(b) => b
  }

  // now compiler knows eval's return type in each case:
  val i: Int = eval(IntExpr(42))
  val b: Boolean = eval(BoolExpr(true))

  println(i)
  println(b)

  // if we used ADT here:
  sealed trait AdtExpr

  case class IntAdtExpr(i: Int) extends AdtExpr

  case class BoolAdtExpr(b: Boolean) extends AdtExpr

  // return type has to be common supertype of both expressions which is AnyVal
  def evalAdt(e: AdtExpr): AnyVal = e match {
    case IntExpr(i) => i
    case BoolExpr(b) => b
  }

  // now we don't know types
  val i2: AnyVal = evalAdt(IntAdtExpr(42))
  val b2: AnyVal = evalAdt(BoolAdtExpr(true))
}
