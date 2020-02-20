package challenge

import java.io.{File, PrintWriter}

case class Solution(librarySelections: Seq[LibrarySelection]) {

  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${librarySelections.size}\n")
    for (LibrarySelection(id, books, _, _) <- librarySelections) {
      pw.write(s"$id ${books.size}\n")
      pw.write(s"${books.map(_.bookId).mkString(" ")}\n")
    }
    pw.close()
  }

  def score(): Int = {
    librarySelections.flatMap(_.scannedBooks.map(_.score)).sum
  }
}

case class LibrarySelection(libraryId: Int, scannedBooks: Seq[ScannedBook], startScanningDay: Int, booksPerDay: Int)

case class ScannedBook(bookId: Int, score: Int)
