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

val _ = null