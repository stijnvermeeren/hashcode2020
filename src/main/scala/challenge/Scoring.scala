package challenge

object Scoring {
  private def mostValuableBookIds(library: Library): Seq[LibraryBook] = {
    library.books.sortBy(_.score).reverse
  }

  def maxScorePerLibrary(daysLeft: Int, library: Library, usedBooks: Set[Int]): MaxScoreLibrary = {
    val scanningDays = daysLeft - library.singUpTime
    if (scanningDays <= 0) {
      MaxScoreLibrary(library.id, 0, Seq.empty)
    } else {
      val booksToBeScanned = scanningDays * library.booksPerDay
      val booksToScan = mostValuableBookIds(library).filterNot(book => usedBooks.contains(book.id)).take(booksToBeScanned)
      MaxScoreLibrary(library.id, booksToScan.map(_.score).sum, booksToScan.map(_.id))
    }
  }
}

case class MaxScoreLibrary(id: Int, score: Int, books: Seq[Int])
