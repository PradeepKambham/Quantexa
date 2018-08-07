
trait QuestionEntry {
  override def toString: String
  def getFieldsAsString: String
}

case class QuestionOneEntry(day: Int, dayTotal: Double) extends QuestionEntry {
  override def toString: String = s"$day,$dayTotal"
  def getFieldsAsString: String = "day,dayTotal"
}


case class QuestionTwoEntry(accountId: String, transactionTypeAndAverage: Map[String, Double]) extends QuestionEntry {
  override def toString: String = {
    s"$accountId,${transactionTypeAndAverage.toList.sortBy(_._1).map(_._2).mkString(",")}"
  }
  def getFieldsAsString: String = "accountId,AA,BB,CC,DD,EE,FF,GG"
}


case class QuestionThreeEntry(day: Int,
                              accountId: String,
                              maximum: Double,
                              average: Double,
                              AATotal: Double,
                              CCTotal: Double,
                              FFTotal: Double) extends QuestionEntry {
  override def toString: String = s"$day,$accountId,$maximum,$average,$AATotal,$CCTotal,$FFTotal"
  def getFieldsAsString: String = "day,accountId,maximum,average,AATotal,CCTotal,FFTotal"
}
