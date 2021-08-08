package net.martinprobson.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions
import org.apache.spark.sql.types.{DoubleType, LongType}


object CustomerAccountQuestion extends App {

  val spark = SparkSession.builder().master("local[*]").appName("AccountAssignment").getOrCreate()

  import spark.implicits._

  case class CustomerData(customerId: String, forename: String, surname: String)

  case class AccountData(customerId: String, accountId: String, balance: Long)

  case class CustomerAccountOutput(customerId: String, forename: String, surname: String, accounts: Seq[AccountData], numberAccounts: BigInt, totalBalance: Long, averageBalance: Double)
  case class CustomerAccountJoined(customerId: String, forename: String, surname: String, accountId: String, balance: Long)

  val customerCSV = spark.read.option("header", "true").csv("customer_data.csv").as[CustomerData]
  val accountCSV = spark.read.option("header", "true").csv("account_data.csv").as[AccountData]

  val customerAccountOutput = customerCSV.joinWith(accountCSV, customerCSV("customerId") === accountCSV("customerId"), "inner")
    .map(record => CustomerAccountJoined(record._1.customerId, record._1.forename, record._1.surname, record._2.accountId, record._2.balance))
    .groupBy($"customerId")
    .agg(
      functions.count("*").cast(LongType).alias("numberAccounts"),
      functions.sum("balance").cast(LongType).alias("totalBalance"),
      functions.avg("balance").cast(DoubleType).alias("averageBalance"),
      functions.collect_list(functions.struct($"customerId", $"forename", $"surname")).alias("accounts"),
    ).select("customerId", "forename", "surname", "accounts", "numberAccounts", "totalBalance", "averageBalance")
    .map(row => (row.get(0).toString, row.getString(1), row.getString(2), AccountData(row.getList(3).get(0).toString, row.getList(3).get(1).toString, row.getList(3).get(2).asInstanceOf[Long]), row.get(4).asInstanceOf[BigInt], row.get(5).asInstanceOf[Long], row.get(6).asInstanceOf[Double]))
    .as[CustomerAccountOutput]
//    .groupBy(customerCSV("customerId")).agg(
//    functions.count("*").cast(LongType).alias("numberAccounts"),
//    functions.sum("balance").cast(LongType).alias("totalBalance"),
//    functions.avg("balance").cast(DoubleType).alias("averageBalance"),
//  )
//    functions.collect_list(functions.struct($"customerId", $"forename", $"surname")).alias("accounts"),
//  ).select("customerId", "forename", "surname", "accounts", "numberAccounts", "totalBalance", "averageBalance")
//    .map { row => CustomerAccountOutputw(functions.col("customerId"))
//    }
//    .as[CustomerAccountOutput]
}
