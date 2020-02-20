package challenge

import java.io.{File, PrintWriter}

case class Solution(librarySelections: Seq[MaxScoreLibrary]) {

  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${librarySelections.size}\n")
    librarySelections foreach {
      case MaxScoreLibrary(id, _, books) =>
        pw.write(s"$id ${books.size}\n")
        pw.write(s"${books.mkString(" ")}\n")
    }
    pw.close()
  }

  val score: Int = librarySelections.map(_.score).sum
}

