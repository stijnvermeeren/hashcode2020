package challenge

class Scoring(problem: ProblemData) {
  def mostValuableBookIds(library: Library): Seq[Int] = {
    library.books.sortBy(bookId => -bookValue(bookId))
  }

  def maxScorePerLibrary(library: Library, day: Int = 0): Int = {
    val scanningDays = problem.days - library.singUpTime - day
    val booksToBeScanned = scanningDays * library.booksPerDay

    mostValuableBookIds(library).take(booksToBeScanned).map(bookValue).sum
  }

  def bookValue(bookId: Int): Int = {
    problem.bookValues(bookId)
  }
}
