package de.visualdigits.kotlin.bannermatic.model.font

import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixText
import de.visualdigits.kotlin.extensions.toPixelMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class PixelMatrixTextFontTest {

    @Test
    fun testFont() {
        val font = FigletFont(FontName.Brite)
        assertEquals(11, font.height)
        assertEquals(8, font.baseLine)
    }

    @Test
    fun testFont2() {
        val matrix = PixelMatrixText(
            text = "Hello World!"
        )
        println(matrix)
    }

    @Test
    fun testTextMatrix() {
        FontName.entries.forEach { fontName ->
            try {
                val matrix = "Hello World! 0:123456789".toPixelMatrix(textWidth = 9999, fontName = fontName)
//                println(matrix)
//                println("-------------------------------")
            } catch (e: Exception) {
                val systemResource = ClassLoader.getSystemResource("figletfonts/${fontName.fontResource}")
                if (systemResource != null) {
                    println("Deleting bogus font: ${fontName.fontResource}")
                    File(systemResource.toURI()).delete()
                }
            }
        }
    }
}
