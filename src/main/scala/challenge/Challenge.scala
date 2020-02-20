package challenge

import scala.annotation.tailrec

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

    val librarySelections = solver.solveRec(day = 0, selected = Seq.empty)

    val solution = Solution(librarySelections)

    solution.writeToFile(s"output/$dataset.txt")
    val simulation = new Simulation(problem, solution)
    println(s"Estimated score for $dataset: ${simulation.run()}")
  }

}
