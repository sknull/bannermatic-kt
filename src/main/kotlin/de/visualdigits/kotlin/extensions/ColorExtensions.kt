package de.visualdigits.kotlin.extensions

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import java.awt.Color
import kotlin.math.max
import kotlin.math.min


fun String.color(): Color = Color.decode(trim().replace("#", "0x"))

fun Color.fade(other: Color): Color {
    val a = alpha / 255.0
    val ia = 1.0 - alpha / 255.0
    return Color(
        (red * a + other.red * ia).toInt(),
        (green * a + other.green * ia).toInt(),
        (blue * a + other.blue * ia).toInt(),
        (Math.min(1.0, a + other.alpha / 255.0) * 255).toInt()
    )
}

fun Color.isTransparent(): Boolean = alpha < AnsiColorRgb.THRESHOLD_ALPHA

/**
 * Returns a color with the hue multiplied by the given factor.
 */
fun Color.hueFactor(factor: Double): Color {
    val hsb = Color.RGBtoHSB(red, green, blue, null)
    return Color(Color.HSBtoRGB(hsb[0].box(factor), hsb[1], hsb[2]))
}

/**
 * Returns a color with the saturation multiplied by the given factor.
 */
fun Color.saturationFactor(factor: Double): Color {
    val hsb = Color.RGBtoHSB(red, green, blue, null)
    return Color(Color.HSBtoRGB(hsb[0], hsb[1].box(factor), hsb[2]))
}

/**
 * Returns a color with the brightness multiplied by the given factor.
 */
fun Color.brightnessFactor(factor: Double): Color {
    val hsb = Color.RGBtoHSB(red, green, blue, null)
    return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2].box(factor)))
}

/**
 * Ensures that the multiplied value is in range 0.0 - 1.0.
 */
private fun Float.box(factor: Double): Float = max(0.0, min(1.0, this * factor)).toFloat()

/**
 * Returns the web color representation of this color.
 */
fun Color.webColor(): String = String.format("#%02x%02x%02x", red, green, blue)


fun Color.ansiColor(background: Boolean = false): String = "\u001B[${38 + if(background)10 else 0};2;$red;$green;${blue}m"
