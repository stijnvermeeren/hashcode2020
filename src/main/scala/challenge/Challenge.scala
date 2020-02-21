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
    val usedBooks = scala.collection.mutable.Set.empty[Int]
    val libraries = problem.libraries.sortBy(library => Scoring.maxScorePerLibrary(problem.days, library, Set.empty).score / library.singUpTime)
      .reverse.filter(library => {
      val hasNewBooks = library.books.exists(book => !usedBooks.contains(book.id))
      usedBooks.addAll(library.books.map(_.id))
      hasNewBooks
    })
    val solution = Solution(calculateSelection(problem.days, problem.libraries, problem.bookCount).filter(_.books.nonEmpty))

    solution.writeToFile(s"output/$dataset.txt")
    println(s"Estimated score for $dataset: ${solution.score}")
  }


  @tailrec
  def calculateSelection(days: Int, libraries: Seq[Library], allBooks: Int, res: Seq[MaxScoreLibrary] = Seq.empty, usedBooks: Set[Int] = Set.empty): Seq[MaxScoreLibrary] = {
    val validLibs = libraries.filter(_.singUpTime < days)
    if (validLibs.isEmpty || usedBooks.size == allBooks) {
      res
    } else {
      val topLibraries = validLibs.par.map(Scoring.maxScorePerLibrary(days, _, usedBooks)).seq.sortBy(library => library.score / library.library.singUpTime).reverse
      val bestLibrary = topLibraries.head
      val bestSelectionForSignUpPeriod = cleanValue(allBooks, topLibraries, bestLibrary, usedBooks)

      val bestSelection = if (bestSelectionForSignUpPeriod.total > bestLibrary.score)
        bestSelectionForSignUpPeriod
      else Seq(bestLibrary)
      val bestSelectionIds = bestSelection.map(_.id)
      calculateSelection(days - bestLibrary.library.singUpTime, validLibs.filterNot(lib => bestSelectionIds.contains(lib.id)), allBooks, res ++ bestSelection, usedBooks ++ bestSelection.flatMap(_.books).toSet)
    }
  }

  private def cleanValue(allBooks: Int, topLibraries: Seq[MaxScoreLibrary], bestLibrary: MaxScoreLibrary, usedBooks: Set[Int]) = {
    calculateSelection(bestLibrary.library.singUpTime, topLibraries.drop(1).filter(_.library.singUpTime < bestLibrary.library.singUpTime).take(bestLibrary.library.singUpTime).map(_.library), allBooks, usedBooks = usedBooks)
  }

  implicit class SelectionImpro(selection: Seq[MaxScoreLibrary]) {
    def total: Int = selection.map(_.score).sum
  }

}
