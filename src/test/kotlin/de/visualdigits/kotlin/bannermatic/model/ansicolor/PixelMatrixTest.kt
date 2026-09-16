package de.visualdigits.kotlin.bannermatic.model.ansicolor

import de.visualdigits.kotlin.bannermatic.model.font.FontName
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.*
import de.visualdigits.kotlin.extensions.toPixelMatrix
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO

@Disabled("Only for manual testing")
class PixelMatrixTest {

    @Test
    fun testImage() {
        val matrix1 = File(ClassLoader.getSystemResource("images/Graffitomat_ascii-banner.png").toURI()).toPixelMatrix(
            targetWidth = 180,
        )
        println(matrix1)

        val matrix2 = ImageIO.read(
            File(ClassLoader.getSystemResource("images/raspberry/raspberrypi.png").toURI())
        ).toPixelMatrix(
            targetWidth = 80,
        )
        println(matrix2)

//        File("E:\\Programmierung\\IntelliJ\\graffitomat\\backendServer\\src\\main\\resources\\banner.txt")
//            .writeText(pm.toString())
    }

    @Test
    fun testText() {
        val matrix1 = "Team   GRU".toPixelMatrix(
            fontName = FontName.Usaflag,
            textWidth = 180,
            justify = Justify.center,
            initialChar = AnsiColorString(
                fgColor = AnsiColorRgb(r = 255, g = 255, b = 255),
                bgColor = AnsiColorRgb(r = 0, g = 0, b = 128)
            )
        )
        println(matrix1)
    }

    @Test
    fun testBanner() {
//        val imageFile = File(ClassLoader.getSystemResource("images/raspberry/raspberrypi.png").toURI())
        val imageFile = File(ClassLoader.getSystemResource("images/kodi.png").toURI())
//        val imageFile = File(ClassLoader.getSystemResource("images/subpixel/tree2.jpg").toURI())

        val pm = PixelMatrixBanner(
            imageFile = imageFile,
            targetWidth = 60,
//            targetWidth = 27,
//            targetWidth = 76,
//            initialCharImage = AnsiColorString(
//                bgColor = AnsiColorRgb(r = 255, g = 255, b = 255),
//                fgColor = AnsiColorRgb(r = 187, g = 16, b = 66)
//            ),

//            text = "Raspberry Pi",
            text = "KodiBerry",
            textWidth = 80,
            justify = Justify.center,
            initialCharText = AnsiColorString(
                fgColor = AnsiColorRgb(r = 38, g = 140, b = 180)
            ),
            textGap = 0,
            textPosition = TextPosition.bottom
        )
//        .extend(2, 1, 2, 1)
//        .extend(2, 1, 2, 1, AnsiColorString(bgColor = AnsiColorRgb(r = 117, g = 169, b = 39)))
//        .extend(2, 1, 2, 1, AnsiColorString(bgColor = AnsiColorRgb(r = 187, g = 16, b = 66)))
        pm.writeToFile(File("./src/test/resources/banners/banner_kodiberry.txt"))
        println(pm)
    }
}
