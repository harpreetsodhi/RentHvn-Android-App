package com.project.renthvn

import java.time.LocalDateTime
import java.util.*

data class OrderClass(val Oid:String,
                      val Pid:String,
                      val Otime:LocalDateTime,
                      val Oimage:String,
                      val Oname:String,
                      val Osize:String,
                      val Operiod:Int,
                      val OeventDate:String,
                      val OdeliveryDate:String,
                      val OpickupDate:String,
                      val Ocolor:String,
                      val Odesc:String,
                      val Oprice:Double,
                      val Ogender:String)
{
}