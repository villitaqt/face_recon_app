package com.facerecon.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

object CameraUtils {
    
    fun ImageProxy.toBitmap(): Bitmap? {
        val buffer: ByteBuffer = planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    
    fun Bitmap.crop(rect: Rect): Bitmap {
        return Bitmap.createBitmap(this, rect.left, rect.top, rect.width(), rect.height())
    }
    
    fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
        val ratio = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()
        
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    }
} 