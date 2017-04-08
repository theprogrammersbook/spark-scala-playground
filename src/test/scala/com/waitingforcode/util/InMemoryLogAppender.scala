package com.waitingforcode.util

import org.apache.log4j.{ConsoleAppender, Level, Logger, PatternLayout}
import org.apache.log4j.spi.LoggingEvent

import scala.collection.mutable.ListBuffer

class InMemoryLogAppender(patterns:Seq[String]) extends ConsoleAppender {

  var messages:ListBuffer[LogMessage] = new ListBuffer()

  override def append(event: LoggingEvent) = {
    val matchingMessage = patterns.find(event.getMessage.toString.contains(_))
    if (matchingMessage.isDefined) {
      messages += LogMessage(event.getMessage.toString, event.getLoggerName)
    }
  }

  def getMessagesText(): Seq[String] = {
    messages.map(_.message)
  }

}


case class LogMessage(message: String, loggingClass: String)

object InMemoryLogAppender {

  def createLogAppender(patterns:Seq[String]): InMemoryLogAppender = {
    val testAppender = new InMemoryLogAppender(patterns)
    val pattern = "%m"
    testAppender.setLayout(new PatternLayout(pattern))
    testAppender.setThreshold(Level.TRACE)
    testAppender.activateOptions()
    Logger.getRootLogger().addAppender(testAppender)
    testAppender
  }
}
