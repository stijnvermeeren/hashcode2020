package challenge

class Scoring(problem: ProblemData) {
  def maxScorePerLibrary(
    library: Library,
    alreadyScannedBooks: Set[Int] = Set.empty,
    day: Int = 0
  ): Int = {
    val scanningDays = problem.days - library.singUpTime - day
    val booksToBeScanned = scanningDays * library.booksPerDay

    val scannedBooks = library
      .scoredAndSortedBooks
      .filterNot(scannedBook => alreadyScannedBooks.contains(scannedBook.bookId))
      .take(booksToBeScanned)

    val score = scannedBooks
      .map(_.score)
      .sum

    val avgScore = if (scannedBooks.nonEmpty) {
      score / scannedBooks.size
    } else {
      0
    }

    if (scanningDays > library.singUpTime / 10) {
      score - (booksToBeScanned - scannedBooks.size) * avgScore / 10
    } else {
      score
    }
  }
}
