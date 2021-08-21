package com.worstsoft.illyriadmanager

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.InputStream

class XMLParser {

    companion object {
        private val xmlPullParser: XmlPullParser = XmlPullParserFactory.newInstance().newPullParser()
        private val httpClient: HttpClient = HttpClient(CIO)
        private val NOTIFICATION_HOSTNAME: String = "https://elgea.illyriad.co.uk/external/notificationsapi/elgea-NOTIF-"

        suspend fun getNotification(apiKey: String): List<NotificationModel> {
            val res: String = httpClient.get<String>(NOTIFICATION_HOSTNAME + apiKey)

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlPullParser.setInput(res.byteInputStream(), "UTF-8")

            val array: ArrayList<NotificationModel> = ArrayList()

            var eventType: Int = xmlPullParser.eventType
            var notificationId: String? = null
            var notificationType: String?  = null
            var notificationOverallType: String?  = null
            var notificationTownId: String?  = null
            var notificationDetail: String?  = null
            var notificationDateAndTime: String?  = null

            while (eventType != XmlPullParser.END_DOCUMENT) {

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xmlPullParser.name) {
                            "notification" -> {
                                if (xmlPullParser.attributeCount == 0) {
                                    notificationId = null
                                    notificationType = null
                                    notificationOverallType = null
                                    notificationTownId = null
                                    notificationDetail = null
                                    notificationDateAndTime = null
                                } else {
                                    notificationId = xmlPullParser.getAttributeValue(0)
                                    eventType = xmlPullParser.nextTag()
                                    continue
                                }
                            }
                            "notificationtype" -> notificationType = xmlPullParser.getAttributeValue(0)
                            "notificationoveralltype" -> notificationOverallType = xmlPullParser.getAttributeValue(0)
                            "notificationtown" -> notificationTownId = xmlPullParser.getAttributeValue(0)
                            "notificationdetail" -> {
                                xmlPullParser.next()
                                var text = xmlPullParser.text
                                while (text.contains('[')) {
                                    var format = text.substring(text.indexOf('['),text.indexOf(']')+1)
                                    if (format.contains("@i")) {
                                        format = format.substring(format.indexOf('=')+1,format.indexOf(']'))
                                        when (format) {
                                            "1|1" -> format = "Wood(s)"
                                            "1|2" -> format = "Clay(s)"
                                            "1|3" -> format = "Iron(s)"
                                            "1|4" -> format = "Stone(s)"
                                            "1|5" -> format = "Food(s)"
                                            "3|1" -> format = "Horse(s)"
                                            "3|12" -> format = "Beer(s)"
                                            "4|1" -> format = "Gold(s)"
                                            "5|1" -> format = "Caravan(s)"
                                            "5|683" -> format = "Cotter(s)"
                                        }
                                    } else if (format.contains("@l")) {
                                        format = format.substring(format.indexOf('=')+1,format.indexOf(']'))
                                        format = format.replaceFirst('|', ' ', false)
                                        format = format.removeRange(format.indexOf('|'), format.length-1)
                                        val formatArray = format.split(" ")
                                        format = ""
                                        for (i in 0..1) {
                                            when (i) {
                                                0 -> {
                                                    format = format.plus("X: " + formatArray[i])
                                                }
                                                1 -> {
                                                    format = format.plus(" Y: " + formatArray[i])
                                                }
                                            }
                                        }
                                    } else if (format.contains("@c")) {
                                        format = format.substring(format.indexOf('=')+1,format.indexOf(']'))
                                        when (format) {
                                            "416" -> format = "Herb(s)"
                                        }
                                    } else format = format.substring(format.indexOf('=')+1,format.indexOf('|'))
                                    text = text.replaceRange(text.indexOf('['),text.indexOf(']')+1,format)
                                }
                                notificationDetail = text
                                continue
                            }
                            "notificationoccurrencedate" -> {
                                xmlPullParser.next()
                                notificationDateAndTime = xmlPullParser.text
                                continue
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (xmlPullParser.name.equals("notification") && xmlPullParser.attributeCount == -1) {
                            array.add(NotificationModel(
                                notificationId = notificationId!!.toInt(),
                                notificationType = NotificationModel.NotificationType.enumFromInt(notificationType!!.toInt()),
                                notificationOverallType = NotificationModel.NotificationOverallType.enumFromInt(notificationOverallType!!.toInt()),
                                notificationTownId = notificationTownId!!.toInt(),
                                notificationDetail = notificationDetail!!,
                                notificationDateAndTime = notificationDateAndTime!!
                            ))
                        }
                    }
                }



                eventType = xmlPullParser.next()
            }

            return array
        }
    }
    
}