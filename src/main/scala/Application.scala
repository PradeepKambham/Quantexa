
object Application extends App {

  val path = getClass.getClassLoader.getResource("transactions.txt").getPath
  val inputData = CsvHelper.readCsv(path)
  val q1 = questionOne(inputData)
  val q2 = questionTwo(inputData)
  val q3 = questionThree(inputData)

  private def formatContent(content: List[QuestionEntry]) = content.map(_.toString).mkString("\n")

  CsvHelper.writeCsv("q1.txt", s"${q1.head.getFieldsAsString}\n${formatContent(q1)}")
  CsvHelper.writeCsv("q2.txt", s"${q2.head.getFieldsAsString}\n${formatContent(q2)}")
  CsvHelper.writeCsv("q3.txt", s"${q3.head.getFieldsAsString}\n${formatContent(q3)}")


  def questionOne(inputData: List[Transaction]): List[QuestionOneEntry] = {
    inputData.groupBy(_.transactionDay)
      .mapValues(_.map(_.transactionAmount).sum).toList
      .map(tsByDay => QuestionOneEntry(tsByDay._1, roundTo2DP(tsByDay._2)))
      .sortBy(_.day)
  }

  def questionTwo(inputData: List[Transaction]): List[QuestionTwoEntry] = {
    inputData.groupBy(_.accountId)
      .map(tsByAccount => {
        QuestionTwoEntry(tsByAccount._1,
          tsByAccount._2.groupBy(_.category)
            .map(tsByCat => (tsByCat._1, getAverage(tsByCat._2)))
        )
      }).toList.sortBy(_.accountId.tail.toInt)
  }

  def questionThree(inputData: List[Transaction]): List[QuestionThreeEntry] = {
    (for {
      x <- 1 to 29
      dayStats <- calculatePreviousFiveDaysStats(inputData, x)
    } yield dayStats).toList
  }

  def calculatePreviousFiveDaysStats(inputData: List[Transaction], day: Int): List[QuestionThreeEntry] = {
    val daysRange = createDateRange(day)
    daysRange match {
      case None => List()
      case _ =>
        val inputForDays = inputData.filter(t => daysRange.get.toList.contains(t.transactionDay))
        val inputByAccount = inputForDays.groupBy(_.accountId)

        lazy val maxTransactions = inputByAccount
          .map(tsByAccount => (tsByAccount._1, tsByAccount._2.map(_.transactionAmount).max))

        lazy val average = inputByAccount
          .map(tsByAccount => (tsByAccount._1, getAverage(tsByAccount._2)))

        def totalFor(accountId: String, cat: String) = inputByAccount
          .map(tsByAccount => (
            tsByAccount._1,
            tsByAccount._2.filter(_.category == cat).map(_.transactionAmount).sum))
          .filter(_._1 == accountId).head._2

        inputByAccount
          .map(tsByAccount => QuestionThreeEntry(
            day,
            tsByAccount._1,
            maxTransactions(tsByAccount._1),
            average(tsByAccount._1),
            roundTo2DP(totalFor(tsByAccount._1, "AA")),
            roundTo2DP(totalFor(tsByAccount._1, "CC")),
            roundTo2DP(totalFor(tsByAccount._1, "FF"))
          )).toList.sortBy(_.accountId)
    }
  }

  def createDateRange(inputDay: Int): Option[Range] = {
    inputDay match {
      case x if x <= 1 => None
      case y if y <= 5 => Some(Range(1, inputDay))
      case _ => Some(Range(inputDay - 5, inputDay))
    }
  }

  private def getAverage(transactions: List[Transaction]): Double = {
    roundTo2DP(transactions.map(_.transactionAmount).sum / transactions.length)
  }

  def roundTo2DP(num: Double) = {
    if (num.isNaN) 0.0 else BigDecimal(num).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

}