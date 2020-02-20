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

    val solution = Solution(calculateSelection(problem.days, problem.libraries))

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Estimated score for $dataset: ${solution.score}")
  }


  @tailrec
  def calculateSelection(days: Int, libs: Seq[Library], res: Seq[MaxScoreLibrary] = Seq.empty, usedBooks: Set[Int] = Set.empty): Seq[MaxScoreLibrary] = {
    if (libs.isEmpty) {
      res
    } else {
      val topLibraries = libs.map(Scoring.maxScorePerLibrary(days, _, usedBooks)).sortBy(library => library.score).reverse
      val bestLibrary = topLibraries.head
      val bestLibraryInitial = libs.find(_.id == bestLibrary.id).get
      calculateSelection(days - bestLibraryInitial.singUpTime, libs.filterNot(_.id == bestLibrary.id), res :+ bestLibrary, usedBooks ++ bestLibrary.books.toSet)
    }
  }
}
