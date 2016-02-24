import java.util
//http://spark.apache.org/docs/latest/ml-features.html
import org.apache.spark._
import scala.collection.mutable
import scala.util.control.NonFatal
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.streaming.dstream.DStream

import org.apache.spark.ml.feature.{HashingTF, IDF, RegexTokenizer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.feature.StopWordsRemover
object bookSearchEngine {
  // define the type used to store the list of documents that come in

  val sparkConf = new SparkConf().setAppName("NetworkWordCount")
  val sc = new SparkContext(sparkConf)
  val ssc = new StreamingContext(sc, Seconds(1))
  var count = 0
  var countComplete = 0

  // Turn off the 100's of messages
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  def main(args: Array[String]) {
    val documents = sc.wholeTextFiles("IR/files")
    val sqlContext = new SQLContext(sc)
    val docDataFrame = sqlContext.createDataFrame(documents).toDF("label","sentence")
    val regexTokenizer = new RegexTokenizer()
    .setInputCol("sentence")
    .setOutputCol("words")
    .setPattern("\\W")

    val regexTokenized = regexTokenizer.transform(docDataFrame)
    //regexTokenized.select("words", "label").take(1).foreach(println)
    regexTokenized.select("words", "label").take(1)(0).getAs[WrappedArray[String]](0).size
    val remover = new StopWordsRemover()
    .setInputCol("words")
    .setOutputCol("filtered")
    remover.transform(regexTokenized).show()
  }

}

 