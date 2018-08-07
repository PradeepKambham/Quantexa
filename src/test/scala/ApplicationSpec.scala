import org.scalatest.{FlatSpec, Matchers}


class ApplicationSpec extends FlatSpec with Matchers {

  behavior of "roundTo2DP"

  it should "round a number with many decimals to 2 decimal points" in {
    Application.roundTo2DP(1.5677777) shouldBe 1.57
    Application.roundTo2DP(1.565) shouldBe 1.57
    Application.roundTo2DP(1.574) shouldBe 1.57
  }

  it should "not round numbers already 2 decimal places or less" in {
    Application.roundTo2DP(1.5) shouldBe 1.5
    Application.roundTo2DP(1.54) shouldBe 1.54
    Application.roundTo2DP(1) shouldBe 1
  }

  it should "return 0 if given invalid double" in {
    Application.roundTo2DP(Double.NaN) shouldBe 0
  }

  behavior of "questionOne"

  it should "Calculate the total transaction value for all transactions for each day." in {

    val expectedOutput = List(
      QuestionOneEntry(1, 1515.86),
      QuestionOneEntry(2, 816.23),
      QuestionOneEntry(3, 1765.27),
      QuestionOneEntry(4, 816.78))

    val actualOutput = Application.questionOne(TestData.questionOneInput)
    actualOutput shouldBe expectedOutput
  }

  behavior of "questionTwo"

  it should "calculate the average transaction of each type for each account" in {

    val expectedOutput = List(
      QuestionTwoEntry("A1", Map(
        "AA" -> 579.42,
        "BB" -> 555.23,
        "CC" -> 408.11,
        "DD" -> 439.49,
        "EE" -> 400.42,
        "FF" -> 508.0,
        "GG" -> 448.24)),
      QuestionTwoEntry("A2", Map(
        "AA" -> 310.26,
        "BB" -> 505.29,
        "CC" -> 476.88,
        "DD" -> 848.9,
        "EE" -> 416.36,
        "GG" -> 526.29))
    )

    val actualOutput = Application.questionTwo(TestData.questionTwoInput)

    actualOutput shouldBe expectedOutput
  }

  behavior of "createDateRange"

  it should "create a range of days when given a good input" in {
    Application.createDateRange(10) shouldBe Some(5 to 9)
    Application.createDateRange(20) shouldBe Some(15 to 19)
    Application.createDateRange(27) shouldBe Some(22 to 26)
  }

  it should "create a shortened range if given a low number as input" in {
    Application.createDateRange(5) shouldBe Some(1 to 4)
    Application.createDateRange(3) shouldBe Some(1 to 2)
  }

  it should "return None if there the input day is 1 or lower" in {
    Application.createDateRange(1) shouldBe None
    Application.createDateRange(0) shouldBe None
    Application.createDateRange(-10) shouldBe None
  }

  behavior of "calculatePreviousFiveDaysStats"

  it should "work out the stats for the last five days for any account in the input data" in {

    val expectedOutput = List(
      QuestionThreeEntry(6,"A1",17.0,5.0,42.0,12.0,4.0),
      QuestionThreeEntry(6,"A2",22.0,4.0,2.0,23.0,3.0)
    )

    val actualOutput = Application.calculatePreviousFiveDaysStats(TestData.questionThreeInput, 6)
    actualOutput shouldBe expectedOutput
  }

  it should "return an empty list if the given day is invalid or 1" in {
    Application.calculatePreviousFiveDaysStats(TestData.questionThreeInput, day = 1) shouldBe List()
  }

}
