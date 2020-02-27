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
    val solver = new Solver(problem)

    val solveState = solver.solveRec(day = 0, SolveState.empty)

    val solution = Solution(solveState.selected)

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Calculated score for $dataset: ${solveState.score}")
  }

}
