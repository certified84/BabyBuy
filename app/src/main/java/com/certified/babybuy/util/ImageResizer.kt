package com.certified.babybuy.util

import android.graphics.Bitmap
import android.util.Log
import kotlin.math.roundToInt
import kotlin.math.sqrt


object ImageResizer {
    //For Image Size 640*480, use MAX_SIZE =  307200 as 640*480 307200
    //private static long MAX_SIZE = 360000;
    //private static long THUMB_SIZE = 6553;
    fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap {
        Log.d("TAG", "reduceBitmapSize: Original size: ${bitmap.byteCount / 1048576} MB")
        val bitmapHeight: Int = bitmap.height
        val bitmapWidth: Int = bitmap.width
        val ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        Log.d("TAG", "Ratio: $ratio")
        val requiredHeight = (bitmapHeight / ratio).roundToInt()
        val requiredWidth = (bitmapWidth / ratio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }

    fun generateThumb(bitmap: Bitmap, THUMB_SIZE: Int): Bitmap {
        val bitmapHeight: Int = bitmap.height
        val bitmapWidth: Int = bitmap.width
        val ratioSquare = (bitmapHeight * bitmapWidth / THUMB_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        Log.d("TAG", "Ratio: $ratio")
        val requiredHeight = (bitmapHeight / ratio).roundToInt()
        val requiredWidth = (bitmapWidth / ratio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }
}