package challenge

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

class Solver(problem: ProblemData) {
  val libraries = problem.libraries
  val scoring = new Scoring(problem)

  @tailrec
  final def solveRec(day: Int, selected: Seq[LibrarySelection]): Seq[LibrarySelection] = {
    if (day > problem.days) {
      selected
    } else {
      val selectedLibraryIds: Set[Int] = selected.map(_.library.id).toSet

      val candidateLibraries = libraries.par
        .filterNot(library => selectedLibraryIds.contains(library.id))

      if (candidateLibraries.nonEmpty) {
        val bestLibrary = candidateLibraries
          .maxBy(library => scoring.maxScorePerLibrary(library, day))

        val newSelected = selected :+ LibrarySelection(
          bestLibrary,
          bestLibrary.scoredAndSortedBooks,
          startScanningDay = day + bestLibrary.singUpTime
        )
        solveRec(day + bestLibrary.singUpTime, newSelected)
      } else {
        selected
      }
    }
  }


  def simulate(librarySelections: Seq[LibrarySelection]): Int = {
    val finalState = (0 until problem.days).foldLeft(SimulationState(scannedBookIds = Set.empty, score = 0)) {
      case (previousState, dayId) =>
        val scannedBooks: Set[ScannedBook] = librarySelections.flatMap{ selection =>
          val daysScanning = dayId - selection.startScanningDay
          if (daysScanning >= 0) {
            val booksPerDay = selection.library.booksPerDay
            selection.scannedBooks.drop(booksPerDay * daysScanning).take(booksPerDay)
          } else {
            Set.empty
          }
        }.toSet

        val newValue = scannedBooks
          .filterNot(scanned => previousState.scannedBookIds.contains(scanned.bookId))
          .toSeq
          .map(_.score)
          .sum

        SimulationState(
          scannedBookIds = previousState.scannedBookIds ++ scannedBooks.map(_.bookId),
          score = previousState.score + newValue
        )
    }

    finalState.score
  }

  case class SimulationState(scannedBookIds: Set[Int], score: Int)
}
