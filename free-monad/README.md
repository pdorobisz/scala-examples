# Free monad

Steps to use free monad:

1. Define algebra (set of operations)
1. Define DSL by lifting algebra into Free monad
1. Implement interpreter(s)
1. Write programs using DSL
1. Execute program using interpreter

How it works:

* Seperates description from execution
* Encodes programs as a value
* Stack-safe: builds program description object on heap (internally each step is
stored as a piece of data, during interpretation it unpacks and runs them)
* Programs can be large which may cause GC pressure (may slow down program)
* No recursion - can deal with arbitrary number of steps as long as program object
fits in memory
* Lots of boilerplate
* It's possible to "compile" to lower-level algebra (interpreter can return
instructions of different algebra)
