package de.visualdigits.kotlin.bannermatic.main

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColor4bit
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixBanner
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.TextPosition
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

@ExperimentalCli
class BannerCommand: Subcommand("banner", "Generate pixel matrix combined from an image and a text.") {

    private val widthImage by option(
        type = ArgType.Int,
        shortName = "wi",
        description = "Width of the image (default is 80 pixels)."
    ).default(80)
    private val initialBgColorImage by option(
        type = ArgType.String,
        shortName = "bgi",
        description = "The initial background color of the image (default is transparent)."
    )
    private val initialFgColorImage by option(
        type = ArgType.String,
        shortName = "fgi",
        description = "The initial foreground color of the image (default is the default terminal foreground color)."
    )
    private val useSubPixels by option(
        type = ArgType.Boolean,
        shortName = "sp",
        description = "Determines whether to use subpixel (default) or simple background color."
    ).default(true)
    private val imageFile by option(
        type = ArgType.String,
        shortName = "i",
        description = "The image file to render."
    ).required()
    private val pixelRatio by option(
        type = ArgType.Double,
        shortName = "pr",
        description = "The pixelratio to use (default to 0.4) maybe needs to be adjusted to look correct on your terminal."
    ).default(0.4)

    private val textWidth by option(
        type = ArgType.Int,
        shortName = "wt",
        description = "Width of the image (default is 80 pixels)."
    ).default(80)
    private val initialBgColorText by option(
        type = ArgType.String,
        shortName = "bgt",
        description = "The initial background color of the image (default is transparent)."
    )
    private val initialFgColorText by option(
        type = ArgType.String,
        shortName = "fgt",
        description = "The initial foreground color of the image (default is the default terminal foreground color)."
    )
    private val fontName by option(
        type = ArgType.String,
        shortName = "f",
        description = "The figlet font to use."
    ).default("basic")
    private val direction by option(
        type = ArgType.Choice<Direction>(),
        shortName = "d",
        description = "The text direction (defaults to auto = left to right)."
    ).default(Direction.auto)
    private val justify by option(
        type = ArgType.Choice<Justify>(),
        shortName = "j",
        description = "The text justification (defaults to auto = left)."
    ).default(Justify.auto)
    private val text by option(
        type = ArgType.String,
        shortName = "t",
        description = "The text to render - will break up into multiple lines when the width exceeds the max width given."
    ).required()

    private val textGap by option(
        type = ArgType.Int,
        shortName = "tg",
        description = "The gap between text and image (defaults to 0)."
    ).default(0)
    private val textPosition by option(
        type = ArgType.Choice<TextPosition>(),
        shortName = "tp",
        description = "The position of the text relative to the image (defaults to right)."
    ).default(TextPosition.right)

    private val outputFile by option(
        type = ArgType.String,
        shortName = "o",
        description = "The output file (if omitted the rendered matrix will be printed to the console)."
    )

    override fun execute() {
        val initialCharImage = AnsiColorChar(
            bgColor = initialBgColorImage?.let { AnsiColorRgb(it) } ?: AnsiColor4bit(),
            fgColor = initialFgColorImage?.let { AnsiColorRgb(it) }
        )

        val initialCharText = AnsiColorChar(
            bgColor = initialBgColorText?.let { AnsiColorRgb(it) } ?: AnsiColor4bit(),
            fgColor = initialFgColorText?.let { AnsiColorRgb(it) }
        )

        val bannerMatrix = PixelMatrixBanner(
            imageFile = File(imageFile),
            imageWidth = widthImage,
            initialCharImage = initialCharImage,
            pixelRatio = pixelRatio,
            useSubPixels = useSubPixels,

            text = text,
            textWidth = textWidth,
            fontName = fontName,
            direction = direction,
            justify = justify,
            initialCharText = initialCharText,

            textGap = textGap,
            textPosition = textPosition
        )

        if (outputFile != null) {
            bannerMatrix.writeToFile(File(outputFile!!))
        } else {
            println(bannerMatrix)
        }
    }
}
