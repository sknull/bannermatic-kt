package de.visualdigits.kotlin.extensions

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorString
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.FontName
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixImage
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixImage.Companion
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixText
import java.awt.image.BufferedImage
import java.io.File


fun BufferedImage.toPixelMatrix(
    targetWidth: Int? = null,
    targetHeight: Int? = null,
    initialChar: AnsiColorString = AnsiColorString(),
    useSubPixels: Boolean = true,
    pixelRatio: Double = Companion.pixelRatio
): String {
    val matrix = PixelMatrixImage(
        targetWidth = targetWidth,
        targetHeight = targetHeight,
        initialChar = initialChar,
        image = this,
        useSubPixels = useSubPixels,
        pixelRatio = pixelRatio
    )

    return matrix.toString()
}

fun File.toPixelMatrix(
    targetWidth: Int? = null,
    targetHeight: Int? = null,
    initialChar: AnsiColorString = AnsiColorString(),
    useSubPixels: Boolean = true,
    pixelRatio: Double = Companion.pixelRatio
): String {
    val matrix = PixelMatrixImage(
        targetWidth = targetWidth,
        targetHeight = targetHeight,
        initialChar = initialChar,
        imageFile = this,
        useSubPixels = useSubPixels,
        pixelRatio = pixelRatio
    )

    return matrix.toString()
}

fun String.toPixelMatrix(
    textWidth: Int = 80,
    ensureWidth: Boolean = false,
    initialChar: AnsiColorString = AnsiColorString(),
    fontName: FontName = FontName.Basic,
    direction: Direction = Direction.auto,
    justify: Justify = Justify.auto,
): String {
    val matrix = PixelMatrixText(
        text = this,
        textWidth = textWidth,
        ensureWidth = ensureWidth,
        initialChar = initialChar,
        fontName = fontName,
        direction = direction,
        justify = justify
    )

    return matrix.toString()
}
