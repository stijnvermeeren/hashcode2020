package challenge

import java.io.File

object Main extends App {
  val datasets: Seq[(String, Config)] = Seq(
    "a_example" -> Config.default,
    "b_read_on" -> Config.default,
    "c_incunabula" -> Config.default,
    "d_tough_choices" -> Config.default,
    "e_so_many_books" -> Config(exponent = 0.9),
    "f_libraries_of_the_world" -> Config(exponent = 0.6)
  )

  val directory = new File("output")
  if (!directory.exists) {
    directory.mkdir()
  }

  for {
    (dataset, config) <- datasets
  } {
    val problem = ProblemData.readFromFile(s"challenge/$dataset.txt")
    val solver = new Solver(problem, config)

    val solution = solver.solveRec(day = 0, Solution.empty)

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Calculated score for $dataset: ${solution.score}")
  }

}
