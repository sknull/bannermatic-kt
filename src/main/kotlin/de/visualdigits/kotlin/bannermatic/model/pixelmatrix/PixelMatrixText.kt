package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.FigletFont
import de.visualdigits.kotlin.bannermatic.model.font.FigletSmusher
import de.visualdigits.kotlin.bannermatic.model.font.Justify

class PixelMatrixText(
    val textWidth: Int = 80,
    ensureWidth: Boolean = false,
    initialChar: AnsiColorChar = AnsiColorChar(),
    val fontName: String = "basic",
    val direction: Direction = Direction.auto,
    val justify: Justify = Justify.auto,
    val text: String
): PixelMatrix<PixelMatrixText>(
    initialChar = initialChar
) {

    private var iterator = 0
    private var maxSmush = 0
    private var curCharWidth = 0
    private var prevCharWidth = 0
    private var currentTotalWidth = 0

    init {
        val fname = if (!fontName.endsWith(".flf") && !fontName.endsWith(".flc")) "$fontName.flf" else fontName
        val font = FigletFont("fonts/$fname")
        val buffer = Array(font.height) { "" }
        val chars: List<Int> = text.map { it.code }
        val blankMarkers = mutableListOf<Pair<Array<String>, Int>>()
        val queue = mutableListOf<Array<String>>()
        val smusher = FigletSmusher(direction, font)

        while (iterator < text.length) {
            addChar(buffer, chars, queue, blankMarkers, font, smusher, textWidth)
            iterator += 1
        }
        if (buffer[0].isNotEmpty()) {
            queue.add(buffer)
        }
        var charMatrix = queue.map { buff ->
            justifyString(justify, buff)
                .map { it.replace(font.hardBlank, " ") }
        }
        val rows = charMatrix.size
        val rowHeight = charMatrix.maxOf { it.size }
        height = rows * rowHeight
        charMatrix = trimLeft(charMatrix)
        width = charMatrix.maxOf { line -> line.maxOf { row -> row.length } }
        initializeMatrix()
        charMatrix.forEachIndexed { l, line ->
            line.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    setChar(x, l * rowHeight + y, char.toString())
                }
            }
        }
        ensureWidth(ensureWidth, width, textWidth)
    }

    private fun ensureWidth(
        ensureWidth: Boolean,
        width: Int,
        textWidth: Int,
    ) {
        if (ensureWidth && width < textWidth) {
            when (justify) {
                Justify.auto, Justify.left -> {
                    extend(0, 0, textWidth - width, 0)
                }

                Justify.right -> {
                    extend(textWidth - width, 0, 0, 0)
                }

                Justify.center -> {
                    val diff = textWidth - width
                    val left = diff / 2
                    val right = diff - left
                    extend(left, 0, right, 0)
                }
            }
        }
    }

    private fun trimLeft(
        charMatrix: List<List<String>>
    ): List<List<String>> {
        val spaces = charMatrix.flatMap { line -> line.map { row -> "^ +".toRegex().find(row)?.value?.length ?: 0 } }.min()
        return charMatrix.map { line -> line.map { it.drop(spaces) } }
    }

    private fun addChar(
        buffer: Array<String>,
        chars: List<Int>,
        queue: MutableList<Array<String>>,
        blankMarkers: MutableList<Pair<Array<String>, Int>>,
        font: FigletFont,
        smusher: FigletSmusher,
        textWidth: Int
    ) {
        val tuple = Pair(buffer.clone(), iterator)
        val code = chars[iterator]
        if (code == '\n'.code) {
            blankMarkers.add(tuple)
            handleNewline(buffer, chars, queue, blankMarkers, font, smusher)
        } else {
            val curChar = font.chars[code]
            if (curChar != null) {
                curCharWidth = font.width[code] ?:0
                if (textWidth < curCharWidth) {
                    throw IllegalStateException("No space left to print char")
                }
                maxSmush = if (curChar.isNotEmpty()) {
                    smusher.currentSmushAmount(buffer, curChar, curCharWidth, prevCharWidth)
                } else {
                    0
                }
                currentTotalWidth = buffer[0].length + curCharWidth - maxSmush
                if (code == ' '.code) {
                    blankMarkers.add(tuple)
                }
                if (currentTotalWidth >= textWidth) {
                    handleNewline(buffer, chars, queue, blankMarkers, font, smusher)
                } else {
                    for (row in 0 until font.height) {
                        val (addLeft, addRight) = smusher.smushRow(buffer[row], curChar, row, maxSmush, curCharWidth, prevCharWidth)
                        buffer[row] = addLeft + addRight.substring(maxSmush)
                    }
                }
                prevCharWidth = curCharWidth
            }
        }
    }

    private fun handleNewline(
        buffer: Array<String>,
        chars: List<Int>,
        queue: MutableList<Array<String>>,
        blankMarkers: MutableList<Pair<Array<String>, Int>>,
        font: FigletFont,
        smusher: FigletSmusher
    ) {
        if (blankMarkers.isNotEmpty()) {
            val (savedBuffer, savedIterator) = blankMarkers.first()
            blankMarkers.removeAt(0)
            queue.add(savedBuffer)
            iterator = savedIterator
        } else {
            queue.add(buffer)
            iterator -= 1
        }

        currentTotalWidth = buffer[0].length
        buffer.fill("", 0, font.height)
        blankMarkers.clear()
        prevCharWidth = 0
        val curChar = font.chars[chars[iterator]]
        if (curChar?.isNotEmpty() == true) {
            maxSmush = smusher.currentSmushAmount(buffer, curChar, curCharWidth, prevCharWidth)
        }
    }

    private fun justifyString(justify: Justify, buffer: Array<String>): Array<String> {
        return when (justify) {
            Justify.right -> buffer.map { row -> " ".repeat((textWidth - row.length)) + row }.toTypedArray()
            Justify.center -> buffer.map { row -> " ".repeat((textWidth - row.length) / 2) + row }.toTypedArray()
            else -> buffer
        }
    }
}
