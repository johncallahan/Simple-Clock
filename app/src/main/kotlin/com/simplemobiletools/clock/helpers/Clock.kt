package com.simplemobiletools.clock.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Clock(context: Context?) : View(context) {

    override fun onDraw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas)
        Log.i("GOODBYE","EARTH")
        val pBackground = Paint()
        pBackground.setColor(Color.TRANSPARENT)
        canvas.drawRect(0F, 0F, 512F, 512F, pBackground)
        val pText = Paint()
        pText.setColor(Color.BLACK)
        pText.setTextSize(20F)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)
        canvas.drawText(formatted, 100F, 100F, pText)
    }

}
