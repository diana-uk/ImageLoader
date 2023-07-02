package com.diamount_studio.image_ninja

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

class ImageOptions(
    var isCircleCropEnabled:MutableState<Boolean> = mutableStateOf(false),
    var isGrayscaleEnabled:MutableState<Boolean> = mutableStateOf(false),
    var selectedTool:MutableState<Tool?> = mutableStateOf(null),
    var rotationAngle:MutableState<Float> =mutableStateOf(0f),

    private val transformations: MutableList<Transformation<Bitmap>> = mutableListOf()
) {
    fun addTransformation(transformation: Transformation<Bitmap>) {
        transformations.add(transformation)
    }

    fun addTransformation(transformation: BitmapTransformation) {
        transformations.add(transformation)
    }


//    fun applyTransformations(request: RequestBuilder<Bitmap>) {
//        for (transformation in transformations) {
//            request.transform(transformation)
//        }
//    }

    fun applyTransformations(request: RequestBuilder<Bitmap>) {
        if (isCircleCropEnabled.value) {
            request.circleCrop()
        }

    }
}

fun ImageOptions.grayscale() {
    addTransformation(GrayscaleTransformation())
}

fun applyRotation(bitmap: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}