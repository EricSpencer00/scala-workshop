import scala.collection.mutable.PriorityQueue

object StreamingMaxHeapApp {

  // ANSI escape codes
  val CLEAR_LINE  = "\u001b[2K"
  val MOVE_UP     = "\u001b[1A"
  val RESET       = "\u001b[0m"
  val BOLD        = "\u001b[1m"
  val CYAN        = "\u001b[36m"
  val GREEN       = "\u001b[32m"
  val YELLOW      = "\u001b[33m"

  val maxHeap = PriorityQueue[Int]()
  var lastDrawnLines = 0

  def clearPreviousDraw(): Unit = {
    for (_ <- 0 until lastDrawnLines) {
      print(s"$MOVE_UP$CLEAR_LINE")
    }
  }

  def drawHeap(): Unit = {
    clearPreviousDraw()

    val snapshot = maxHeap.clone().dequeueAll // sorted descending
    val lines    = scala.collection.mutable.ArrayBuffer[String]()

    lines += s"$BOLD$CYAN╔══════════════════════════╗$RESET"
    lines += s"$BOLD$CYAN║   MAX HEAP  (size: ${"%3d".format(snapshot.size)})  ║$RESET"
    lines += s"$BOLD$CYAN╠══════════════════════════╣$RESET"

    if (snapshot.isEmpty) {
      lines += s"$CYAN║   $RESET(empty)$CYAN                  ║$RESET"
    } else {
      snapshot.zipWithIndex.foreach { case (v, i) =>
        val rank  = i + 1
        val color = if (i == 0) YELLOW else GREEN
        val bar   = "█" * math.min(v.abs, 15)
        val label = s"$rank. $v"
        val padded = label.padTo(8, ' ')
        lines += s"$CYAN║ $color$padded $bar$RESET"
      }
    }

    lines += s"$BOLD$CYAN╚══════════════════════════╝$RESET"

    lines.foreach(println)
    lastDrawnLines = lines.size
  }

  def main(args: Array[String]): Unit = {
    println(s"${BOLD}Enter numbers separated by commas, one line at a time. Type 'exit' to finish.$RESET\n")
    drawHeap()

    val reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in))

    var running = true
    while (running) {
      print(s"${CYAN}> $RESET")
      Console.flush()

      val line = reader.readLine()
      if (line == null || line.trim.toLowerCase == "exit") {
        running = false
      } else {
        val nums = line.split("[,\\s]+").flatMap(_.trim.toIntOption)
        maxHeap ++= nums
        drawHeap()
      }
    }

    println(s"\n${BOLD}Final sorted output:$RESET")
    val result = maxHeap.clone().dequeueAll
    result.foreach(println)
  }
}