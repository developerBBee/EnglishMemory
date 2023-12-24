package jp.developer.bbee.englishmemory.common.constant

import java.time.format.DateTimeFormatter

val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
val DATE_JP_FORMATTER: DateTimeFormatter  = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
val TIME_JP_FORMATTER: DateTimeFormatter  = DateTimeFormatter.ofPattern("HH:mm:ss")