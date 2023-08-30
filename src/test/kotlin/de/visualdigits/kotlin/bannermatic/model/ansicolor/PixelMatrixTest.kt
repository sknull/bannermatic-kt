package de.visualdigits.kotlin.bannermatic.model.ansicolor

import de.visualdigits.kotlin.bannermatic.model.font.Justify
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled("Only for manual testing")
class PixelMatrixTest {

    @Test
    fun withPixels() {
        val width = 10
        val height = 10
        val pm = PixelMatrix(width, height, AnsiColorChar(bgColor = AnsiColorRgb(r = 255, g = 255, b = 255)), initializeMatrix = true)
        for (y in 0 until height) {
            for (x in 0 until width) {
                pm.setBgColor(x, y, AnsiColorRgb(
                    r = (x.toFloat() / width * 255.0).toInt(),
                    g = (y.toFloat() / height * 255.0).toInt(),
                    b = 0
                ))
            }
        }
        pm.extend(1,1,20,1)
        val red = PixelMatrix(10, 5, AnsiColorChar(bgColor = AnsiColorRgb(r = 0, g = 0, b = 0), fgColor = AnsiColorRgb(r = 255, g = 0, b = 0), char = "#"), initializeMatrix = true)
        pm.paint(red, 12, 3)
        println(pm)
    }

    @Test
    fun testImage() {
        val imageFile = File(ClassLoader.getSystemResource("images/raspberry/raspberrypi.png").toURI())
//        val imageFile = File(ClassLoader.getSystemResource("images/minion.png").toURI())
//        val imageFile = File(ClassLoader.getSystemResource("images/subpixel/tree2.jpg").toURI())

        val pm = PixelMatrixImage(
            imageFile = imageFile,
            width = 40,
            useSubPixels = true
        )
        println(pm)
    }

    @Test
    fun testText() {
        val pm = PixelMatrixText(
            text = "Team GRU",
            textWidth = 40,
            justify = Justify.center,
            initialChar = AnsiColorChar(
                fgColor = AnsiColorRgb(r = 187, g = 16, b = 66),
                bgColor = AnsiColorRgb(r = 0, g = 255, b = 0)
            )
        )
        println(pm)
    }

    @Test
    fun testBanner() {
//        val imageFile = File(ClassLoader.getSystemResource("images/raspberry/raspberrypi.png").toURI())
        val imageFile = File(ClassLoader.getSystemResource("images/minion.png").toURI())
//        val imageFile = File(ClassLoader.getSystemResource("images/subpixel/tree2.jpg").toURI())

        val pm = PixelMatrixBanner(

            imageFile = imageFile,
            imageWidth = 40,
//            imageWidth = 27,
//            imageWidth = 76,
//            initialCharImage = AnsiColorChar(
//                bgColor = AnsiColorRgb(r = 255, g = 255, b = 255),
//                fgColor = AnsiColorRgb(r = 187, g = 16, b = 66)
//            ),

//            text = "Raspberry Pi",
            text = "Team GRU",
            textWidth = 40,
            justify = Justify.center,
            initialCharText = AnsiColorChar(
                fgColor = AnsiColorRgb(r = 255, g = 230, b = 100)
            ),
            textGap = 2,
            textPosition = TextPosition.bottom
        )
        .extend(0, 2, 0, 0)
//        .extend(2, 1, 2, 1)
//        .extend(2, 1, 2, 1, AnsiColorChar(bgColor = AnsiColorRgb(r = 117, g = 169, b = 39)))
//        .extend(2, 1, 2, 1, AnsiColorChar(bgColor = AnsiColorRgb(r = 187, g = 16, b = 66)))
        pm.writeToFile(File("./src/test/resources/banners/banner_team-gru.txt"))
        println(pm)
    }
}
