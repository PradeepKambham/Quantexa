
import scala.io.Source
import scala.util.{Failure, Success, Try}
import java.io.PrintWriter

/*

  The readCsv code here was adapted from the supplied code on the assignment sheet

*/

object CsvHelper {

  def readCsv(path: String): List[Transaction] = {
    Source.fromFile(path).getLines().drop(1)
    .flatMap { line =>
        val split = line.split(',')
        Try(Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)) match {
          case Success(x) => Some(x)
          case Failure(x) => None
        }
      }.toList
  }

  def writeCsv(path: String, content: String): Unit = {
    new PrintWriter(path){
      try {
        write(content)
      } finally close()
    }
  }
}
