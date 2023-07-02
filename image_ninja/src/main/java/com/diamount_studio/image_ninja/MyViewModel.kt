package com.diamount_studio.image_ninja

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(private val imageLoader: ImageLoader) : ViewModel() {

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _bitmapState = MutableLiveData<Bitmap?>()
    val bitmapState: LiveData<Bitmap?> = _bitmapState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun loadImage(url: String) {
        _imageUrl.value = url
    }

    fun loadImageWithTransformations(url: String, options: ImageOptions) {
        viewModelScope.launch {
            _loadingState.value = true
            val loadedBitmap = withContext(Dispatchers.IO) {
                imageLoader.load(url, options)
            }
            _bitmapState.value = loadedBitmap
            _loadingState.value = false
        }
    }
}
