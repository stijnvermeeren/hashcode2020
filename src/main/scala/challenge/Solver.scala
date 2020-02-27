package challenge

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

class Solver(problem: ProblemData) {
  val libraries = problem.libraries
  val scoring = new Scoring(problem)

  /**
   * Greedy search: whenever we can sign up another library, pick the one with the highest "score" (see scoreLibarary)
   * considering the books that will already be scanned anyway by previously selected libraries.
   */
  @tailrec
  final def solveRec(day: Int, solveState: SolveState): SolveState = {
    if (day > problem.days) {
      solveState
    } else {
      val selectedLibraryIds: Set[Int] = solveState.selected.map(_.library.id).toSet

      val candidateLibraries = libraries.par
        .filterNot(library => selectedLibraryIds.contains(library.id))

      if (candidateLibraries.nonEmpty) {
        val alreadyScannedBooks = solveState.scannedBookIds

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

        val newSolveState = addLibrary(solveState, newSelection)

        solveRec(day + bestLibrary.signUpTime, newSolveState)
      } else {
        solveState
      }
    }
  }

  /**
   * Score potential libraries by the maximum value that we can get from them, divided by the number of days it takes
   * to sign up this library, i.e. maximize by value per day of sign-up time.
   */
  def scoreLibrary(
    alreadyScannedBooks: Set[Int],
    day: Int
  )(library: Library): Double = {
    maxValue(library, alreadyScannedBooks, day).toDouble / library.signUpTime
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


  def addLibrary(state: SolveState, newSelection: LibrarySelection): SolveState = {
    val booksPerDay = newSelection.library.booksPerDay
    val daysScanning = problem.days - newSelection.startScanningDay

    val newScannedBooks = newSelection.scannedBooks
      .filterNot(scanned => state.scannedBookIds.contains(scanned.bookId)) // ensure again that we don't double-count any books
      .take(booksPerDay * daysScanning)

    val newValue = newScannedBooks
      .map(_.score)
      .sum

    SolveState(
      state.selected :+ newSelection,
      scannedBookIds = state.scannedBookIds ++ newScannedBooks.map(_.bookId),
      score = state.score + newValue
    )
  }
}

/**
 * Keeps track of the state of the greedy search.
 * The value `selected` contains the partial solutions (which library to sign-up and which books to scan from them),
 *   `scannedBookIds` contains the ids of all the scanned books and `score` contains their total value.
 */
case class SolveState(
  selected: Seq[LibrarySelection],
  scannedBookIds: Set[Int],
  score: Int
)

object SolveState {
  val empty = SolveState(
    selected = Seq.empty,
    scannedBookIds = Set.empty,
    score = 0
  )
}
