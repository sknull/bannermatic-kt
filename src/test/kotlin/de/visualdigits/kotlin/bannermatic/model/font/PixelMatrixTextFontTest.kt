package de.visualdigits.kotlin.bannermatic.model.font

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PixelMatrixTextFontTest {

    @Test
    fun testFont() {
        val font = FigletFont("fonts/brite.flf")
        assertEquals(11, font.height)
        assertEquals(8, font.baseLine)
    }
}
