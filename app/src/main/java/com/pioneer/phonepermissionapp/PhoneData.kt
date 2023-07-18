package com.pioneer.phonepermissionapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Telephony
import android.telephony.TelephonyManager

class PhoneData {

    companion object {

        fun getPhoneDetails(context: Context): String {
            // Retrieve the TelephonyManager instance
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Get the phone details
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val device = Build.DEVICE
            val version = Build.VERSION.SDK_INT.toString()
            val androidVersion = Build.VERSION.RELEASE

            // Get the IMEI number
//            val imei = telephonyManager.imei

            // Display the phone details
            println("Manufacturer: $manufacturer")
            println("Model: $model")
            println("Device: $device")
            println("Android Version: $androidVersion")
            println("API Level: $version")
//            println("IMEI: $imei")

            return "Manufacturer: $manufacturer\nModel: $model\nDevice: $device\nAndroid Version: $androidVersion\nAPI Level: $version\nIMEI: no permission"
        }


        // 1.reading all messages
        @SuppressLint("Range")
        fun getAllMessages(context: Context): ArrayList<Message> {
            val messages = ArrayList<Message>()

            val uri: Uri = Uri.parse("content://sms")
            val projection = arrayOf(
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE
            )

            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val messageId = cursor.getLong(cursor.getColumnIndex(Telephony.Sms._ID))
                    val address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))
                    val date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE))
                    val type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE))

                    // Create a Message object and add it to the list
                    val message = Message(messageId, address, body, date, type)
                    messages.add(message)
                }
            }

            cursor?.close()

            return messages
        }

        data class Message(
            val id: Long,
            val address: String,
            val body: String,
            val date: Long,
            val type: Int
        )

        // 2.reading call logs
        @SuppressLint("Range")
        fun getAllCallLogs(context: Context): ArrayList<CallLogItem> {
            val callLogs = ArrayList<CallLogItem>()

            val projection = arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
            )

            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val callLogId = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID))
                    val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                    val duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                    val type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))

                    // Create a CallLogItem object and add it to the list
                    val callLogItem = CallLogItem(callLogId, number, date, duration, type)
                    callLogs.add(callLogItem)
                }
            }

            cursor?.close()

            return callLogs
        }

        data class CallLogItem(
            val id: Long,
            val number: String,
            val date: Long,
            val duration: Long,
            val type: Int
        )

        // 3.reading all contacts
        @SuppressLint("Range")
        fun getAllContacts(context: Context): ArrayList<Contact> {
            val contacts = ArrayList<Contact>()

            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
            )

            val cursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val contactId =
                        cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    // Create a Contact object and add it to the list
                    val contact = Contact(contactId, displayName)
                    contacts.add(contact)
                }
            }

            cursor?.close()

            return contacts
        }

        data class Contact(val id: Long, val displayName: String)

        // 4.reading all music files
        fun getAllMusicFiles(context: Context): List<MusicModel> {
            val musicFiles = mutableListOf<MusicModel>()

            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TRACK,
            )
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )

            cursor?.use { cursor ->
                val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                while (cursor.moveToNext()) {
                    val filePath = cursor.getString(columnIndex)
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val album: String? = cursor.getString(albumIndex)
                    musicFiles.add(MusicModel(album, title, artist, filePath))
                }
            }

            return musicFiles
        }


        // 5.reading all image files
        fun getAllImageFiles(context: Context): List<String> {
            val images = mutableListOf<String>()

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                null,
                null,
                sortOrder
            )

            cursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (cursor.moveToNext()) {
//                    val id = cursor.getLong(idColumn)
//                    val name = cursor.getString(nameColumn)
                    val data = cursor.getString(dataColumn)

                    // You can use the 'id', 'name', and 'data' as per your requirement
                    images.add(data)
                }
            }

            return images
        }

        // 6.reading all video files
        fun getAllVideoFiles(context: Context): List<String> {
            val images = mutableListOf<String>()

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
            )

            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

            val queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                null,
                null,
                sortOrder
            )

            cursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

                while (cursor.moveToNext()) {
//                    val id = cursor.getLong(idColumn)
//                    val name = cursor.getString(nameColumn)
                    val data = cursor.getString(dataColumn)

                    // You can use the 'id', 'name', and 'data' as per your requirement
                    images.add(data)
                }
            }

            return images
        }

    }
}