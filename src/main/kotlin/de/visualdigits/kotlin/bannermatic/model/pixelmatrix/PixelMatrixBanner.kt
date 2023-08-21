package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import java.io.File

class PixelMatrixBanner(
    imageFile: File,
    imageWidth: Int = 80,
    initialCharImage: AnsiColorChar = AnsiColorChar(),
    useSubPixels: Boolean = true,
    pixelRatio: Double = PixelMatrixImage.pixelRatio,

    text: String,
    textWidth: Int = 80,
    initialCharText: AnsiColorChar = AnsiColorChar(),
    fontName: String = "basic",
    justify: Justify = Justify.auto,

    initialCharBorder: AnsiColorChar = AnsiColorChar(),

    textGap: Int = 0
): PixelMatrix<PixelMatrixBanner>() {

    init {
        val imageMatrix = PixelMatrixImage(
            imageFile = imageFile,
            width = imageWidth,
            initialChar = initialCharImage,
            useSubPixels = useSubPixels,
            pixelRatio = pixelRatio
        )
        val textMatrix = PixelMatrixText(
            text = text,
            fontName = fontName,
            textWidth = textWidth,
            justify = justify,
            initialChar = initialCharText,
            marginLeft = textGap
        )
        val left = imageMatrix.width + 2
        val top = Math.max(0, (imageMatrix.height - textMatrix.height) / 2)

        width = imageMatrix.width + textGap + textMatrix.width
        height = Math.max(imageMatrix.height, textMatrix.height)
        initializeMatrix()
        if (top >= 0) {
            paint(imageMatrix, left = 0, top = 0)
            paint(textMatrix, left = left, top = top)
        } else {
            paint(imageMatrix, left = 0, top = top)
            paint(textMatrix, left = left, top = 0)
        }
    }
}
