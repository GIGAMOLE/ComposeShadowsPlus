[![](/media/header.png)](https://intive.com/)

TODO: Android Arsenal | ![](https://jitpack.io/v/GIGAMOLE/ComposeShadowsPlus.svg) | [Setup Guide](#setup)
| [Report new issue](https://github.com/GIGAMOLE/ComposeShadowsPlus/issues/new)

# ComposeShadowsPlus

`ComposeShadowsPlus` is a powerful Android Compose library that empowers developers with the ability to easily create and customize stunning, high-quality shadows for
their UI elements.

![](/media/demo.gif)

Features:

- Android Compose custom shadows with offset, color, and spread support.
- `NativePaint.setShadowLayer(...)` & `View.LAYER_TYPE_SOFTWARE` based custom shadow.
- `RenderScript` & `ScriptIntrinsicBlur` based custom shadow.
- Advanced [sample app](#sample-app).

## Sample App

| RSBlur | SoftLayer | Elevation |
|-|-|-|
| <img src="/media/rs_blur.gif" width="200"/> | <img src="/media/soft_layer.gif" width="200"/> | <img src="/media/elevation.gif" width="200"/> |

Download or clone this repository to discover the sample app.

## Setup

Add to the root `build.gradle.kts`:

``` groovy
allprojects {
    repositories {
        ...
        maven("https://jitpack.io")
    }
}
```

Add to the package `build.gradle.kts`:

``` groovy
dependencies {
    implementation("com.github.GIGAMOLE:ComposeShadowsPlus:{latest-version}")
}
```

Latest version is: ![](https://jitpack.io/v/GIGAMOLE/ComposeShadowsPlus.svg).

Also, it's possible to download the latest artifact from the [releases page](https://github.com/GIGAMOLE/ComposeShadowsPlus/releases).

## Guide

`ComposeShadowsPlus` provides two custom shadow `Modifiers`: [RSBlurShadow](#RSBlurShadow) and [SoftLayerShadow](#SoftLayerShadow)

`ComposeShadowsPlus` common params:

| Param | Description |
|-|-|
|radius|The shadow radius.|
|color|The shadow color.|
|shape|The shadow shape.|
|spread|The shadow positive or negative spread.|
|offset|The shadow offset.|

`ShadowsPlusDefaults` contains default values of custom shadows.

You can use `Modifier.shadowsPlus(...)` to set the custom shadows with `ShadowsPlusType` enum.

### RSBlurShadow

`RSBlurShadow` uses `RenderScript` and `ScriptIntrinsicBlur` to blur the shadow content to simulate the shadow.

`RSBlurShadow` more params:

| Param | Description |
|-|-|
|alignRadius|The exponential align radius indicator.|

To apply `RSBlurShadow` use `Modifier.rsBlurShadow(...)`.

### SoftLayerShadow

`SoftLayerShadow` uses `NativePaint.setShadowLayer(...)` to apply native shadow layer to the shadow content.

To render `SoftLayerShadow`, Android devices with API < 28(P), need to use `View.LAYER_TYPE_SOFTWARE`. It's recommended to use `SoftLayerShadowContainer` to handle this
automatically.

To apply `SoftLayerShadow` use `Modifier.softLayerShadow(...)`.

## License

MIT License. See the [LICENSE](https://github.com/GIGAMOLE/ComposeShadowsPlus/blob/master/LICENSE) file for more details.

## Credits

Special thanks to the [GoDaddy](https://github.com/godaddy) for the amazing [color picker library](https://github.com/godaddy/compose-color-picker).

Created at [intive](https://intive.com).  
**We spark digital excitement.**

![](/media/credits.png)

## Author:

[Basil Miller](https://www.linkedin.com/in/gigamole/)  
[gigamole53@gmail.com](mailto:gigamole53@gmail.com)

![](/media/footer.png)