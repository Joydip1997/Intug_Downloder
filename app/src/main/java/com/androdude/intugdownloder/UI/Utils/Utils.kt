package com.androdude.intugdownloder.UI.Utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class Utils {

    companion object {
        //check the correct url
        fun checkLink(url: String): Boolean {
            return url.contains("https://www.instagram.com") and (url.startsWith("https://") or url.startsWith(
                "http://"
            ))
        }

         fun getThePostId(url: String): String {
            try {
                val x = url.split("/".toRegex()).toTypedArray()
                val url = "https://www.instagram.com/p/" + x[4] + "/?__a=1"
                return url
            } catch (e: Exception) {

                return "There is a error.Please try again later"
            }

        }

        //Redundant function to download file
        fun downloadVideo(url: String,context : Context,isVideo : Boolean) {
            val fileName : String
            if(isVideo)
            {
                fileName = "video" + System.currentTimeMillis().toString() + ".mp4"
            }
            else
            {
                fileName = "video" + System.currentTimeMillis().toString() + ".png"
            }
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(fileName)
            request.setDescription("Please Wait....")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS
                , fileName
            )

            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }

        //helper function to download the image and video
        fun downloadFile(baseActivity:Context,url: String?,title: String?,isVideo : Boolean): Long {
            val direct = File(Environment.getExternalStorageDirectory().toString() + "/Intug")
            direct.mkdirs()
            val extension : String
            val fileType : String
            if(isVideo==false)
            {
                extension = ".png"
                fileType="intugimage"
            }
            else
            {
                extension = ".mp4"
                fileType="intugvideo"
            }
            val downloadReference: Long
            var  dm: DownloadManager
            dm= baseActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(
                "/Intug",
                fileType + System.currentTimeMillis() + extension
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(title)
            Toast.makeText(baseActivity, "start Downloading..", Toast.LENGTH_SHORT).show()

            downloadReference = dm?.enqueue(request) ?: 0

            return downloadReference



        }


        //Helper Function to convert the created time in manageable format
        fun setConvertedTime(duration : Long) : String
        {
            val now = Date()

            val secs = Integer.parseInt(TimeUnit.MILLISECONDS.toSeconds(now.time - duration).toString())
            val mins = Integer.parseInt(TimeUnit.MILLISECONDS.toMinutes(now.time - duration).toString())
            val hours = Integer.parseInt(TimeUnit.MILLISECONDS.toHours(now.time - duration).toString())
            val days = Integer.parseInt(TimeUnit.MILLISECONDS.toDays(now.time - duration).toString())

            if(secs < 60)
            {
                return "just now"
            }
            else if(mins == 1)
            {
                return "a minute ago"
            }
            else if (mins > 1 && mins < 60)
            {
                return mins.toString() + " minutes ago"
            }
            else if(hours == 1)
            {
                return "an hour ago"
            }
            else if(hours > 1 && hours < 24)
            {
                return hours.toString() + " hours ago"
            }
            else if( days == 1)
            {
                return "1 day ago"
            }
            else
            {
                return days.toString() + " days ago"
            }

        }

        fun checkFileExtention(file : File)  : Boolean
        {
            val file = Uri.fromFile(File(file.path))
            val fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString())
            return fileExt == "png"

        }



    }



}