package com.esv.eff

package object domain {

  case class User(id: Int, name: String)

  case class Article(title: String, content: String, authors: Seq[User] = Nil)

}
