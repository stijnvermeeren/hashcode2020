package practice

class Solver(problemData: ProblemData) {

  def solveGreedy(): Solution = {
    val partialSums: Seq[Int] = problemData.pizzaSizes.foldLeft(Seq.empty[Int]) {
      case (sums, nextSize) =>
        val previousSum = sums.lastOption.getOrElse(0)
        sums :+ (previousSum + nextSize)
    }

    val selectedSlices = partialSums.zipWithIndex takeWhile {
      case (size, _) => size <= problemData.maxSlices
    } map {
      case (_, index) => index
    }

    Solution(selectedSlices)
  }
}
