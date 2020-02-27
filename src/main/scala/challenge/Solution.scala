package challenge

import java.io.{File, PrintWriter}


/**
 * Keeps track of the state of the greedy search.
 * The value `selected` contains the partial solutions (which library to sign-up and which books to scan from them),
 *   `scannedBookIds` contains the ids of all the scanned books and `score` contains their total value.
 */
case class Solution(
  selected: Seq[LibrarySelection],
  scannedBookIds: Set[Int],
  score: Int
) {
  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${selected.size}\n")
    for (LibrarySelection(library, books, _) <- selected) {
      pw.write(s"${library.id} ${books.size}\n")
      pw.write(s"${books.map(_.bookId).mkString(" ")}\n")
    }
    pw.close()
  }
}

object Solution {
  val empty = Solution(
    selected = Seq.empty,
    scannedBookIds = Set.empty,
    score = 0
  )
}

/**
 * A triple of
 * - a Library object
 * - the order for the books to be scanned from this library and their scores
 * - at what day this library will be ready to start scanning
 */
case class LibrarySelection(library: Library, scannedBooks: Seq[ScannedBook], startScanningDay: Int)

case class ScannedBook(bookId: Int, score: Int)
