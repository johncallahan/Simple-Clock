package com.simplemobiletools.clock.helpers

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.simplemobiletools.clock.extensions.config
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow

class Clock(context: Context?) : View(context) {

    var WIDTH = 500
    var HEIGHT = 500
    var SCALE = 1f
    var RADIUS = 0

    var mBottom = 500
    var mTop = 0
    var mLeft = 0
    var mRight = 500

    override fun onDraw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas)

        val mCalendar = Calendar.getInstance()
        RADIUS = Math.min(WIDTH,HEIGHT) / 2

        var hourDotColor = Color.RED
        var minuteDotColor = Color.WHITE
        var handsColor = Color.YELLOW
        var secondDotColor = Color.RED
        val locationDotColor = Color.YELLOW
        val showDate = false
        val showSecond = false
        val showLocation = true
        var datePrimaryColor = Color.WHITE
        var dateSecondaryColor = Color.WHITE
        var centerCirclePrimaryColor = Color.GREEN
        var centerCircleSecondaryColor = Color.BLUE

        /** The coordinates used to paint the clock hands.  */
        var xHandSec: Int = 0
        var yHandSec: Int = 0
        var xHandMin: Int = 0
        var yHandMin: Int = 0
        var xHandHour: Int = 0
        var yHandHour: Int = 0
        var xHandLocation: Int = 0
        var yHandLocation: Int = 0

        var xHandHourLHR: Int = 0
        var yHandHourLHR: Int = 0

        var xHandHourSFO: Int = 0
        var yHandHourSFO: Int = 0

        var xHandHourSIN: Int = 0
        var yHandHourSIN: Int = 0

        /** The size of the clock.  */
        //WIDTH = if(this.measuredWidth == 0) 400 else this.measuredWidth
        //HEIGHT = if(this.measuredHeight == 0) 400 else this.measuredHeight

        Log.i("WIDTH = ", WIDTH.toString())
        Log.i("HEIGHT = ", HEIGHT.toString())

        /** The length of the clock hands relative to the clock size.  */
        val secondHandLength = WIDTH / 2 - 30
        val minuteHandLength = WIDTH / 2 - 100
        val hourHandLength = WIDTH / 2 - 130
        val locationHandLength = WIDTH / 2 - 110

        /** The distance of the dots from the origin (center of the clock).  */
        //private val DISTANCE_DOT_FROM_ORIGIN = WIDTH / 2 - 30
        val DISTANCE_DOT_FROM_ORIGIN = WIDTH / 2 - 65

        val DIAMETER_BIG_DOT = 5
        val DIAMETER_SMALL_DOT = 3
        val DIAMETER_SECOND_DOT = 8
        val DIAMETER_LOCATION_DOT = 20
        val DIAMETER_CENTER_BIG_DOT = 14
        val DIAMETER_CENTER_SMALL_DOT = 6

        val currentSecond = LocalDateTime.now().second
        val currentMinute = LocalDateTime.now().minute
        val currentHour = LocalDateTime.now().hour
        val relativeHour = getRelativeHour(currentMinute)

        xHandSec = old_minToLocation(currentSecond, secondHandLength, WIDTH, HEIGHT).x
        yHandSec = old_minToLocation(currentSecond, secondHandLength, WIDTH, HEIGHT).y
        xHandMin = old_minToLocation(currentMinute, minuteHandLength, WIDTH, HEIGHT).x
        yHandMin = old_minToLocation(currentMinute, minuteHandLength, WIDTH, HEIGHT).y
        xHandHour = minToLocation(currentHour * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).x
        yHandHour = minToLocation(currentHour * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).y

        xHandLocation = minToLocation(currentHour * 3 + relativeHour, locationHandLength, WIDTH, HEIGHT).x
        yHandLocation = minToLocation(currentHour * 3 + relativeHour, locationHandLength, WIDTH, HEIGHT).y

        xHandHourLHR = minToLocation(((currentHour + 5) % 24) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).x
        yHandHourLHR = minToLocation(((currentHour + 5) % 24) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).y

        xHandHourSFO = minToLocation(((if (currentHour < 3)  24 + currentHour else currentHour) - 3) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).x
        yHandHourSFO = minToLocation(((if (currentHour < 3)  24 + currentHour else currentHour) - 3) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).y

        xHandHourSIN = minToLocation(((currentHour + 13) % 24) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).x
        yHandHourSIN = minToLocation(((currentHour + 13) % 24) * 3 + relativeHour, secondHandLength, WIDTH, HEIGHT).y

        val availW = mRight - mLeft
        val availH = mBottom - mTop

        val wScale = availW.div(WIDTH)
        val hScale = availH.div(HEIGHT)
        val scale = Math.min(wScale,hScale)
        SCALE = scale.toFloat()

        val cX = availW / 2
        val cY = availH / 2

        val w = (WIDTH * SCALE).toInt()
        val h = (HEIGHT * SCALE).toInt()

        var scaled = false

        if (availW < w || availH < h) {
            scaled = true
            val scale = Math.min(availW.toFloat() / w.toFloat(),
                availH.toFloat() / h.toFloat())
            canvas.save()
            canvas.scale(scale, scale, cX.toFloat(), cY.toFloat())
        }

        val paint = Paint()
        paint.isAntiAlias = true

        // Draw the dots
        //for (i in 0..59) {

        //    val dotCoordinates = minToLocation(i, DISTANCE_DOT_FROM_ORIGIN)

        //    if (i % 5 == 0) {
        // big dot
        //        paint.color = hourDotColor
        //        canvas.drawCircle((dotCoordinates.x - DIAMETER_BIG_DOT.times(SCALE) / 2),
        //                (dotCoordinates.y - DIAMETER_BIG_DOT.times(SCALE) / 2),
        //                DIAMETER_BIG_DOT.times(SCALE),
        //                paint)
        //    } else {
        // small dot
        //        paint.color = minuteDotColor
        //        canvas.drawCircle((dotCoordinates.x - DIAMETER_SMALL_DOT.times(SCALE) / 2),
        //                (dotCoordinates.y - DIAMETER_SMALL_DOT.times(SCALE) / 2),
        //                DIAMETER_SMALL_DOT.times(SCALE),
        //                paint)
        //    }
        //}

        paint.color = datePrimaryColor
        paint.textSize = 25f.times(SCALE)

        //Draw Date
        if(showDate) {
            paint.isFakeBoldText = true
            //val typeface = ResourcesCompat.getFont(context, R.font.k2d)
            //paint.typeface = typeface
            val distanceFromBottom = 210.times(SCALE)
            //Draw year
            paint.color = datePrimaryColor
            val yearSize = Rect()
            val yearName = mCalendar.get(Calendar.YEAR).toString()
            paint.textSize = 25f.times(SCALE)
            paint.getTextBounds(yearName, 0, yearName.length, yearSize)
            val yearX = WIDTH.times(SCALE) / 2 - yearSize.width() / 2 - yearSize.left
            val yearY = HEIGHT.times(SCALE) - distanceFromBottom
            canvas.drawText(yearName, yearX, yearY, paint)
            //Draw day
            //paint.color = dateSecondaryColor
            val daySize = Rect()
            val dayName = to2Digit(mCalendar.get(Calendar.DAY_OF_MONTH))
            paint.textSize = 35f.times(SCALE)
            paint.getTextBounds(dayName, 0, dayName.length, daySize)
            val dayX = WIDTH.times(SCALE) / 2 - daySize.width() / 2 - daySize.left
            val dayY = HEIGHT.times(SCALE) - distanceFromBottom - yearSize.height() - 10
            canvas.drawText(dayName, dayX, dayY, paint)
            //Draw Month
            paint.color = datePrimaryColor
            val monthSize = Rect()
            val monthName = mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US).toUpperCase()
            paint.textSize = 25f.times(SCALE)
            paint.getTextBounds(monthName, 0, monthName.length, monthSize)
            val monthX = WIDTH.times(SCALE) / 2 - monthSize.width() / 2 - monthSize.left
            val monthY = HEIGHT.times(SCALE) - distanceFromBottom - (daySize.height() + yearSize.height()) - 20
            canvas.drawText(monthName, monthX, monthY, paint)
        }

        // Draw the clock hands
        //canvas.drawLine((WIDTH.times(SCALE) / 2)
        //        , (HEIGHT.times(SCALE) / 2)
        //        , xHandMin.toFloat(), yHandMin.toFloat(), paint)
        //canvas.drawLine((WIDTH.times(SCALE) / 2)
        //        , (HEIGHT.times(SCALE) / 2)
        //        , xHandHour.toFloat()
        //        , yHandHour.toFloat(), paint)

        //Draw boundary circle
        //paint.color = centerCirclePrimaryColor
        //paint.setStyle(Paint.Style.STROKE)
        //canvas.drawCircle((WIDTH.times(SCALE) / 2)
        //        , (HEIGHT.times(SCALE) / 2)
        //        , DIAMETER_CENTER_BIG_DOT.times(SCALE * 12)
        //        , paint)

        // Draw time
        //paint.color = hourDotColor
        //canvas.drawCircle(xHandHour.toFloat(),
        //        yHandHour.toFloat(),
        //        DIAMETER_BIG_DOT.times(2).times(SCALE),
        //        paint)
        paint.color = minuteDotColor
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = 8f.times(SCALE)
        paint.strokeCap = Paint.Cap.ROUND
        paint.isFakeBoldText = true
        canvas.drawText(
            "IAD",
            xHandHour.toFloat(),
            yHandHour.toFloat() - ((paint.descent() + paint.ascent()) / 2),
            paint)
        canvas.drawText(
            "LHR",
            xHandHourLHR.toFloat(),
            yHandHourLHR.toFloat() - ((paint.descent() + paint.ascent()) / 2),
            paint)
        canvas.drawText(
            "SFO",
            xHandHourSFO.toFloat(),
            yHandHourSFO.toFloat() - ((paint.descent() + paint.ascent()) / 2),
            paint)
        canvas.drawText(
            "SIN",
            xHandHourSIN.toFloat(),
            yHandHourSIN.toFloat() - ((paint.descent() + paint.ascent()) / 2),
            paint)

        // Draw the dots
        //for (i in 0..59) {
        for (i in 0..71) {

            val dotCoordinates = minToLocation(i, DISTANCE_DOT_FROM_ORIGIN, WIDTH, HEIGHT)

            Log.i("i = ", i.toString())
            Log.i("dotCoordinates.x = ", dotCoordinates.x.toString())
            Log.i("dotCoordinates.y = ", dotCoordinates.y.toString())

            if (i % 6 == 0) {
                // big dot
                paint.color = minuteDotColor
                paint.textAlign = Paint.Align.CENTER
                paint.strokeWidth = 4f.times(SCALE)
                //canvas.drawCircle(dotCoordinates.x.toFloat(),
                //        dotCoordinates.y.toFloat(),
                //        DIAMETER_BIG_DOT.times(SCALE),
                //        paint)
                canvas.drawText(
                    (i / 3).toString(),
                    dotCoordinates.x.toFloat(),
                    dotCoordinates.y.toFloat() - ((paint.descent() + paint.ascent()) / 2),
                    paint)
            } else if (i % 3 == 0) {
                // small dot
                paint.color = minuteDotColor
                canvas.drawCircle(dotCoordinates.x.toFloat(),
                    dotCoordinates.y.toFloat(),
                    DIAMETER_SMALL_DOT.times(SCALE),
                    paint)
            }
        }

        //Draw clock second hands
        if(showSecond) {
            paint.color = Color.WHITE
            canvas.drawCircle((xHandSec - DIAMETER_SECOND_DOT.times(SCALE) / 2)
                , (yHandSec - DIAMETER_SECOND_DOT.times(SCALE) / 2)
                , (DIAMETER_SECOND_DOT.times(SCALE) + 2)
                , paint)
            paint.color = secondDotColor
            canvas.drawCircle((xHandSec - DIAMETER_SECOND_DOT.times(SCALE) / 2)
                , (yHandSec - DIAMETER_SECOND_DOT.times(SCALE) / 2)
                , DIAMETER_SECOND_DOT.times(SCALE)
                , paint)
        }

        if(showLocation) {
            paint.color = locationDotColor
            canvas.drawCircle(xHandLocation.toFloat()
                , yHandLocation.toFloat() - ((paint.descent() + paint.ascent()) / 2)
                , DIAMETER_LOCATION_DOT.times(SCALE)
                , paint)
        }

        //Draw center circle
        //paint.color = centerCirclePrimaryColor
        //canvas.drawCircle((WIDTH.times(SCALE) / 2)
        //        , (HEIGHT.times(SCALE) / 2)
        //        , DIAMETER_CENTER_BIG_DOT.times(SCALE)
        //        , paint)
        //paint.color = centerCircleSecondaryColor
        //canvas.drawCircle((WIDTH.times(SCALE) / 2)
        //        , (HEIGHT.times(SCALE) / 2)
        //        , DIAMETER_CENTER_SMALL_DOT.times(SCALE)
        //        , paint)

        if (scaled) {
            canvas.restore()
        }
    }

    private var xIsPos = true
    private var yIsPos = true

    private fun getRelativeHour(min: Int): Int {
        return min / 20
    }

    private fun old_minToLocation(timeStep: Int, radius: Int, width: Int, height: Int): Point {
        val t =  2.0 * Math.PI * (timeStep - 15).toDouble() / 60
        when (timeStep) {
            in 0..15 -> {
                xIsPos = true
                yIsPos = true
            }
            in 16..30 -> {
                xIsPos = true
                yIsPos = false
            }
            in 31..45 -> {
                xIsPos = false
                yIsPos = false
            }
            in 46..60 -> {
                xIsPos = false
                yIsPos = true
            }
        }

        val xDegree = if(xIsPos) Math.cos(t).absoluteValue.pow(11.0/11)
        else Math.cos(t).absoluteValue.pow(11.0/11).times(-1)

        val yDegree = - if(yIsPos) Math.sin(t).absoluteValue.pow(11.0/11)
        else Math.sin(t).absoluteValue.pow(11.0/11).times(-1)

        val x = (width.times(SCALE) / 2 + radius.times(SCALE) * xDegree)
        val y = (height.times(SCALE) / 2 + radius.times(SCALE) * yDegree)

        return Point(x.toInt(),y.toInt())
    }

    private fun minToLocation(timeStep: Int, radius: Int, width: Int, height: Int): Point {
        val t =  2.0 * Math.PI * (timeStep + 18).toDouble() / 72

        Log.i("width = ", width.toString())
        Log.i("height = ", height.toString())
        Log.i("SCALE = ", SCALE.toString())
        Log.i("t = ", t.toString())

        when (timeStep) {
            in 0..17 -> {
                xIsPos = false
                yIsPos = false
            }
            in 18..35 -> {
                xIsPos = false
                yIsPos = true
            }
            in 36..53 -> {
                xIsPos = true
                yIsPos = true
            }
            in 54..71 -> {
                xIsPos = true
                yIsPos = false
            }
        }

        val xDegree = if(xIsPos) Math.cos(t).absoluteValue.pow(11.0/11)
        else Math.cos(t).absoluteValue.pow(11.0/11).times(-1)

        val yDegree = - if(yIsPos) Math.sin(t).absoluteValue.pow(11.0/11)
        else Math.sin(t).absoluteValue.pow(11.0/11).times(-1)

        val x = (width.times(SCALE) / 2 + radius.times(SCALE) * xDegree)
        val y = (height.times(SCALE) / 2 + radius.times(SCALE) * yDegree)

        return Point(x.toInt(),y.toInt())
    }

    private fun to2Digit(number: Number): String {
        return String.format(Locale.US, "%02d", number)
    }

}
