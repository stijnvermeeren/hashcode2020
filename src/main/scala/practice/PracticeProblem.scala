package practice

object PracticeProblem extends App {
  val datasets = Seq("a_example", "b_small", "c_medium", "d_quite_big", "e_also_big")

  for {
    dataset <- datasets
  } {
    println(dataset)
    val problem = ProblemData.readFromFile(s"practice_problem/$dataset.in")
    val solver = new Solver(problem)
    val solution = solver.solveGreedy()
    assert(solution.isValidForProblem(problem))
    println(s"Score for $dataset: ${solution.score(problem)}")
    solution.writeToFile(s"practice_problem/$dataset.out")
  }
}
