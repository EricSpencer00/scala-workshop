import scala.collection.mutable.PriorityQueue
import scala.io.StdIn

object MaxHeapSortApp {

  def main(args: Array[String]): Unit = {
    println("Enter numbers separated by spaces:")

    val input = StdIn.readLine()

    // Safe parsing: filter out invalid values
    val numbers: Seq[Int] = input
      .split("\\s+")
      .flatMap(s => s.toIntOption)   // avoids exceptions

    if (numbers.isEmpty) {
      println("No valid numbers provided.")
      return
    }

    // Max heap (default PriorityQueue is max heap in Scala)
    val maxHeap = PriorityQueue[Int]()
    maxHeap ++= numbers

    println("Sorted (descending via max heap):")

    // Extract elements safely
    while (maxHeap.nonEmpty) {
      println(maxHeap.dequeue())
    }
  }
}