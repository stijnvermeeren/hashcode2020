package challenge

import java.io.{File, PrintWriter}

case class Solution(librarySelections: Seq[LibrarySelection]) {
  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${librarySelections.size}\n")
    for (LibrarySelection(bookIds) <- librarySelections) {
      pw.write(s"${bookIds.size}\n")
      pw.write(s"${bookIds.mkString(" ")}\n")
    }
    pw.close()
  }

  def score(problem: ProblemData): Int = ???
}

case class LibrarySelection(bookIds: Seq[Int])
