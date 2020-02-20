package challenge

object ChallengeUnique extends App {
  val datasets = Seq(
    "a_example",
    "c_incunabula",
    "d_tough_choices",
    "b_read_on",
    "e_so_many_books",
    "f_libraries_of_the_world"
  )

  for {
    dataset <- datasets
  } {
    val problem = ProblemData.readFromFile(s"challenge/$dataset.txt")
    val solver = new Solver(problem)

    val libraries = solver.byUnique()
    val solveState = solver.solveRecUnique(0, SolveState.empty, libraries)

    val solution = Solution(solveState.selected)

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Estimated score for $dataset: ${solveState.simulationState.score}")
  }

}
