package practice

import scala.io.Source

case class ProblemData(
  maxSlices: Int,
  pizzaCount: Int,
  pizzaSizes: Seq[Int]
) {
  assert(pizzaCount == pizzaSizes.size)
  assert(pizzaSizes.max <= maxSlices)
}

object ProblemData {
  def readFromFile(path: String): ProblemData = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines()
    val line1 = lines.next().split(" ").map(Integer.parseInt)
    val line2 = lines.next().split(" ").map(Integer.parseInt)

    bufferedSource.close

    ProblemData(
      maxSlices = line1(0),
      pizzaCount = line1(1),
      pizzaSizes = line2
    )
  }
}
