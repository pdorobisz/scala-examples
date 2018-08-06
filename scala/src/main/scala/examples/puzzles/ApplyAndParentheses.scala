package examples.puzzles

/*
Brak listy argumentow w deklaracji metody moze zmienic
 */
object ApplyAndParentheses extends App {

  // Poniewaz m nie ma listy argumentow to ponizszy kod oznacza wywolanie apply na wartosci zwroconej przez m.
  // to samo co: (new A().m).apply()
  new A().m()

  // Dodanie pustej listy argumentow zmienia rezultat - tutaj wywolana jest tylko metoda m
  new B().m()


  class A {
    def m: A = {
      println("called A.m")
      this
    }

    def apply(): Unit = {
      println("called A.apply")
    }
  }

  class B {
    def m(): B = {
      println("called B.m")
      this
    }

    def apply(): Unit = {
      println("called B.apply")
    }
  }
}
