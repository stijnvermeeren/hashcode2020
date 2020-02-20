package challenge

class Simulation(problemData: ProblemData, solution: Solution) {
  def run(): Int = {
    val finalState = (0 until problemData.days).foldLeft(SimulationState(scannedBookIds = Set.empty, score = 0)) {
      case (previousState, dayId) =>
        val scannedBooks: Set[ScannedBook] = solution.librarySelections.flatMap{ selection =>
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
}

case class SimulationState(scannedBookIds: Set[Int], score: Int)
