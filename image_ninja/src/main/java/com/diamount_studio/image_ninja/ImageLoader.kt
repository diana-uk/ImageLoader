package com.diamount_studio.image_ninja

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageLoader(private val context: Context) {

    private val memoryCache: LruCache<String, Bitmap> = createMemoryCache()

    suspend fun load(url: String, options: ImageOptions = ImageOptions()): Bitmap? {
        // Check if the image is already cached
        val cachedBitmap = memoryCache.get(url)
        if (cachedBitmap != null) {
            return cachedBitmap
        }

        return loadImageFromUrl(url, options)?.also { loadedBitmap ->
            // Cache the loaded image
            memoryCache.put(url, loadedBitmap)
        }
    }

    private suspend fun loadImageFromUrl(url: String, options: ImageOptions): Bitmap? {
        // Use your preferred library or method for image loading
        // Example: Glide, Coil, Picasso, etc.

        val glideRequest = Glide.with(context)
            .asBitmap()
            .load(url)

        // Apply transformations
        options.applyTransformations(glideRequest)

        return withContext(Dispatchers.IO) {
            glideRequest.submit().get()
        }
    }

    private fun createMemoryCache(): LruCache<String, Bitmap> {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        return object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes
                return bitmap.byteCount / 1024
            }
        }
    }
}
