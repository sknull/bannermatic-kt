package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorString
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.FontName
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class PixelMatrixBanner(
    targetWidth: Int? = null,
    targetHeight: Int? = null,
    image: BufferedImage,
    initialCharImage: AnsiColorString = AnsiColorString(),
    useSubPixels: Boolean = true,
    pixelRatio: Double = PixelMatrixImage.pixelRatio,

    text: String,
    textWidth: Int = 80,
    initialCharText: AnsiColorString = AnsiColorString(),
    fontName: FontName = FontName.Basic,
    justify: Justify = Justify.auto,
    direction: Direction = Direction.auto,

    textGap: Int = 0,
    textPosition: TextPosition = TextPosition.right
): PixelMatrix<PixelMatrixBanner>() {

    constructor(
        targetWidth: Int? = 80,
        targetHeight: Int? = null,
        imageFile: File,
        initialCharImage: AnsiColorString = AnsiColorString(),
        useSubPixels: Boolean = true,
        pixelRatio: Double = PixelMatrixImage.pixelRatio,

        text: String,
        textWidth: Int = 80,
        initialCharText: AnsiColorString = AnsiColorString(),
        fontName: FontName = FontName.Basic,
        justify: Justify = Justify.auto,
        direction: Direction = Direction.auto,

        textGap: Int = 0,
        textPosition: TextPosition = TextPosition.right
    ): this(
        targetWidth = targetWidth,
        targetHeight = targetHeight,
        image = ImageIO.read(imageFile),
        initialCharImage = initialCharImage,
        useSubPixels = useSubPixels,
        pixelRatio = pixelRatio,
    
        text = text,
        textWidth = textWidth,
        initialCharText = initialCharText,
        fontName = fontName,
        justify = justify,
        direction = direction,
    
        textGap = textGap,
        textPosition = textPosition
    )
    
    init {
        val imageMatrix = PixelMatrixImage(
            targetWidth = targetWidth,
            targetHeight = targetHeight,
            image = image,
            initialChar = initialCharImage,
            useSubPixels = useSubPixels,
            pixelRatio = pixelRatio
        )
        val textMatrix = PixelMatrixText(
            text = text,
            fontName = fontName,
            textWidth = textWidth,
            justify = justify,
            direction = direction,
            initialChar = initialCharText,
        )

        val left: Int
        val top: Int
        when (textPosition) {
            TextPosition.right -> {
                left = imageMatrix.width + textGap
                top = (imageMatrix.height - textMatrix.height) / 2
                width = imageMatrix.width + textGap + textMatrix.width
                height = Math.max(imageMatrix.height, textMatrix.height)
            }
            TextPosition.left -> {
                left = textMatrix.width + textGap
                top = (imageMatrix.height - textMatrix.height) / 2
                width = imageMatrix.width + textGap + textMatrix.width
                height = Math.max(imageMatrix.height, textMatrix.height)
            }
            TextPosition.top -> {
                left = (imageMatrix.width - textMatrix.width) / 2
                top = textMatrix.height + textGap
                width = Math.max(imageMatrix.width, textMatrix.width)
                height = imageMatrix.height + textGap + textMatrix.height
            }
            TextPosition.bottom -> {
                left = (imageMatrix.width - textMatrix.width) / 2
                top = imageMatrix.height + textGap
                width = Math.max(imageMatrix.width, textMatrix.width)
                height = imageMatrix.height + textGap + textMatrix.height
            }
        }

        initializeMatrix()

        when (textPosition) {
            TextPosition.right -> {
                if (top >= 0) {
                    paint(imageMatrix, left = 0, top = 0)
                    paint(textMatrix, left = left, top = top)
                } else {
                    paint(imageMatrix, left = 0, top = -1 * top)
                    paint(textMatrix, left = left, top = 0)
                }
            }
            TextPosition.left -> {
                if (top >= 0) {
                    paint(imageMatrix, left = left, top = 0)
                    paint(textMatrix, left = 0, top = top)
                } else {
                    paint(imageMatrix, left = left, top = -1 * top)
                    paint(textMatrix, left = 0, top = 0)
                }
            }
            TextPosition.top -> {
                if (left >= 0) {
                    paint(imageMatrix, left = 0, top = top)
                    paint(textMatrix, left = left, top = 0)
                } else {
                    paint(imageMatrix, left = -1 * left, top = top)
                    paint(textMatrix, left = 0, top = 0)
                }
            }
            TextPosition.bottom -> {
                if (left >= 0) {
                    paint(imageMatrix, left = 0, top = 0)
                    paint(textMatrix, left = left, top = top)
                } else {
                    paint(imageMatrix, left = -1 * left, top = 0)
                    paint(textMatrix, left = 0, top = top)
                }
            }
        }
    }
}
