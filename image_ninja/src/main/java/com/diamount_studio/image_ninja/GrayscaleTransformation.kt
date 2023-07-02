package com.diamount_studio.image_ninja

import android.graphics.*
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

class GrayscaleTransformation : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val transformedBitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return convertToGrayscale(transformedBitmap)
    }

    private fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter

        val matrix = Matrix()
        canvas.drawBitmap(bitmap, matrix, paint)

        return grayscaleBitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is GrayscaleTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "com.diamount_studio.image_ninja.GrayscaleTransformation"
        private val ID_BYTES: ByteArray = ID.toByteArray(Key.CHARSET)
    }

}

