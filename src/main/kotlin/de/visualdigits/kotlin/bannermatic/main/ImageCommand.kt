package de.visualdigits.kotlin.bannermatic.main

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColor4bit
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixImage
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

@ExperimentalCli
class ImageCommand: Subcommand("image", "Generate pixel matrix from an image.") {

    val width by option(
        type = ArgType.Int,
        shortName = "w",
        description = "Width of the image (default is 80 pixels)."
    ).default(80)
    val initialBgColor by option(
        type = ArgType.String,
        shortName = "bg",
        description = "The initial background color of the image (default is transparent)."
    )
    val initialFgColor by option(
        type = ArgType.String,
        shortName = "fg",
        description = "The initial foreground color of the image (default is the default terminal foreground color)."
    )
    val useSubPixels by option(
        type = ArgType.Boolean,
        shortName = "sp",
        description = "Determines whether to use subpixel (default) or simple background color."
    ).default(true)
    val imageFile by option(
        type = ArgType.String,
        shortName = "i",
        description = "The image file to render."
    ).required()
    val pixelRatio by option(
        type = ArgType.Double,
        shortName = "pr",
        description = "The pixelratio to use (default to 0.4) maybe needs to be adjusted to look correct on your terminal."
    ).default(0.4)

    val outputFile by option(
        type = ArgType.String,
        shortName = "o",
        description = "The output file (if omitted the rendered matrix will be printed to the console)."
    )

    override fun execute() {
        val initialChar = AnsiColorChar(
            bgColor = initialBgColor?.let { AnsiColorRgb(it) } ?: AnsiColor4bit(),
            fgColor = initialFgColor?.let { AnsiColorRgb(it) }
        )

        val imageMatrix = PixelMatrixImage(
            imageFile = File(imageFile),
            width = width,
            initialChar = initialChar,
            useSubPixels = useSubPixels,
            pixelRatio = pixelRatio
        )

        if (outputFile != null) {
            imageMatrix.writeToFile(File(outputFile!!))
        } else {
            println(imageMatrix)
        }
    }
}