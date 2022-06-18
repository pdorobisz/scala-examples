package examples.classes

/*
`abstract override` is a modifier that has to be added to traits in certain situations. This allows implementing
"stackable trait pattern" by providing stackable modifications to underlying core classes or traits.
This pattern is similar to decorator pattern but happens on class level during compile time.
 */
object AbstractOverride extends App {

  // Some behaviour
  trait Hello {
    def sayHello(name: String): String

    def bye(name: String): String = s"Bye $name"
  }

  // Class that provides basic functionality
  class GoodMorning extends Hello {
    override def sayHello(name: String): String = s"Good morning, $name"
  }

  // Stackable trait that modifies core functionality. It has to be mixed in to base implementation
  trait UpperCaseHello extends Hello {
    /*
    When overriding method accesses abstract method from a parent trait or class (`super.hello`) it has to be defined
    as `abstract override`.
     */
    abstract override def sayHello(name: String): String = super.sayHello(name).toUpperCase

    // `abstract` not required because base `super.bye` is not abstract (it's already implemented)
    override def bye(name: String): String = super.bye(name).toUpperCase
  }

  // Another stackable trait
  trait HowAreYouHello extends Hello {
    abstract override def sayHello(name: String): String = super.sayHello(name) + ", how are you today?"
  }

  // use basic functionality
  println(new GoodMorning().sayHello("John"))
  // use modified functionality by mixing in stacking modification
  println((new GoodMorning() with UpperCaseHello).sayHello("John"))
  // we can mix in multiple traits but the order of mixins is important:
  // prints: GOOD MORNING, JOHN, how are you today?
  println((new GoodMorning() with UpperCaseHello with HowAreYouHello).sayHello("John"))
  // prints: GOOD MORNING, JOHN, HOW ARE YOU TODAY?
  println((new GoodMorning() with HowAreYouHello with UpperCaseHello).sayHello("John"))
}
