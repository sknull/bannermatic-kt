package de.visualdigits.kotlin.bannermatic.model.ansicolor

import de.visualdigits.kotlin.extensions.color
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class AnsiColorRgb(
    override val name: String = "",
    override var bgColor: AnsiCode = AnsiCode.FOREGROUND,
    override val fgColor: AnsiCode = AnsiCode.DIM,
    val r: Int,
    val g: Int,
    val b: Int,
    val a: Int = 0,
): Color(r, g, b, a), AnsiColor<AnsiColorRgb> {

    override fun clone(): AnsiColorRgb {
        return AnsiColorRgb(
            name = name,
            bgColor = bgColor,
            fgColor = fgColor,
            r = r,
            g = g,
            b = b,
            a = a
        )
    }

    constructor(color: String, isBackground: Boolean = false): this(color.color(), if(isBackground) AnsiCode.BACKGROUND else AnsiCode.FOREGROUND)

    constructor(color: Color, bgColor: AnsiCode = AnsiCode.FOREGROUND): this(
        bgColor = bgColor,
        r = color.red,
        g = color.green,
        b = color.blue,
        a = color.alpha
    )

    companion object {

        val THRESHOLD_ALPHA = 4
    }

    override fun toString(): String {
        return "\u001B[${bgColor.value};${fgColor.value};$r;$g;${b}m"
    }

    override fun color(): Color = Color(r, g, b, a)

    /**
     * Converts the current RGB value to a grayscale value [0.0 - 1.0] using the luminosity method
     * as described here: see https://www.baeldung.com/cs/convert-rgb-to-grayscale
     *
     * @return Double
     */
    override fun toGray(): Double {
        return red / 255.0 * 0.3 + green / 255.0 * 0.59 + blue / 255.0 * 0.11
    }

    /**
     * Returns a color with the hue multiplied by the given factor.
     */
    fun hueFactor(factor: Double): AnsiColorRgb {
        val hsb = RGBtoHSB(r, g, b, null)
        return AnsiColorRgb(Color(HSBtoRGB(hsb[0].box(factor), hsb[1], hsb[2])))
    }

    /**
     * Returns a color with the saturation multiplied by the given factor.
     */
    fun saturationFactor(factor: Double): AnsiColorRgb {
        val hsb = RGBtoHSB(r, g, b, null)
        return AnsiColorRgb(Color(HSBtoRGB(hsb[0], hsb[1].box(factor), hsb[2])))
    }

    /**
     * Returns a color with the brightness multiplied by the given factor.
     */
    fun brightnessFactor(factor: Double): AnsiColorRgb {
        val hsb = RGBtoHSB(r, g, b, null)
        return AnsiColorRgb(Color(HSBtoRGB(hsb[0], hsb[1], hsb[2].box(factor))))
    }

    /**
     * Ensures that the multiplied value is in range 0.0 - 1.0.
     */
    private fun Float.box(factor: Double): Float = max(0.0, min(1.0, this * factor)).toFloat()

    /**
     * Returns the web color representation of this color.
     */
    fun webColor(): String = String.format("#%02x%02x%02x", r, g, b)
}
