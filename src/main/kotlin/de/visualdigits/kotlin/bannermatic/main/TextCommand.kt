package de.visualdigits.kotlin.bannermatic.main

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColor4bit
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.Justify
import de.visualdigits.kotlin.bannermatic.model.pixelmatrix.PixelMatrixText
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

@ExperimentalCli
class TextCommand: Subcommand("text", "Generate pixel matrix from a text.") {

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
    val fontName by option(
        type = ArgType.String,
        shortName = "f",
        description = "The figlet font to use."
    ).default("basic")
    val direction by option(
        type = ArgType.Choice<Direction>(),
        shortName = "d",
        description = "The text direction (defaults to auto = left to right)."
    ).default(Direction.auto)
    val justify by option(
        type = ArgType.Choice<Justify>(),
        shortName = "j",
        description = "The text justification (defaults to auto = left)."
    ).default(Justify.auto)
    val text by option(
        type = ArgType.String,
        shortName = "t",
        description = "The text to render - will break up into multiple lines when the width exceeds the max width given."
    ).required()

    val marginLeft by option(
        type = ArgType.Int,
        shortName = "ml",
        description = "The margin to add on the left side (defaults to 0)."
    ).default(0)
    val marginTop by option(
        type = ArgType.Int,
        shortName = "mt",
        description = "The margin to add on the top (defaults to 0)."
    ).default(0)
    val marginRight by option(
        type = ArgType.Int,
        shortName = "mr",
        description = "The margin to add on the right side (defaults to 0)."
    ).default(0)
    val marginBottom by option(
        type = ArgType.Int,
        shortName = "mb",
        description = "The margin to add on the bottom (defaults to 0)."
    ).default(0)

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

        val textMatrix = PixelMatrixText(
            text = text,
            fontName = fontName,
            textWidth = width,
            justify = justify,
            initialChar = initialChar,
        ).extend(
            left = marginLeft,
            top = marginTop,
            right = marginRight,
            bottom = marginBottom
        )

        if (outputFile != null) {
            textMatrix.writeToFile(File(outputFile!!))
        } else {
            println(textMatrix)
        }
    }
}