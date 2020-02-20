package challenge

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

class Solver(problem: ProblemData) {
  val libraries = problem.libraries
  val scoring = new Scoring(problem)

  @tailrec
  final def solveRec(day: Int, solveState: SolveState): SolveState = {
    if (day > problem.days) {
      solveState
    } else {
      val selectedLibraryIds: Set[Int] = solveState.selected.map(_.library.id).toSet

      val candidateLibraries = libraries.par
        .filterNot(library => selectedLibraryIds.contains(library.id))

      if (candidateLibraries.nonEmpty) {
        val alreadyScannedBooks = simulate(solveState.selected).scannedBookIds

        val bestLibrary = candidateLibraries
          .maxBy(library => scoring.maxScorePerLibrary(library, alreadyScannedBooks, day))

        // println(s"Selected library ${bestLibrary.id}")

        val selectedBooks = bestLibrary
          .scoredAndSortedBooks
          .filterNot(scannedBook => alreadyScannedBooks.contains(scannedBook.bookId))

        val newSelection = LibrarySelection(
          bestLibrary,
          selectedBooks,
          startScanningDay = day + bestLibrary.singUpTime
        )

        val newSolveState = SolveState(
          solveState.selected :+ newSelection,
          simulateStep(solveState.simulationState, newSelection)
        )

        solveRec(day + bestLibrary.singUpTime, newSolveState)
      } else {
        solveState
      }
    }
  }


  def simulateStep(state: SimulationState, newSelection: LibrarySelection): SimulationState = {
    (0 until problem.days).foldLeft(state) {
      case (previousState, dayId) =>
        val daysScanning = dayId - newSelection.startScanningDay
        val newScannedBooks = if (daysScanning >= 0) {
          val booksPerDay = newSelection.library.booksPerDay
          newSelection.scannedBooks.drop(booksPerDay * daysScanning).take(booksPerDay)
        } else {
          Set.empty
        }

        val newValue = newScannedBooks
          .filterNot(scanned => previousState.scannedBookIds.contains(scanned.bookId))
          .toSeq
          .map(_.score)
          .sum

        SimulationState(
          scannedBookIds = previousState.scannedBookIds ++ newScannedBooks.map(_.bookId),
          score = previousState.score + newValue
        )
    }
  }

  def simulate(librarySelections: Seq[LibrarySelection]): SimulationState = {
    (0 until problem.days).foldLeft(SimulationState(scannedBookIds = Set.empty, score = 0)) {
      case (previousState, dayId) =>
        val scannedBooks = librarySelections.par.flatMap{ selection =>
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
  }
}

case class SimulationState(scannedBookIds: Set[Int], score: Int)

object SimulationState {
  val empty = SimulationState(Set.empty, 0)
}

case class SolveState(selected: Seq[LibrarySelection], simulationState: SimulationState)

object SolveState {
  val empty = SolveState(Seq.empty, SimulationState.empty)
}
