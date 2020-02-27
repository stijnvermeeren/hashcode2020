package challenge

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

class Solver(problem: ProblemData, config: Config) {
  val libraries = problem.libraries

  /**
   * Greedy search: whenever we can sign up another library, pick the one with the highest "score" (see scoreLibarary)
   * considering the books that will already be scanned anyway by previously selected libraries.
   */
  @tailrec
  final def solveRec(day: Int, solution: Solution): Solution = {
    if (day > problem.days) {
      solution
    } else {
      val selectedLibraryIds: Set[Int] = solution.selected.map(_.library.id).toSet

      val candidateLibraries = libraries.par
        .filterNot(library => selectedLibraryIds.contains(library.id))

      if (candidateLibraries.nonEmpty) {
        val alreadyScannedBooks = solution.scannedBookIds

        val bestLibrary = candidateLibraries
          .maxBy(scoreLibrary(alreadyScannedBooks, day))

        val selectedBooks = bestLibrary
          .scoredAndSortedBooks
          .filterNot(scannedBook => alreadyScannedBooks.contains(scannedBook.bookId))

        val newSelection = LibrarySelection(
          bestLibrary,
          selectedBooks,
          startScanningDay = day + bestLibrary.signUpTime
        )

        val newSolveState = addLibrary(solution, newSelection)

        solveRec(day + bestLibrary.signUpTime, newSolveState)
      } else {
        solution
      }
    }
  }

  /**
   * Score potential libraries by the maximum value that we can get from them, divided by the number of days it takes
   * to sign up this library, i.e. maximize by value per day of sign-up time.
   *
   * For some datasets, we obtained slightly better results by raising the sign up time to some power (less than 1),
   * which has to be fine-tuned by hand.
   */
  def scoreLibrary(
    alreadyScannedBooks: Set[Int],
    day: Int
  )(library: Library): Double = {
    maxValue(library, alreadyScannedBooks, day).toDouble / math.pow(library.signUpTime, config.exponent)
  }

  /**
   * Given a set of bookIds that will already be scanned anyway, and the day at which we can start signing up this
   * library, what is the maximum value that we can get from this library (i.e. by signing it up immediately and
   * scanning the books that are not already scanned from other libraries by descending value)
   */
  def maxValue(
    library: Library,
    alreadyScannedBooks: Set[Int],
    day: Int
  ): Int = {
    val scanningDays = problem.days - library.signUpTime - day
    val booksToBeScanned = scanningDays * library.booksPerDay

    val scannedBooks = library
      .scoredAndSortedBooks
      .filterNot(scannedBook => alreadyScannedBooks.contains(scannedBook.bookId))
      .take(booksToBeScanned)

    scannedBooks
      .map(_.score)
      .sum
  }


  def addLibrary(solution: Solution, newSelection: LibrarySelection): Solution = {
    val booksPerDay = newSelection.library.booksPerDay
    val daysScanning = problem.days - newSelection.startScanningDay

    val newScannedBooks = newSelection.scannedBooks
      .filterNot(scanned => solution.scannedBookIds.contains(scanned.bookId)) // ensure again that we don't double-count any books
      .take(booksPerDay * daysScanning)

    if (newScannedBooks.nonEmpty) {
      val newValue = newScannedBooks
        .map(_.score)
        .sum

      Solution(
        solution.selected :+ newSelection,
        scannedBookIds = solution.scannedBookIds ++ newScannedBooks.map(_.bookId),
        score = solution.score + newValue
      )
    } else {
      // don't add libraries that don't get to scan any books to the solution
      solution
    }
  }
}
