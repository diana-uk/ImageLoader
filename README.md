Sure! Here's a sample README file for the given code:

```markdown
# ImageLoader

ImageLoader is a utility class that provides functionality for loading and caching images from URLs. It supports applying various image transformations using the ImageOptions class.

## Usage

To use the ImageLoader class, follow these steps:

1. Create an instance of ImageLoader by passing a context to the constructor.

```kotlin
val imageLoader = ImageLoader(context)
```

2. Load an image from a URL using the `load` function.

```kotlin
val bitmap = imageLoader.load(url, options)
```

The `load` function takes an optional `options` parameter of type `ImageOptions`, which allows you to specify transformation options for the loaded image.

## ImageOptions

The ImageOptions class allows you to specify various transformation options for the loaded image. It provides the following options:

- `isCircleCropEnabled`: A boolean value indicating whether to apply circle cropping to the image.

- `isGrayscaleEnabled`: A boolean value indicating whether to apply grayscale filtering to the image.

- `rotationAngle`: A float value representing the rotation angle (in degrees) to apply to the image.

You can create an instance of ImageOptions and configure the desired options:

```kotlin
val options = ImageOptions().apply {
    isCircleCropEnabled.value = true
    isGrayscaleEnabled.value = true
    rotationAngle.value = 90f
}
```

You can also use the provided extension functions to add custom transformations to the ImageOptions:

```kotlin
options.addTransformation(transformation)
options.grayscale()
```

## Dependencies

The ImageLoader class relies on the following dependencies:

- Glide: A fast and efficient image loading library for Android.

Make sure to add the required dependencies to your project's build.gradle file:

```groovy
dependencies {
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    kapt 'com.github.bumptech.glide:compiler:4.12.0'
}
```

## Contributing

Contributions to the ImageLoader project are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
```