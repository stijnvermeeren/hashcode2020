package challenge

import scala.io.Source

case class ProblemData(
  bookCount: Int,
  libraryCount: Int,
  days: Int,
  bookValues: Seq[Int],
  libraries: Library
)

case class Library(
  bookCount: Int,
  singUpTime: Int,
  booksPerDay: Int,
  books: Seq[Int]
)

object ProblemData {
  def readFromFile(path: String): ProblemData = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines()
    val line1 = lines.next().split(" ").map(Integer.parseInt)
    val line2 = lines.next().split(" ").map(Integer.parseInt)

    val libraries = for (i <- 0 until line1(1)) yield {
      val libraryLine1 = lines.next().split(" ").map(Integer.parseInt)
      val libraryLine2 = lines.next().split(" ").map(Integer.parseInt)

      Library(
        bookCount = libraryLine1(0),
        singUpTime = libraryLine1(1),
        booksPerDay = libraryLine1(2),
        books = libraryLine2
      )
    }

    bufferedSource.close

    ProblemData(
      bookCount = line1(0),
      libraryCount = line1(1),
      days = line1(2),
      bookValues = line2,
      libraries = libraries
    )
  }
}
