package practice
import java.io._

case class Solution(sliceIndices: Seq[Int]) {
  def writeToFile(path: String): Unit = {
    val pw = new PrintWriter(new File(path))
    pw.write(s"${sliceIndices.size}\n")
    pw.write(s"${sliceIndices.mkString(" ")}")
    pw.close()
  }

  def score(problem: ProblemData): Int = sliceIndices.map(problem.pizzaSizes).sum

  def isValidForProblem(problem: ProblemData): Boolean = {
    score(problem) <= problem.maxSlices
  }
}
