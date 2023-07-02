package com.diamount_studio.imageninja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diamount_studio.image_ninja.ImageLoader
import com.diamount_studio.image_ninja.MyViewModel
import com.diamount_studio.image_ninja.MyViewModelFactory
import com.diamount_studio.image_ninja.PhotoEditorApp
import com.diamount_studio.imageninja.ui.theme.ImageNinjaTheme

class MainActivity : ComponentActivity() {
    private val imageLoader = ImageLoader(this)
    private val viewModel: MyViewModel by viewModels { MyViewModelFactory(imageLoader) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageNinjaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")
                    MyScreen(viewModel)
//                    PhotoEditorApp()



                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageNinjaTheme {
        Greeting("Android")
    }
}