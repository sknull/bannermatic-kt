package de.visualdigits.kotlin.bannermatic.model.ansicolor

import org.junit.jupiter.api.Test
import java.awt.Color

class AnsiColorRgbTest {

    @Test
    fun testRgbColor() {
        val sb = StringBuilder()
        sb.append(AnsiColor4bit.RESET)

        for(r in 0..255 step 10) {
            for(g in 0..255 step 10 ) {
                sb
                    .append(AnsiColorRgb(r = r, g = g, b = 0, bgColor = AnsiCode.BACKGROUND))
                    .append(AnsiColorRgb(r = 255 - r, g = 255 - g, b = 0))
                    .append("#")
            }
            sb.append("${AnsiColor4bit.RESET}\n")
        }
        sb.append("\n")

        for(r in 0..255 step 10) {
            for(b in 0..255 step 10 ) {
                sb.append(AnsiColorRgb(r = r, g = 0, b = b, bgColor = AnsiCode.BACKGROUND)).append(" ")
            }
            sb.append("${AnsiColor4bit.RESET}\n")
        }
        sb.append("\n")

        for(g in 0..255 step 10) {
            for(b in 0..255 step 10 ) {
                sb.append(AnsiColorRgb(r = 0, g = g, b = b, bgColor = AnsiCode.BACKGROUND)).append(" ")
            }
            sb.append("${AnsiColor4bit.RESET}\n")
        }
        sb.append("\n")

        for(g in 0..255 step 10) {
            sb.append(AnsiColorRgb(r = g, g = g, b = g, bgColor = AnsiCode.BACKGROUND)).append(" ")
        }
        sb.append("${AnsiColor4bit.RESET}\n")

        println(sb)
    }

    @Test
    fun testHello() {
        val bg = AnsiColorRgb(r = 255, g = 0, b = 0, bgColor = AnsiCode.BACKGROUND)
        val fg = AnsiColorRgb(r = 255, g = 255, b = 0)
        println("$bg$fg hello world! ${AnsiColor4bit.RESET}")
    }

    @Test
    fun withColors() {
        for (hue in 0..360 step 10) {
            val c = AnsiColorRgb(Color(Color.HSBtoRGB(hue / 360.0f, 1.0f, 1.0f)), bgColor = AnsiCode.BACKGROUND)
            print("${c} ")
        }
        print("${AnsiColor4bit.RESET}\n")
    }
}