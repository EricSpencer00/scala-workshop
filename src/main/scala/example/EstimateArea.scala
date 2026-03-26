//> using scala 3.3.7
//> using dep "com.lihaoyi::mainargs:0.7.8"
//> using dep "com.github.haifengl::smile-scala:5.2.2"
//> using dep "org.slf4j:slf4j-simple:2.0.17"

import mainargs.{main, arg, ParserForMethods}
import java.nio.file.{Path, Files}
import java.nio.charset.StandardCharsets
import smile.data.DataFrame
import smile.io.Read
import org.apache.commons.csv.CSVFormat

object EstimateArea:

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args.toIndexedSeq)

  @main(doc = "Monte Carlo area estimator") 
  def run(
    @arg(short = 'i', doc = "Input CSV file of darts (default: darts.csv)") input: String = "darts.csv",
    @arg(short = 'o', doc = "Optional output file to write the estimated area") output: Option[String] = None
  ): Unit =

    if !Files.exists(Path.of(input)) then
      println(s"Input file '${input}' does not exist.")
      sys.exit(1)

    val df: DataFrame = Read.csv(input,
      CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()
    )

    val totalCount = df.nrow()

    if totalCount == 0 then
      println(s"Input file '${input}' is empty.")
      sys.exit(1)

    val insideCount = df.stream().filter(row => row.getBoolean("inside")).count()
    val estimatedArea = 4.0 * insideCount / totalCount
    val resultText = f"Estimated area of unit circle: $estimatedArea%.5f using $insideCount/$totalCount darts"

    println(resultText)

    output.foreach: outputPath =>
      Files.writeString(Path.of(outputPath), resultText + "\n", StandardCharsets.UTF_8)
      println(s"Result also written to ${outputPath}")