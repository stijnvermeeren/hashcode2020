package challenge

object Challenge extends App {
  val datasets = Seq(
    "a_example",
    "b_read_on",
    "c_incunabula",
    "d_tough_choices",
    "e_so_many_books",
    "f_libraries_of_the_world"
  )

  for {
    dataset <- datasets
  } {
    val problem = ProblemData.readFromFile(s"challenge/$dataset.txt")
    val scoring = new Scoring(problem)

    val libraries = problem.libraries

    val bestLibraries = libraries.sortBy(library => -scoring.maxScorePerLibrary(library))

    val librarySelections = bestLibraries.take(problem.days).foldLeft(Seq.empty[LibrarySelection]) {
      case (previousSelection, nextLibrary) =>
        val scannedBooks = scoring.mostValuableBookIds(nextLibrary) map { bookId =>
          ScannedBook(bookId, scoring.bookValue(bookId))
        }

        val startScanningDay = previousSelection.lastOption match {
          case Some(selection) => selection.startScanningDay + nextLibrary.singUpTime
          case None => nextLibrary.singUpTime
        }

        previousSelection :+ LibrarySelection(
          nextLibrary,
          scannedBooks,
          startScanningDay = startScanningDay
        )
    }

    val solution = Solution(librarySelections)

    solution.writeToFile(s"output/$dataset.txt")
    val simulation = new Simulation(problem, solution)
    println(s"Estimated score for $dataset: ${simulation.run()}")
  }
}
