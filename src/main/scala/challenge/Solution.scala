package challenge

import java.io.{File, PrintWriter}

case class Solution(librarySelections: Seq[LibrarySelection]) {
  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${librarySelections.size}\n")
    for (LibrarySelection(library, books, _) <- librarySelections) {
      pw.write(s"${library.id} ${books.size}\n")
      pw.write(s"${books.map(_.bookId).mkString(" ")}\n")
    }
    pw.close()
  }

  def score(): Int = {
    librarySelections.flatMap(_.scannedBooks.map(_.score)).sum
  }
}

/**
 * A triple of
 * - a Library object
 * - the order for the books to be scanned from this library and their scores
 * - at what day this library will be ready to start scanning
 */
case class LibrarySelection(library: Library, scannedBooks: Seq[ScannedBook], startScanningDay: Int)

case class ScannedBook(bookId: Int, score: Int)
