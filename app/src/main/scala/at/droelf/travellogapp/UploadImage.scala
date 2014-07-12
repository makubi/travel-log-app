package at.droelf.travellogapp

import org.joda.time.LocalDateTime

case class UploadImage(dateTime: LocalDateTime, timezone: String, name: String, imagePath: String)
