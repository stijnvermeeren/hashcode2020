package challenge

class Scoring(problem: ProblemData) {
  def maxScorePerLibrary(
    library: Library,
    alreadyScannedBooks: Set[Int] = Set.empty,
    day: Int = 0
  ): Int = {
    val scanningDays = problem.days - library.singUpTime - day
    val booksToBeScanned = scanningDays * library.booksPerDay

    library
      .scoredAndSortedBooks
      .filterNot(scannedBook => alreadyScannedBooks.contains(scannedBook.bookId))
      .take(booksToBeScanned)
      .map(_.score)
      .sum
  }
}
