package challenge

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

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

    val solution = Solution(calculateSelection(problem.days, problem.libraries, problem.bookCount).filter(_.books.nonEmpty))

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Estimated score for $dataset: ${solution.score}")
  }


  @tailrec
  def calculateSelection(days: Int, libs: Seq[Library], allBooks: Int, res: Seq[MaxScoreLibrary] = Seq.empty, usedBooks: Set[Int] = Set.empty): Seq[MaxScoreLibrary] = {
    if (libs.isEmpty || usedBooks.size == allBooks) {
      res
    } else {
      val topLibraries = libs.par.map(Scoring.maxScorePerLibrary(days, _, usedBooks)).seq.sortBy(library => library.score).reverse
      val bestLibrary = topLibraries.head
      val bestLibraryInitial = libs.par.find(_.id == bestLibrary.id).get
      calculateSelection(days - bestLibraryInitial.singUpTime, libs.par.filterNot(_.id == bestLibrary.id).seq, allBooks, res :+ bestLibrary, usedBooks ++ bestLibrary.books.toSet)
    }
  }
}
