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
}
