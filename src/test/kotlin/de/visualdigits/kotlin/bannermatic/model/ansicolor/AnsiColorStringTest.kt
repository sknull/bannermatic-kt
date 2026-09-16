package de.visualdigits.kotlin.bannermatic.model.ansicolor

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.awt.Color

@Disabled("Only for manual testing")
class AnsiColorStringTest {

    @Test
    fun testHello() {
        val c1 = AnsiColorString(bgColor = AnsiColorRgb(r = 255, g = 0, b = 0), fgColor = AnsiColorRgb(
            r = 255,
            g = 255,
            b = 0
        ), value = "#")
        val c2 = AnsiColorString(bgColor = AnsiColorRgb(r = 0, g = 0, b = 255), fgColor = AnsiColorRgb(
            r = 0,
            g = 255,
            b = 0
        ), value = "#")
        println("$c1$c2${AnsiColor4bit.RESET}")
    }

    @Test
    fun withColors() {
        for (hue in 0..360 step 10) {
            val c = AnsiColorString(
                bgColor = AnsiColorRgb(Color(Color.HSBtoRGB(hue / 360.0f, 1.0f, 1.0f)))
            )
            print("${c}")
        }
        print("${AnsiColor4bit.RESET}\n")
    }
}
