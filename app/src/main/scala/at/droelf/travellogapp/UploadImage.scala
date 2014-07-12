package at.droelf.travellogapp

import org.joda.time.LocalDateTime

case class UploadImage(id: Long, dateTime: LocalDateTime, timezone: String, name: String, imagePath: String)
