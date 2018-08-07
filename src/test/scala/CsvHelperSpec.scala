import org.scalatest.{FlatSpec, Matchers}

class CsvHelperSpec extends FlatSpec with Matchers {


  behavior of "readCsv"

  it should "return Transactions from valid csv" in {

    val expectedOutput = List(
      Transaction("1", "A1", 1, "GG", 111.11),
      Transaction("2", "A2", 2, "BB", 222.22)
    )

    val pathToTestFile = getClass.getClassLoader.getResource("testCsv").getPath

    val actualOutput = CsvHelper.readCsv(pathToTestFile)

    actualOutput shouldBe expectedOutput
  }

  it should "skip over invalid csv rows" in {

    val expectedOutput = List(Transaction("1", "A1", 1, "GG", 111.11))

    val pathToTestFile = getClass.getClassLoader.getResource("partiallyInvalidCsv").getPath

    val actualOutput = CsvHelper.readCsv(pathToTestFile)

    actualOutput shouldBe expectedOutput
  }


}
