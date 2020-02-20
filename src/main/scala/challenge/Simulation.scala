package challenge

class Simulation(problemData: ProblemData, solution: Solution) {
  def run(): Int = {
    (0 until problemData.days).foldLeft(SimulationState(scannedBookIds = Set.empty, score = 0)) {
      case (previousState, dayId) =>
        val bookIds: Set[Int] = solution.librarySelections.map{ selection =>
          if (dayId >= selection.startScanningDay) {
            selection.drop(selection.booksPerDay)
          } else {
            Set.empty
          }
        }
    }
  }
}

case class SimulationState(scannedBookIds: Set[Int], score: Int)
