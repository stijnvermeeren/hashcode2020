package challenge

import scala.io.Source

case class ProblemData(
  bookCount: Int,
  libraryCount: Int,
  days: Int,
  bookValues: Seq[Int],
  libraries: Seq[Library]
)

case class Library(
  id: Int,
  bookCount: Int,
  singUpTime: Int,
  booksPerDay: Int,
  books: Seq[LibraryBook]
)

case class LibraryBook(id: Int, score: Int)

object ProblemData {
  def readFromFile(path: String): ProblemData = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines()
    val line1 = lines.next().split(" ").map(Integer.parseInt)
    val bookValues = lines.next().split(" ").map(Integer.parseInt)

    val libraries = for (libraryId <- 0 until line1(1)) yield {
      val libraryLine1 = lines.next().split(" ").map(Integer.parseInt)
      val libraryLine2 = lines.next().split(" ").map(Integer.parseInt)

      Library(
        id = libraryId,
        bookCount = libraryLine1(0),
        singUpTime = libraryLine1(1),
        booksPerDay = libraryLine1(2),
        books = libraryLine2.map(bookId => LibraryBook(bookId, bookValues(bookId)))
      )
    }

    bufferedSource.close

    ProblemData(
      bookCount = line1(0),
      libraryCount = line1(1),
      days = line1(2),
      bookValues = bookValues,
      libraries = libraries
    )
  }
}
