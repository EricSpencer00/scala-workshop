import scala.collection.mutable.PriorityQueue
import scala.io.StdIn

object StreamingMaxHeapApp {

  def main(args: Array[String]): Unit = {
    println("Enter numbers (type 'exit' to finish):")

    val maxHeap = PriorityQueue[Int]()

    Iterator
      .continually(StdIn.readLine())
      .takeWhile(_ != null)
      .takeWhile(_.trim.toLowerCase != "exit")
      .foreach { line =>
        val nums = line
          .split("\\s+")
          .flatMap(_.toIntOption)

        nums.foreach { n =>
          maxHeap += n
          // \r overwrites the current line in-place for live feedback
          print(s"\r[Heap size: ${maxHeap.size}] Last inserted: $n    ")
          Console.flush()
        }
      }

    println("\n\nSorted (descending via max heap):")

    while (maxHeap.nonEmpty) {
      val value = maxHeap.dequeue()
      println(value)
      // Small delay so output visibly streams rather than printing all at once
      Thread.sleep(80)
    }
  }
}