package challenge

import scala.annotation.tailrec

object Challenge extends App {
  val datasets = Seq(
    "f_libraries_of_the_world",
    "a_example",
    "b_read_on",
    "e_so_many_books",
    "c_incunabula",
    "d_tough_choices"
  )

  for {
    dataset <- datasets
  } {
    val problem = ProblemData.readFromFile(s"challenge/$dataset.txt")
    val solver = new Solver(problem)

    val solveState = solver.solveRec(day = 0, SolveState.empty)

    val solution = Solution(solveState.selected)

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Estimated score for $dataset: ${solveState.simulationState.score}")
  }

}
