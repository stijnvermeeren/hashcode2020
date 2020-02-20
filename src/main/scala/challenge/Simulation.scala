package challenge

class Simulation(problemData: ProblemData, solution: Solution) {
  def run(): Int = {
    ???
  }

  def nextState(previousState: SimulationState): SimulationState = {
    val nextLibraryReady = previousState.libraryStates.values.toSet.contains(SigningUp(0))

    if (nextLibraryReady) {

    }

    val libraryStates = solution.librarySelections map { librarySelection =>


      librarySelection.libraryId -> ???
    }

    ???
  }
}

case class SimulationState(scannedBookIds: Set[Int], score: Int, libraryStates: Map[Int, LibraryState])

sealed trait LibraryState
case object Waiting extends LibraryState
case class SigningUp(remainingDays: Int) extends LibraryState
case class Scanning(booksScanned: Int) extends LibraryState
