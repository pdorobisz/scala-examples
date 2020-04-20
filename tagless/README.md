# Tagless

## Expression problem (extensibility problem)

Structuring application in such a way that data model and set of operations can be extended without changing already
existing code.

Suppose we're describing shapes (e.g. Square, Circle) and implementing operations (e.g. area).

In functional programming we define operations as functions accepting all types of Shape so adding new operation
(e.g. perimeter) is easy - we just create new function. But adding new shape (e.g. Triangle) is harder - we have to
change already existing functions to handle newly added shape instance.

In object oriented programming operations are defined as object's methods so adding new class is easy but adding new
operation is harder because we have to add it to every class.

Tagless final is one of the approaches to this problem.


## Functional programming

FP consists of 3 parts: operations, programs and interpreters. It defines pure functions (referentially transparent) and
pushes side effects to the edges of our program.

## Encoding

We can encode available operations and expressions in various ways.
Below examples are based on https://w.pitula.me/presentations/2020-02-krakow-sug/#/.

### Initial encoding

```scala
// Algebra - operations are encoded as data (ADT):
sealed trait DBOps
case class SaveUser(user: User)         extends DBOps
case class FindUserById(id: String)     extends DBOps
case class Transaction(ops: Seq[DBOps]) extends DBOps

// Expressions as values:
val findId1: DBOps = FindUserById(1)
val transaction1: DBOps =
    Transaction(Seq(
        SaveUser(user1),
        FindUserById(user1.id)
    ))

// Interpreter is a function:
def show(exp: DBOps): String = exp match {
 ...
}

def usedUserIds(exp: DBOps): Seq[Int] = exp match {
 ...
}

// Interpretation is a function application (interpreter applied on expression):
val desc: String  = show(findId1)
val ids: Seq[Int] = usedUserIds(transaction1)
```

### Final encoding


```scala
// Algebra - operations are encoded as functions in interface:
trait DBOps[T] {
    def saveUser(user: User): T
    def findUserById(id: Int): T
    def transaction(ops: Seq[T]): T
}

// Expressions are encoded as functions:
def findId1[T](db: DBOps[T]): T = db.findUserById(1)
def transaction1[T](db: DBOps[T]): T =
    db.transaction(Seq(
        db.saveUser(user1),
        db.findUserById(user1.id)
    ))

// Interpreter is a value (interface implementation):
val show: DBOps[String] = new DBOps[String] {
    def saveUser(user: User): String = ...
    def findUserById(id: Int): String = ...
    def transaction(ops: Seq[String]): String = ...
}

// Interpretation is a function application (expression executed with interpreter):
val desc: String  = findId1(show)
val ids: Seq[Int] = transaction1(usedUserIds)
```

### Tagged encoding

Result encoded as ADT:

```scala
sealed trait Result
case class FindByIdResult(user: Option[User])   extends Result
case class SaveResult(id: Int)                  extends Result
case class TransResult(subResults: Seq[Result]) extends Result
```

Tagged initial encoding interpreter and interpretation:

```scala
def executeDBQuery(exp: DBOps): Result
val r: Result = executeDBQuery(findId1)
```

Tagged final encoding interpreter and interpretation:

```scala
val executeDBQuery: DBOps[Result]
val r: Result = findId1(executeDBQuery)
```

### Tagless encoding

Algebra parameterized with return type.


```scala
// Tagless initial encoding - algebra as GADT  : 
sealed trait DBOps[Result]
case class SaveUser(user: User)     extends DBOps[Int]
case class FindUserById(id: Int)    extends DBOps[Option[User]]
case class TransactionHL extends DBOps[HL]

// Expression:
val findId1: DBOps[Option[User]] = FindUserById(1)

// Interpreters are parameterized too:
def show[T](exp: DBOps[T]): String
def usedUserIds[T](exp: DBOps[T]): Seq[Int]
def executeDBQueryAsync[T](exp: DBOps[T]): Task[T]

// Interpretation:
val userOpt: Task[Option[User]] = executeDBQueryAsync(findId1)
```


```scala
// Tagless final encoding - operations as higher-kinded interface:
trait DBOps[F[_]] {
    def saveUser(user: User): F[Int]
    def findUserById(id: Int): F[Option[Int]]
}

// Expression:
def saveMyUserF[_]: F[Int] = db.saveUser(myUser)
def findId1F[_]: F[Option[User]] = db.findUserById(1)

// Interpreters:
val show: DBOps[Const[String, ?]]
val usedUserIds: DBOps[Const[Seq[Int], ?]]
val executeDBQueryAsync: DBOps[Task]

// Interpretation:
val userOpt: Task[Option[User]] = findId1(executeDBQueryAsync)
```

## Tagless final encoding

Tagless Final Encoding is a technique for embedding a DSL (Domain Specific Language). Tagless final style consists of:

* Algebras - set of operations, our language
* Interpreters - implementations of these operations

Some links:

* https://gist.github.com/fsat/cbfc04efd1997302f6e5ab112314024b
* https://gist.github.com/OlivierBlanvillain/48bb5c66dbb0557da50465809564ee80
* https://jproyo.github.io/posts/2019-02-07-practical-tagless-final-in-scala.html
* https://typelevel.org/blog/2017/12/27/optimizing-final-tagless.html
* https://degoes.net/articles/tagless-horror
* https://w.pitula.me/presentations/2020-02-krakow-sug/#/