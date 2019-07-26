package com.timon.stream

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.scala._

object SocketWindowWordCount {
  def main(args: Array[String]) : Unit = {

    // get the execution environment
    // val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI()
    //val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 8081, "D:\\code\\TiMon\\web\\target\\web-0.1.jar")
    //val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 8081)

    //env.getConfig.disableSysoutLogging();

    // get input data by connecting to the socket
    val text = env.socketTextStream("172.16.16.5", 9000, '\n')

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("level")
      .timeWindow(Time.seconds(1000), Time.seconds(60))
      .sum("count")

    // print the results with a single thread, rather than in parallel
    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")
  }

  // Data type for words with count
  case class WordWithCount(word: String, count: Long)

}
