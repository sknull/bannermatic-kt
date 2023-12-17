package de.visualdigits.kotlin.bannermatic.model.font

import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixText
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.Exception

class PixelMatrixTextFontTest {

    @Test
    fun testFont() {
        val font = FigletFont("fonts/brite.flf")
        assertEquals(11, font.height)
        assertEquals(8, font.baseLine)
    }

    @Test
    fun testFont2() {
        val matrix = PixelMatrixText(
            textWidth = 9999,
            text = "Hello World!"
        )
        println(matrix)
    }

    @Test
    fun testTextMatrix() {
        File(ClassLoader.getSystemResource("fonts").toURI())
            .listFiles { f -> f.isFile && f.name.lowercase().endsWith(".flf") }
            ?.forEach { f ->
                println("${f.name}:")
                try {
                    val matrix = PixelMatrixText(
                        textWidth = 9999,
                        fontName = f.name,
                        text = "Hello World! 0:123456789"
                    )
                    println(matrix)
                    println("-------------------------------")
                } catch (e: Exception) {
                    f.delete()
                }
            }
    }
}
