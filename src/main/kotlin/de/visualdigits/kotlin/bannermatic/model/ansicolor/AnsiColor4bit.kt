package de.visualdigits.kotlin.bannermatic.model.ansicolor

import java.awt.Color

class AnsiColor4bit(
    override val name: String = "",
    override var bgColor: AnsiCode = AnsiCode.BACKGROUND_DEFAULT,
    override val fgColor: AnsiCode? = AnsiCode.FOREGROUND_DEFAULT
): AnsiColor<AnsiColor4bit> {

    override fun clone(): AnsiColor4bit {
        return AnsiColor4bit(
            name = name,
            bgColor = bgColor,
            fgColor = fgColor
        )
    }

    companion object {

       val RESET = AnsiColor4bit(name = "RESET")
     }
    
    override fun toString(): String {
        return if (fgColor != null) {
            "\u001B[${fgColor.value};${bgColor.value}m"
        } else {
            "\u001B[${bgColor.value}m"
        }
    }

    override fun color(): Color = Color(fgColor?.r?:0, fgColor?.g?:0, fgColor?.b?:0, 255)

    /**
     * Converts the current RGB value to a grayscale value [0.0 - 1.0] using the luminosity method
     * as described here: see https://www.baeldung.com/cs/convert-rgb-to-grayscale
     *
     * @return Double
     */
    override fun toGray(): Double {
        return bgColor.r / 255.0 * 0.3 + bgColor.g / 255.0 * 0.59 + bgColor.b / 255.0 * 0.11
    }
}
