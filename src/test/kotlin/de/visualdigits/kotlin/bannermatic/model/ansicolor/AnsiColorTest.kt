package de.visualdigits.kotlin.bannermatic.model.ansicolor

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled("Only for manual testing")
class AnsiColorTest {

    @Test
    fun testHello() {
        val c = AnsiColor4bit(bgColor = AnsiCode.RED_BG, fgColor = AnsiCode.YELLOW_FG)
        println("${c} hello world! ${AnsiColor4bit.RESET}")
    }
}