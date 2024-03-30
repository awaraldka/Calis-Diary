package com.callisdairy.Utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.callisdairy.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class DateFormat {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")


        fun  dateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun dateFormatPicker(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-MM-yyyy")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MM-yyyy")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun dateFormatFinal(date: String?): String? {
            var result = ""
            val sourceFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }






        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")
        fun covertTimeOtherFormat(dataDate: String): String {
            var convTime = ""
            val suffix = "ago."
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")

                //String time = getUtcTime(dataDate.substring(0,19));
                //  String[] timeie=dataDate.split("\\.");
                val date = sdf.parse(dataDate)
                val nowTime = Date()
                val dateDiff = nowTime.time - date!!.time
                try {
                    val newSdf = SimpleDateFormat("yyyy-MM-dd")
                    newSdf.timeZone = TimeZone.getTimeZone("UTC")
                    val startTime = date.let { newSdf.format(it) }
                    val endTime = newSdf.format(nowTime)
                    val localeDateStart = LocalDate.parse(startTime)
                    val localeDateEnd = LocalDate.parse(endTime)


                    var period = Period.between(localeDateStart,localeDateEnd)

                    var month = period.months
                    var year = period.years
                }catch (e: Exception) {
                    e.printStackTrace()
                }
//                var days = period.days
//                val weekOfMonth = calendar[Calendar.WEEK_OF_MONTH]
                val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
                val days = (nowTime.time - date.time)/(24*60*60*1000)

                if (second < 60) {
                    convTime = "Just Now"
                } else if (minute < 60) {
                    convTime = "$minute mins $suffix"
                } else if (hour < 24) {
                    convTime = "$hour hours $suffix"
                } else if (hour >= 24) {
                    convTime = "$days day $suffix"
                }
//                else if(days > 30) {
//                    convTime = "$month month $suffix"
//                } else if(month > 12) {
//                    convTime = "$year year $suffix"
//                }
            } catch (e: ParseException) {
                e.printStackTrace()
                //Log.e("ConvTimeE", e.getMessage());
            }
            return convTime
        }


        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")
        fun dateFormatEvent(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd yyyy hh:mm aa")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

//        2022-08-11T18:30:00.000Z

        fun getDate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy ")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun getTime(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("hh:mm aa")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun eventDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }



        fun getTimeOnly(date: String?): String? {
            var result = ""

//        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'");
            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            //  SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("hh:mm aa")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun getTimeOnly2(date: String?): String? {
            var result = ""

//        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'");
            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            //  SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("hh:mm aa")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun storyDateToTimestamp(date: String?): Long {
            val df: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(date)
            return  date.time
        }




        fun eventViewDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun petViewDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun filterDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
//            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }



        @SuppressLint("SimpleDateFormat")
        fun petDobDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-mm-yyyy")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }





//        All Common Methods


        @RequiresApi(Build.VERSION_CODES.O)
        fun dateSelector(context:Context,setText:TextView):String {
            val c = Calendar.getInstance()
            var dateFormat =""
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            c.set(year, month, date)
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                     dateFormat = dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")!!
                    setText.text = dateFormat
                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
            return dateFormat
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun dateSelectorFutureDates(context:Context,setText:TextView):String {
            val c = Calendar.getInstance()
            var dateFormat =""
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            c.set(year, month, date)
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                     dateFormat = dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")!!
                    setText.text = dateFormat
                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.minDate = c.timeInMillis

            datePickerDialog.show()
            return dateFormat
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(dateString: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val dateTime = LocalDateTime.parse(dateString, inputFormatter)
            return dateTime.format(outputFormatter)
        }

        fun convertTo12HourFormat(time: String): String {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

            val timeConverted = inputFormat.parse(time)
            val time12Hour = timeConverted?.let {
                outputFormat.format(it)
            }

            return time12Hour!!
        }


        @SuppressLint("SimpleDateFormat")
        fun convert24To12(time24: String): String {
            var time12 = ""
            try {
                val sdf24 = SimpleDateFormat("HH:mm")
                val sdf12 = SimpleDateFormat("hh:mm a")
                val date = sdf24.parse(time24)
                time12 = sdf12.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time12
        }




        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDate(): String {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return currentDate.format(formatter)
        }



        fun dateFormatCommon(inputDate: String?, inputFormat: String): String {
            if (inputDate.isNullOrEmpty()) return ""
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault())

            val date: Date?
            try {
                date = inputDateFormat.parse(inputDate)
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

            return date?.let { outputDateFormat.format(it) } ?: ""
        }



    }

}