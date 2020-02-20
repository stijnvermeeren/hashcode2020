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
    dataset <- datasets.headOption
  } {
    val problem = ProblemData.readFromFile(s"challenge/$dataset.txt")
    val scoring = new Scoring(problem)

    val libraries = problem.libraries

    val bestLibraries = libraries.sortBy(library => -scoring.maxScorePerLibrary(library))

    bestLibraries.take(3) foreach { lib =>
      println(lib)
      println(lib)
      println(scoring.maxScorePerLibrary(lib))
    }
  }
}
