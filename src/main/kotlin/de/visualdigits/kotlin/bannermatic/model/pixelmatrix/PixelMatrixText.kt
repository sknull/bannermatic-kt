package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.font.Direction
import de.visualdigits.kotlin.bannermatic.model.font.FigletFont
import de.visualdigits.kotlin.bannermatic.model.font.FigletSmusher
import de.visualdigits.kotlin.bannermatic.model.font.Justify

class PixelMatrixText(
    val textWidth: Int = 80,
    val ensureWidth: Boolean = false,
    initialChar: AnsiColorChar = AnsiColorChar(),
    val fontName: String = "basic",
    val direction: Direction = Direction.auto,
    val justify: Justify = Justify.auto,
    val text: String
): PixelMatrix<PixelMatrixText>(
    initialChar = initialChar
) {

    private var chars: List<Int> = text.map { it.code }
            
    private var font = FigletFont("fonts/$fontName.flf")

    private var iterator = 0
    private var maxSmush = 0

    private var curCharWidth = 0
    private var prevCharWidth = 0
    private var currentTotalWidth = 0

    private var blankMarkers = mutableListOf<Pair<Array<String>, Int>>()
    private val queue = mutableListOf<Array<String>>()
    private var buffer = Array(font.height) { "" }

    private var smusher = FigletSmusher(direction, this.font)

    init {
        while (iterator < text.length) {
            addChar()
            iterator += 1
        }
        if (buffer[0].isNotEmpty()) {
            queue.add(buffer)
        }
        var charMatrix = queue.map { buffer ->
            justifyString(justify, buffer)
                .map { it.replace(font.hardBlank, " ") }
        }
        val rows = charMatrix.size
        val rowHeight = charMatrix.maxOf { it.size }
        height = rows * rowHeight
        charMatrix = trimLeft(charMatrix)
        width = charMatrix.maxOf { line -> line.maxOf { row -> row.length } }
        initializeMatrix()
        if (ensureWidth) {

        }
        charMatrix.forEachIndexed { l, line ->
            line.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    setChar(x, l * rowHeight + y, char.toString())
                }
            }
        }
        ensureWidth()
    }

    private fun ensureWidth() {
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

    private fun trimLeft(charMatrix: List<List<String>>): List<List<String>> {
        val spaces = charMatrix.flatMap { line -> line.map { row -> "^ +".toRegex().find(row)?.value?.length ?: 0 } }.min()
        return charMatrix.map { line -> line.map { it.drop(spaces) } }
    }

    private fun addChar() {
        val tuple = Pair(buffer.clone(), iterator)
        val c = chars[iterator]
        if (c == '\n'.code) {
            blankMarkers.add(tuple)
            handleNewline()
        } else {
            val curChar = font.chars[chars[iterator]]
            if (curChar != null) {
                curCharWidth = font.width[chars[iterator]] ?:0
                if (textWidth < curCharWidth) {
                    throw IllegalStateException("No space left to print char")
                }
                maxSmush = if (curChar.isNotEmpty()) {
                    smusher.currentSmushAmount(buffer, curChar, curCharWidth, prevCharWidth)
                } else {
                    0
                }
                currentTotalWidth = buffer[0].length + curCharWidth - maxSmush
                if (c == ' '.code) {
                    blankMarkers.add(tuple)
                }
                if (currentTotalWidth >= textWidth) {
                    handleNewline()
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

    private fun handleNewline() {
        if (blankMarkers.isNotEmpty()) {
            val (savedBuffer, savedIterator) = blankMarkers.first()
            blankMarkers = blankMarkers.drop(1).toMutableList()
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
            Justify.right -> buffer.map { row -> " ".repeat((width - row.length)) + row }.toTypedArray()
            Justify.center -> buffer.map { row -> " ".repeat((width - row.length) / 2) + row }.toTypedArray()
            else -> buffer
        }
    }
}
