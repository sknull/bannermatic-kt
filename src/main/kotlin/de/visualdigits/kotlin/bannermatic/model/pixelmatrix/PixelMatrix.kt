package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiCode
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColor
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColor4bit
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import java.io.File

@Suppress("UNCHECKED_CAST")
open class PixelMatrix<T : PixelMatrix<T>>(
    var width: Int = 80,
    var height: Int = 80,
    val initialChar: AnsiColorChar = AnsiColorChar(),
    initializeMatrix: Boolean = false
) {

    private val matrix: MutableList<MutableList<AnsiColorChar>> = mutableListOf()

    init {
        if (initializeMatrix) {
            initializeMatrix()
        }
    }

    protected fun initializeMatrix() {
        for (y in 0 until height) {
            val row: MutableList<AnsiColorChar> = mutableListOf()
            for (x in 0 until width) {
                row.add(initialChar.clone())
            }
            matrix.add(row)
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (row in matrix) {
            for (pixel in row) {
                sb.append(pixel)
            }
            sb.append("${AnsiColor4bit.RESET}\n")
        }
        return sb.toString()
    }

    fun writeToFile(file: File) {
        file.writeText(toString())
    }

    fun set(x: Int, y: Int, char: AnsiColorChar): T {
        matrix[y][x] = char
        return this as T
    }

    fun setChar(x: Int, y: Int, char: String): T {
        matrix[y][x].char = char
        return this as T
    }

    fun setBgColor(x: Int, y: Int, color: AnsiColor<*>): T {
        if (color is AnsiColorRgb) color.bgColor = AnsiCode.BACKGROUND
        matrix[y][x].bgColor = color
        return this as T
    }

    fun setFgColor(x: Int, y: Int, color: AnsiColor<*>): T {
        if (color is AnsiColorRgb) color.bgColor = AnsiCode.FOREGROUND
        matrix[y][x].fgColor = color
        return this as T
    }

    fun get(x: Int, y: Int): AnsiColorChar {
        return matrix[y][x]
    }

    fun extend(left: Int, top: Int, right: Int, bottom: Int, initialChar: AnsiColorChar = this.initialChar): T {
        if (left > 0) {
            val leftList = List(left) { initialChar.clone() }
            matrix.forEach { row ->
                row.addAll(0, leftList)
            }
        }

        if (right > 0) {
            val rightList = List(right) { initialChar.clone() }
            matrix.forEach { row ->
                row.addAll(rightList)
            }
        }

        width += left + right

        if (top > 0) {
            val topList = List(top) { MutableList(width) { initialChar.clone() } }
            matrix.addAll(0, topList)
        }
        if (bottom > 0) {
            val bottomList = List(bottom) { MutableList(width) { initialChar.clone() } }
            matrix.addAll(bottomList)
        }
        height += top + bottom
        return this as T
    }

    fun clip(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): T {
        if (left > 0) {
            matrix.forEach { row -> for (i in 0 until left) { row.removeFirst() } }
        }

        if (right > 0) {
            matrix.forEach { row -> for (i in 0 until right) { row.removeLast() } }
        }

        if (top > 0) {
            for (i in 0 until top) { matrix.removeFirst() }
        }

        if (bottom > 0) {
            for (i in 0 until bottom) { matrix.removeLast() }
        }

        return this as T
    }

    fun paint(other: PixelMatrix<*>, left: Int = 0, top: Int = 0): T {
        var maxX = other.width
        if (other.width + left > width) {
            maxX -= other.width + left - width
        }
        val maxY = other.height
        if (other.height + top > height) {
            maxX -= other.height + left - height
        }
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                set(left + x, top + y, other.get(x, y))
            }
        }
        return this as T
    }
}
