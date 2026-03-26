import scala.collection.mutable.PriorityQueue

object StreamingMaxHeapApp {

  val ESC          = "\u001b["
  val RESET        = s"${ESC}0m"
  val BOLD         = s"${ESC}1m"
  val DIM          = s"${ESC}2m"
  val HIDE_CURSOR  = s"${ESC}?25l"
  val SHOW_CURSOR  = s"${ESC}?25h"
  val ERASE_DOWN   = s"${ESC}J"

  val GOLD   = s"${ESC}38;5;220m"
  val SILVER = s"${ESC}38;5;250m"
  val BRONZE = s"${ESC}38;5;172m"
  val TEAL   = s"${ESC}38;5;51m"
  val PURPLE = s"${ESC}38;5;135m"
  val PINK   = s"${ESC}38;5;213m"
  val BG_DARK   = s"${ESC}48;5;234m"
  val BG_DARKER = s"${ESC}48;5;232m"

  val maxHeap = PriorityQueue[Int]()

  // Row on screen where the heap box is pinned
  var heapBoxRow = 5

  def moveTo(row: Int, col: Int = 1): Unit = print(s"${ESC}${row};${col}H")
  def saveCursor(): Unit   = print(s"${ESC}s")
  def restoreCursor(): Unit = print(s"${ESC}u")

  def colorForRank(i: Int): String = i match {
    case 0 => GOLD
    case 1 => SILVER
    case 2 => BRONZE
    case 3 => TEAL
    case 4 => PURPLE
    case _ => PINK
  }

  def medalForRank(i: Int): String = i match {
    case 0 => s"${GOLD}👑"
    case 1 => s"${SILVER}🥈"
    case 2 => s"${BRONZE}🥉"
    case _ => s"$DIM  "
  }

  def animateBubble(nums: Seq[Int]): Unit = {
    val label = nums.mkString(", ")
    val frames = List(
      s"${TEAL}  ∘ $label ∘$RESET",
      s"${PURPLE} ○  $label  ○$RESET",
      s"${PINK}◎   $label   ◎$RESET",
      s"${TEAL}◉   $label   ◉$RESET",
      s"${DIM} ·  $label  ·$RESET",
      s"${DIM}    $label   $RESET",
      s"              "
    )
    print(HIDE_CURSOR)
    frames.foreach { f =>
      saveCursor()
      moveTo(heapBoxRow - 1)
      print(s"${ESC}2K$f")
      restoreCursor()
      Console.flush()
      Thread.sleep(65)
    }
    // clear bubble row
    saveCursor()
    moveTo(heapBoxRow - 1)
    print(s"${ESC}2K")
    restoreCursor()
    print(SHOW_CURSOR)
  }

  def drawHeap(snapshot: IndexedSeq[Int], cascade: Boolean): Unit = {
    val width = 34
    val lines = scala.collection.mutable.ArrayBuffer[String]()

    val title    = " MAX HEAP "
    val titlePad = (width - title.length) / 2
    lines += s"$BG_DARKER$BOLD$GOLD${"═" * width}$RESET"
    lines += s"$BG_DARKER$BOLD$GOLD${" " * titlePad}$title${" " * (width - titlePad - title.length)}$RESET"
    lines += s"$BG_DARKER$BOLD$GOLD${"═" * width}$RESET"

    if (snapshot.isEmpty) {
      lines += s"$BG_DARK$DIM${" " * 12}( empty )${" " * 13}$RESET"
    } else {
      snapshot.zipWithIndex.foreach { case (v, i) =>
        val medal   = medalForRank(i)
        val color   = colorForRank(i)
        val numStr  = s"$BOLD$color${v.toString.reverse.padTo(8, ' ').reverse}"
        val sparkle = if (i == 0) s"  $GOLD✦ ✦ ✦" else s"$DIM   ···"
        lines += s"$BG_DARK $medal $numStr$sparkle$RESET"
      }
    }

    lines += s"$BG_DARKER$DIM$TEAL${"─" * width}$RESET"
    lines += s"$BG_DARKER$DIM$TEAL  size: ${snapshot.size}${" " * (width - 8 - snapshot.size.toString.length)}$RESET"

    // stamp each line at its exact screen row, overwriting whatever was there
    lines.zipWithIndex.foreach { case (line, i) =>
      moveTo(heapBoxRow + i)
      print(s"${ESC}2K")  // erase full line first
      print(line)
      if (cascade) {
        Console.flush()
        Thread.sleep(38)
      }
    }

    // erase any extra lines below (heap shrink case — shouldn't happen with a max heap but defensive)
    moveTo(heapBoxRow + lines.size)
    print(ERASE_DOWN)
    Console.flush()
  }

  def main(args: Array[String]): Unit = {
    print(HIDE_CURSOR)
    // Reserve top lines for header + bubble row + heap box
    println(s"\n$BOLD${TEAL}Enter numbers (comma/space separated). Type 'exit' to quit.$RESET")
    println()  // blank line = bubble animation row (heapBoxRow - 1)
    println()  // heap starts at heapBoxRow

    // Figure out what row we're on after the header prints
    // Header = line 1, blank = line 2, blank = line 3 -> heap starts row 4
    heapBoxRow = 4

    drawHeap(IndexedSeq.empty, cascade = false)

    // Place input prompt below the box (header=1, blank=2, blank=3, box=~10 lines -> row 14+)
    val promptRow = heapBoxRow + 12

    val reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in))

    var running = true
    print(SHOW_CURSOR)

    while (running) {
      moveTo(promptRow)
      print(s"${ESC}2K${TEAL}> $RESET")
      Console.flush()

      val line = reader.readLine()
      if (line == null || line.trim.toLowerCase == "exit") {
        running = false
      } else {
        val nums = line.split("[,\\s]+").flatMap(_.trim.toIntOption).toSeq
        if (nums.nonEmpty) {
          print(HIDE_CURSOR)
          animateBubble(nums)
          maxHeap ++= nums
          drawHeap(maxHeap.clone().dequeueAll.toIndexedSeq, cascade = true)
          print(SHOW_CURSOR)
        }
      }
    }

    moveTo(promptRow + 1)
    println(s"\n$BOLD${GOLD}Final sorted output:$RESET")
    maxHeap.clone().dequeueAll.foreach { v =>
      println(s"$TEAL  $v$RESET")
      Thread.sleep(40)
    }
  }
}