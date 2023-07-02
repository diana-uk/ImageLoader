package com.diamount_studio.imageninja

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.diamount_studio.image_ninja.GrayscaleTransformation
import com.diamount_studio.image_ninja.ImageOptions
import com.diamount_studio.image_ninja.MyViewModel
import com.diamount_studio.image_ninja.applyRotation
import kotlinx.coroutines.launch

@Composable
fun MyScreen(viewModel: MyViewModel) {
    val imageUrl by viewModel.imageUrl.observeAsState("")
    val bitmapState by viewModel.bitmapState.observeAsState()

    // Transformation options with fade and slide animations
    val options = remember { ImageOptions() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image loading and display with animations
        AnimatedVisibility(
            visible = bitmapState != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface)
            ) {
                bitmapState?.let { bitmap ->
                    val transformedBitmap = applyImageFilters(LocalContext.current, bitmap, options)
                    Image(
                        bitmap = transformedBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to load image with scale animation
        @Composable
        fun LoadImageButton(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
        ) {
            val context = LocalContext.current

            Button(
                onClick = {
                    if (imageUrl.isNotEmpty()) {
                        onClick()
                    } else {
                        Toast.makeText(context, "No image URL was given", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = modifier,
                shape = CircleShape
            ) {
                Text(text)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Text input for image URL with floating label animation
        FloatingOutlinedTextField(
            value = imageUrl,
            onValueChange = { viewModel.loadImage(it) },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Checkbox(
                checked = options.isCircleCropEnabled.value,
                onCheckedChange = { options.isCircleCropEnabled.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.onSurface
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Circle Crop")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Checkbox(
                checked = options.isGrayscaleEnabled.value,
                onCheckedChange = { options.isGrayscaleEnabled.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.onSurface
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Grayscale")

        }
        // ... add more checkboxes for other transformation options

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = options.rotationAngle.value,
            onValueChange = { options.rotationAngle.value = it },
            valueRange = -180f..180f,
            steps = 1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Apply transformations with rotation animation
        val scope = rememberCoroutineScope()
        LoadImageButton(
            onClick = {
                scope.launch {
                    viewModel.loadImageWithTransformations(imageUrl, options)
                }
            },
            text = "Load Image with Transformations",
            modifier = Modifier
                .animateContentSize()
        )
    }
}

@Composable
fun applyImageFilters(context: Context, bitmap: Bitmap, options: ImageOptions): Bitmap {
    var transformedBitmap = bitmap

    // Apply filters
    if (options.isGrayscaleEnabled.value) {
        transformedBitmap = applyGrayscaleFilter(context, transformedBitmap)
    }

    if (options.rotationAngle.value != 0f) {
        transformedBitmap = applyRotation(transformedBitmap, options.rotationAngle.value)
    }
    return transformedBitmap
}

fun applyGrayscaleFilter(context: Context, transformedBitmap: Bitmap): Bitmap {
    val grayscaleTransformation = GrayscaleTransformation()
    val bitmapPool = Glide.get(context).bitmapPool
    val width = transformedBitmap.width
    val height = transformedBitmap.height
    val resource = BitmapResource(transformedBitmap, bitmapPool)
    val transformedResource = grayscaleTransformation.transform(context, resource, width, height)
    return transformedResource.get()
}

@Composable
fun FloatingOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val textFieldState = remember { mutableStateOf(TextFieldValue(value)) }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = textFieldState.value,
        onValueChange = {
            textFieldState.value = it
            onValueChange(it.text)
        },
        label = label,
        modifier = modifier.focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    focusRequester.requestFocus()
                }
            }
    )

    AnimatedVisibility(
        visible = textFieldState.value.text.isEmpty(),
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .padding(start = 8.dp, top = 4.dp)
        ) {
            label()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun LoadImageButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,

) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape
    ) {
        Text(text)
    }
}

fun Modifier.rotateIf(condition: Boolean, degrees: Float): Modifier =
    if (condition) {
        this.rotate(degrees)
    } else {
        this
    }

fun Modifier.scaleIf(condition: Boolean, scale: Float): Modifier =
    if (condition) {
        this.scale(scale)
    } else {
        this
    }