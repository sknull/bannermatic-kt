package de.visualdigits.kotlin.bannermatic.model.pixelmatrix

import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorChar
import de.visualdigits.kotlin.bannermatic.model.ansicolor.AnsiColorRgb
import de.visualdigits.kotlin.extensions.fade
import de.visualdigits.kotlin.extensions.isTransparent
import de.visualdigits.kotlin.extensions.scale
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class PixelMatrixImage(
    width: Int,
    initialChar: AnsiColorChar = AnsiColorChar(),
    val imageFile: File,
    val useSubPixels: Boolean = true,
    val pixelRatio: Double = Companion.pixelRatio
): PixelMatrix<PixelMatrixImage>(
    width = width,
    initialChar = initialChar
) {

    companion object {

        val pixelRatio = 0.4

        // reverse engineered from here: https://underware.nl/case-studies/subpixel-ascii-plus-art/
        private val ASCII_ART_CHARS_SUBPIXELS = mapOf(
            "'" to listOf(0, 0, 0, 0, 0, 0, 0, 2, 0),
            "󲫤" to listOf(0, 0, 0, 0, 0, 0, 1, 1, 1),
            "“" to listOf(0, 0, 0, 0, 0, 0, 1, 1, 2),
            "\"" to listOf(0, 0, 0, 0, 0, 0, 1, 2, 1),
            "”" to listOf(0, 0, 0, 0, 0, 0, 1, 2, 2),
            "󷐮" to listOf(0, 0, 0, 0, 1, 0, 0, 0, 0),
            "󷐭" to listOf(0, 0, 0, 0, 1, 0, 0, 1, 0),
            "󲫡" to listOf(0, 0, 0, 0, 1, 0, 0, 2, 0),
            "󷀮" to listOf(0, 1, 0, 0, 0, 0, 0, 0, 0),
            "·" to listOf(0, 0, 0, 0, 2, 0, 0, 0, 0),
            "󲫨" to listOf(0, 0, 0, 0, 2, 0, 0, 1, 0),
            "󷑲" to listOf(0, 0, 0, 0, 2, 0, 0, 1, 1),
            "󷑬" to listOf(0, 0, 0, 0, 2, 0, 0, 2, 0),
            "󷑶" to listOf(0, 0, 0, 0, 2, 0, 1, 1, 1),
            "." to listOf(0, 1, 0, 0, 1, 0, 0, 0, 0),
            "󷒢" to listOf(0, 0, 0, 0, 2, 0, 1, 2, 0),
            "󷐹" to listOf(0, 0, 0, 0, 2, 0, 1, 2, 1),
            "󷑳" to listOf(0, 0, 0, 0, 2, 1, 0, 1, 0),
            "¬" to listOf(0, 0, 0, 0, 1, 2, 1, 1, 0),
            "↑" to listOf(0, 0, 0, 0, 2, 0, 1, 3, 1),
            "¹" to listOf(0, 0, 0, 0, 2, 1, 0, 2, 0),
            "²" to listOf(0, 0, 0, 0, 2, 1, 0, 2, 1),
            ":" to listOf(0, 1, 0, 0, 1, 0, 0, 2, 0),
            "󷑦" to listOf(0, 0, 0, 0, 2, 1, 0, 3, 1),
            "󷑣" to listOf(0, 0, 0, 1, 2, 0, 0, 1, 0),
            "󷐵" to listOf(0, 0, 0, 0, 2, 1, 1, 2, 1),
            "󷑹" to listOf(0, 0, 0, 0, 3, 0, 1, 1, 1),
            "󷑮" to listOf(0, 0, 0, 1, 1, 1, 1, 1, 1),
            "󷑨" to listOf(0, 0, 0, 1, 1, 1, 1, 2, 1),
            "-" to listOf(0, 0, 0, 1, 2, 1, 0, 0, 0),
            ">" to listOf(0, 0, 0, 1, 2, 1, 0, 1, 0),
            "󷑡" to listOf(0, 0, 0, 1, 2, 1, 0, 1, 1),
            "ℓ" to listOf(0, 1, 0, 0, 2, 0, 0, 2, 0),
            "▲" to listOf(0, 0, 0, 1, 2, 1, 0, 2, 0),
            "!" to listOf(0, 1, 0, 0, 2, 0, 0, 3, 0),
            "," to listOf(0, 2, 0, 0, 1, 0, 0, 0, 0),
            "≈" to listOf(0, 0, 0, 1, 2, 1, 1, 1, 0),
            "󷑷" to listOf(0, 0, 0, 1, 2, 1, 1, 1, 1),
            "?" to listOf(0, 1, 0, 0, 2, 0, 1, 2, 1),
            "º" to listOf(0, 0, 0, 1, 2, 1, 1, 2, 1),
            "×" to listOf(0, 0, 0, 1, 3, 1, 0, 0, 0),
            ";" to listOf(0, 2, 0, 0, 1, 0, 0, 2, 0),
            "★" to listOf(0, 0, 0, 1, 3, 1, 0, 1, 0),
            "◀" to listOf(0, 0, 0, 1, 3, 1, 0, 1, 1),
            "󲢡" to listOf(0, 1, 0, 0, 3, 0, 0, 2, 0),
            "󲢻" to listOf(0, 0, 0, 1, 2, 2, 1, 1, 1),
            "^" to listOf(0, 0, 0, 2, 1, 2, 0, 2, 0),
            "󲢿" to listOf(0, 1, 0, 1, 2, 0, 0, 2, 0),
            "z" to listOf(0, 1, 1, 0, 2, 0, 0, 1, 0),
            "󷀩" to listOf(0, 2, 0, 0, 2, 0, 0, 0, 0),
            "↗" to listOf(0, 0, 0, 1, 2, 2, 1, 2, 1),
            "▶" to listOf(0, 0, 0, 1, 3, 1, 1, 1, 0),
            "▼" to listOf(0, 0, 0, 1, 3, 1, 1, 1, 1),
            "¡" to listOf(0, 2, 0, 0, 2, 0, 0, 1, 0),
            "󲢫" to listOf(0, 0, 0, 2, 2, 1, 1, 1, 1),
            "»" to listOf(0, 0, 0, 2, 2, 2, 0, 0, 0),
            "T" to listOf(0, 1, 0, 0, 3, 0, 1, 3, 1),
            "Y" to listOf(0, 1, 0, 0, 3, 0, 2, 1, 2),
            "t" to listOf(0, 1, 0, 1, 2, 0, 1, 2, 1),
            "÷" to listOf(0, 1, 0, 1, 2, 1, 0, 1, 0),
            "s" to listOf(0, 1, 0, 1, 2, 1, 0, 1, 1),
            "|" to listOf(0, 2, 0, 0, 2, 0, 0, 2, 0),
            "↖" to listOf(0, 0, 0, 2, 2, 1, 1, 2, 1),
            "v" to listOf(0, 1, 0, 1, 2, 1, 1, 0, 1),
            "󶛱" to listOf(0, 1, 0, 1, 2, 1, 1, 1, 0),
            "9" to listOf(0, 1, 0, 1, 2, 1, 1, 2, 1),
            "ə" to listOf(0, 1, 0, 1, 2, 2, 0, 1, 0),
            "󷑧" to listOf(0, 1, 0, 1, 2, 2, 0, 1, 1),
            ")" to listOf(0, 2, 0, 0, 2, 1, 0, 2, 0),
            "󷀷" to listOf(0, 2, 0, 1, 2, 0, 0, 0, 0),
            "4" to listOf(0, 0, 0, 2, 2, 2, 1, 2, 1),
            "+" to listOf(0, 1, 0, 1, 3, 1, 0, 1, 0),
            "o" to listOf(0, 1, 0, 2, 1, 2, 0, 1, 0),
            "󷀳" to listOf(0, 2, 1, 0, 2, 0, 0, 0, 0),
            "„" to listOf(1, 1, 1, 0, 1, 1, 0, 0, 0),
            "_" to listOf(1, 2, 1, 0, 0, 0, 0, 0, 0),
            "e" to listOf(0, 1, 0, 2, 2, 1, 0, 1, 0),
            "L" to listOf(0, 1, 1, 1, 2, 0, 1, 1, 0),
            "(" to listOf(0, 2, 0, 1, 2, 0, 0, 2, 0),
            "5" to listOf(0, 1, 0, 1, 2, 2, 1, 2, 1),
            "©" to listOf(0, 1, 0, 2, 1, 2, 1, 1, 1),
            "J" to listOf(1, 1, 0, 0, 2, 1, 1, 2, 1),
            "󷂢" to listOf(1, 2, 0, 0, 2, 0, 0, 0, 0),
            "∞" to listOf(0, 0, 0, 2, 3, 2, 1, 1, 1),
            "ŧ" to listOf(0, 1, 0, 1, 3, 1, 1, 2, 1),
            "ō" to listOf(0, 1, 0, 2, 1, 2, 1, 2, 1),
            "C" to listOf(0, 1, 1, 2, 1, 0, 1, 2, 1),
            "Ț" to listOf(0, 2, 0, 0, 3, 0, 1, 3, 1),
            "[" to listOf(0, 2, 0, 1, 2, 0, 1, 2, 0),
            "ț" to listOf(0, 2, 0, 1, 2, 0, 1, 2, 1),
            "󶐤" to listOf(0, 2, 0, 1, 2, 1, 0, 1, 0),
            "ș" to listOf(0, 2, 0, 1, 2, 1, 0, 1, 1),
            "V" to listOf(0, 1, 0, 1, 3, 1, 2, 1, 2),
            "U" to listOf(0, 1, 0, 2, 1, 2, 2, 1, 2),
            "ē" to listOf(0, 1, 0, 2, 2, 1, 1, 2, 1),
            "󶐀" to listOf(0, 1, 0, 2, 2, 2, 0, 1, 0),
            "≥" to listOf(0, 1, 1, 1, 2, 1, 1, 1, 0),
            "↘" to listOf(0, 1, 1, 1, 2, 2, 0, 0, 0),
            "\\" to listOf(0, 1, 2, 0, 2, 0, 2, 1, 0),
            "y" to listOf(0, 2, 0, 1, 2, 1, 1, 0, 1),
            "󶒣" to listOf(1, 1, 0, 1, 2, 1, 0, 1, 0),
            "≤" to listOf(1, 1, 0, 1, 2, 1, 0, 1, 1),
            "∫" to listOf(1, 2, 0, 0, 2, 0, 0, 2, 1),
            "/" to listOf(2, 1, 0, 0, 2, 0, 0, 1, 2),
            "♥" to listOf(0, 0, 0, 2, 3, 2, 2, 1, 2),
            "O" to listOf(0, 1, 0, 2, 1, 2, 2, 2, 2),
            "󶐶" to listOf(0, 1, 0, 2, 2, 2, 0, 2, 0),
            "ȳ" to listOf(0, 2, 0, 1, 2, 1, 1, 1, 1),
            "]" to listOf(0, 2, 1, 0, 2, 1, 0, 2, 1),
            "F" to listOf(1, 0, 0, 2, 2, 1, 1, 2, 1),
            "3" to listOf(1, 1, 0, 0, 2, 2, 1, 2, 1),
            "󶐲" to listOf(1, 1, 1, 0, 2, 1, 0, 1, 0),
            "…" to listOf(1, 1, 1, 1, 1, 1, 0, 0, 0),
            "Ł" to listOf(0, 1, 1, 2, 2, 0, 1, 1, 0),
            "ȷ" to listOf(1, 2, 0, 0, 2, 1, 0, 1, 0),
            "󶐷" to listOf(1, 2, 0, 0, 2, 1, 0, 1, 1),
            "6" to listOf(0, 1, 0, 2, 2, 2, 1, 2, 0),
            "8" to listOf(0, 1, 0, 2, 2, 2, 1, 2, 1),
            "∂" to listOf(0, 1, 0, 2, 2, 2, 1, 2, 2),
            "Ŀ" to listOf(0, 1, 1, 1, 2, 2, 1, 1, 0),
            "c" to listOf(0, 1, 1, 2, 2, 1, 0, 1, 0),
            "ọ" to listOf(0, 2, 0, 2, 1, 2, 0, 1, 0),
            "󷀱" to listOf(0, 3, 1, 0, 2, 0, 0, 0, 0),
            "P" to listOf(1, 0, 0, 2, 2, 1, 2, 2, 1),
            "ẹ" to listOf(0, 2, 0, 2, 2, 1, 0, 1, 0),
            "Ļ" to listOf(0, 2, 1, 1, 2, 0, 1, 1, 0),
            "ø" to listOf(1, 1, 0, 2, 1, 2, 0, 1, 1),
            "1" to listOf(1, 1, 1, 0, 2, 1, 1, 2, 0),
            "2" to listOf(1, 1, 1, 0, 2, 1, 1, 2, 1),
            "ı" to listOf(1, 1, 1, 0, 3, 0, 1, 1, 0),
            "ƒ" to listOf(1, 2, 0, 0, 3, 0, 0, 2, 1),
            "0" to listOf(0, 1, 0, 2, 2, 2, 2, 2, 2),
            "ŉ" to listOf(0, 1, 1, 1, 2, 2, 2, 1, 1),
            "G" to listOf(0, 1, 1, 2, 1, 2, 1, 2, 1),
            "ī" to listOf(1, 1, 1, 0, 3, 0, 1, 2, 0),
            "¿" to listOf(1, 2, 1, 0, 2, 0, 0, 1, 0),
            "󶒥" to listOf(0, 1, 0, 2, 3, 2, 1, 1, 1),
            "¶" to listOf(0, 1, 1, 1, 2, 2, 2, 2, 2),
            "d" to listOf(0, 1, 1, 2, 2, 2, 0, 1, 2),
            "‡" to listOf(0, 2, 0, 1, 3, 1, 1, 2, 1),
            "Þ" to listOf(1, 0, 0, 2, 2, 2, 2, 2, 1),
            "№" to listOf(1, 0, 0, 2, 2, 2, 2, 2, 2),
            "S" to listOf(1, 1, 0, 1, 2, 2, 1, 2, 1),
            "l" to listOf(1, 1, 1, 0, 3, 0, 1, 3, 0),
            "I" to listOf(1, 1, 1, 0, 3, 0, 1, 3, 1),
            "Z" to listOf(1, 1, 1, 1, 2, 0, 1, 2, 1),
            "Q" to listOf(0, 1, 1, 2, 1, 2, 2, 2, 2),
            "đ" to listOf(0, 1, 1, 2, 2, 2, 0, 2, 2),
            "u" to listOf(0, 1, 1, 2, 2, 2, 1, 0, 1),
            "󶛴" to listOf(0, 1, 1, 2, 3, 1, 0, 1, 1),
            "Ụ" to listOf(0, 2, 0, 2, 1, 2, 2, 1, 2),
            "󶐹" to listOf(0, 2, 0, 2, 2, 2, 0, 1, 0),
            "ş" to listOf(1, 2, 0, 1, 2, 1, 0, 1, 1),
            "Ọ" to listOf(0, 2, 0, 2, 1, 2, 2, 2, 2),
            "X" to listOf(1, 0, 1, 1, 3, 1, 2, 2, 2),
            "∏" to listOf(1, 0, 1, 2, 1, 2, 2, 2, 2),
            "w" to listOf(1, 0, 1, 2, 2, 2, 1, 0, 1),
            "Ĳ" to listOf(1, 1, 0, 2, 1, 2, 2, 1, 2),
            "#" to listOf(1, 1, 0, 2, 2, 1, 1, 2, 2),
            "ū" to listOf(0, 1, 1, 2, 2, 2, 1, 2, 1),
            "R" to listOf(1, 0, 1, 2, 2, 1, 2, 2, 1),
            "n" to listOf(1, 0, 1, 2, 2, 2, 1, 1, 0),
            "π" to listOf(1, 0, 1, 2, 2, 2, 1, 1, 1),
            "D" to listOf(1, 1, 0, 2, 1, 2, 2, 2, 1),
            "ŀ" to listOf(1, 1, 1, 0, 3, 1, 1, 3, 0),
            "±" to listOf(1, 1, 1, 1, 2, 1, 1, 2, 1),
            "a" to listOf(1, 1, 1, 1, 2, 2, 0, 1, 0),
            "󷀴" to listOf(1, 2, 1, 1, 2, 0, 0, 0, 0),
            "¤" to listOf(0, 1, 1, 2, 3, 1, 1, 2, 1),
            "ę" to listOf(0, 2, 1, 2, 2, 1, 0, 1, 0),
            "ĸ" to listOf(1, 0, 1, 2, 3, 1, 1, 1, 1),
            "󶐱" to listOf(1, 1, 1, 1, 3, 1, 0, 1, 0),
            "↙" to listOf(1, 1, 1, 2, 2, 1, 0, 0, 0),
            "󶐳" to listOf(1, 2, 1, 0, 2, 1, 1, 1, 0),
            "Œ" to listOf(0, 1, 1, 2, 2, 2, 2, 2, 2),
            "󷀹" to listOf(0, 3, 1, 1, 2, 1, 0, 0, 0),
            "A" to listOf(1, 0, 1, 2, 2, 2, 1, 3, 1),
            "h" to listOf(1, 0, 1, 2, 2, 2, 2, 1, 0),
            "N" to listOf(1, 0, 1, 2, 2, 2, 2, 1, 2),
            "ł" to listOf(1, 1, 1, 1, 3, 0, 1, 3, 0),
            "󷀶" to listOf(1, 3, 1, 0, 2, 0, 0, 0, 0),
            "q" to listOf(0, 1, 2, 2, 2, 2, 0, 1, 1),
            "Ģ" to listOf(0, 2, 1, 2, 1, 2, 1, 2, 1),
            "ħ" to listOf(1, 0, 1, 2, 2, 2, 2, 2, 0),
            "W" to listOf(1, 0, 1, 2, 2, 2, 2, 2, 2),
            "k" to listOf(1, 0, 1, 2, 3, 1, 2, 1, 1),
            "b" to listOf(1, 1, 0, 2, 2, 2, 2, 1, 0),
            "ā" to listOf(1, 1, 1, 1, 2, 2, 1, 2, 1),
            "f" to listOf(1, 1, 1, 1, 3, 1, 0, 3, 1),
            "x" to listOf(1, 1, 1, 1, 3, 1, 1, 1, 1),
            "󷀰" to listOf(1, 2, 1, 1, 2, 1, 0, 0, 0),
            "Ç" to listOf(0, 3, 1, 2, 1, 0, 1, 2, 1),
            "B" to listOf(1, 1, 0, 2, 2, 2, 2, 2, 1),
            "Ș" to listOf(1, 2, 0, 1, 2, 2, 1, 2, 1),
            "ļ" to listOf(1, 2, 1, 0, 3, 0, 1, 3, 0),
            "Ị" to listOf(1, 2, 1, 0, 3, 0, 1, 3, 1),
            "󶐵" to listOf(1, 2, 1, 1, 2, 1, 0, 1, 1),
            "ɲ" to listOf(2, 0, 1, 2, 1, 2, 1, 1, 0),
            "ụ" to listOf(0, 2, 1, 2, 2, 2, 1, 0, 1),
            "Ų" to listOf(0, 3, 0, 2, 1, 2, 2, 1, 2),
            "£" to listOf(1, 1, 1, 2, 2, 1, 1, 2, 1),
            "󷋴" to listOf(1, 3, 1, 0, 2, 1, 0, 0, 0),
            "%" to listOf(1, 1, 1, 1, 3, 1, 2, 2, 1),
            "ẞ" to listOf(1, 1, 1, 2, 1, 2, 2, 2, 1),
            "Ω" to listOf(1, 1, 1, 2, 1, 2, 2, 2, 2),
            "ą" to listOf(1, 1, 2, 1, 2, 2, 0, 1, 0),
            "Đ" to listOf(1, 1, 0, 2, 2, 3, 2, 2, 2),
            "E" to listOf(1, 1, 1, 2, 2, 1, 2, 2, 1),
            "Δ" to listOf(1, 1, 1, 2, 2, 2, 0, 3, 0),
            "ņ" to listOf(1, 1, 1, 2, 2, 2, 1, 1, 0),
            "m" to listOf(1, 1, 1, 2, 2, 2, 1, 1, 1),
            "ạ" to listOf(1, 2, 1, 1, 2, 2, 0, 1, 0),
            "þ" to listOf(2, 1, 0, 2, 1, 2, 2, 1, 0),
            "ç" to listOf(0, 3, 1, 2, 2, 1, 0, 1, 0),
            "&" to listOf(1, 1, 1, 2, 2, 2, 1, 2, 0),
            "󶐴" to listOf(1, 2, 1, 1, 3, 1, 0, 1, 0),
            "Ạ" to listOf(1, 1, 1, 2, 2, 2, 1, 3, 1),
            "Ņ" to listOf(1, 1, 1, 2, 2, 2, 2, 1, 2),
            "ŋ" to listOf(1, 1, 2, 2, 1, 2, 1, 1, 0),
            "ǫ" to listOf(1, 3, 0, 2, 1, 2, 0, 1, 0),
            "p" to listOf(2, 1, 0, 2, 2, 2, 1, 1, 0),
            "‰" to listOf(1, 1, 1, 2, 2, 2, 2, 2, 0),
            "ß" to listOf(1, 1, 1, 2, 2, 2, 2, 2, 1),
            "ķ" to listOf(1, 1, 1, 2, 3, 1, 2, 1, 1),
            "ŗ" to listOf(1, 2, 1, 1, 3, 1, 1, 1, 1),
            "󷀸" to listOf(1, 3, 1, 1, 2, 1, 0, 0, 0),
            "μ" to listOf(2, 1, 1, 2, 1, 2, 1, 0, 1),
            "" to listOf(0, 2, 1, 2, 3, 2, 1, 2, 1),
            "ų" to listOf(0, 2, 2, 2, 2, 2, 1, 0, 1),
            "œ" to listOf(1, 1, 1, 2, 3, 2, 1, 1, 1),
            "Į" to listOf(1, 3, 1, 0, 3, 0, 1, 3, 1),
            "g" to listOf(1, 2, 1, 2, 2, 2, 0, 1, 1),
            "Æ" to listOf(1, 1, 1, 2, 3, 2, 1, 3, 2),
            "Ẹ" to listOf(1, 2, 1, 2, 2, 1, 2, 2, 1),
            "Ǫ" to listOf(1, 3, 0, 2, 1, 2, 2, 2, 2),
            "Ą" to listOf(1, 1, 2, 2, 2, 2, 1, 3, 1),
            "§" to listOf(1, 2, 1, 2, 2, 2, 1, 2, 1),
            "@" to listOf(1, 2, 1, 2, 2, 2, 1, 2, 2),
            "Ɲ" to listOf(2, 0, 1, 3, 2, 3, 2, 1, 2),
            "◊" to listOf(1, 3, 1, 2, 1, 2, 1, 3, 1),
            "Ę" to listOf(1, 2, 2, 2, 2, 1, 2, 2, 1),
            "Ŋ" to listOf(1, 1, 2, 3, 2, 3, 2, 1, 2),
        )
    }

    init {
        ImageIO.read(imageFile)?.let { image ->
            val ratio = pixelRatio * image.height / image.width
            height = Math.round(width * ratio).toInt()
            initializeMatrix()

            if (useSubPixels) {
                renderSubPixelMatrix(image)
            } else {
                renderSimpleMatrix(image)
            }
        }
    }

    private fun renderSimpleMatrix(image: BufferedImage) {
        val img = image.scale(width, height)
        val hasAlpha = img.colorModel.hasAlpha()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val c = Color(img.getRGB(x, y), hasAlpha)
                val color = AnsiColorRgb(r = c.red, g = c.green, b = c.blue, a = c.alpha)
                if (!color.isTransparent()) {
                    setBgColor(x, y, color)
                }
            }
        }
    }

    private fun renderSubPixelMatrix(image: BufferedImage) {
        val w = width * 3
        val h = height * 3
        val img = image.scale(w, h)
        val hasAlpha = img.colorModel.hasAlpha()
        for (y in 0 until h step 3) {
            for (x in 0 until w step 3) {
                val colors = listOf(
                    /* 0-0 */ Color(img.getRGB(x, y), hasAlpha),
                    /* 0-1 */ if (x + 1 < w) Color(img.getRGB(x + 1, y), hasAlpha) else Color(0, 0, 0),
                    /* 0-2 */ if (x + 2 < w) Color(img.getRGB(x + 2, y), hasAlpha) else Color(0, 0, 0),
                    /* 1-0 */ if (y + 1 < h) Color(img.getRGB(x, y + 1), hasAlpha) else Color(0, 0, 0),
                    /* 1-0 */ if (y + 1 < h && x + 1 < w) Color(img.getRGB(x + 1, y + 1), hasAlpha) else Color(0, 0, 0),
                    /* 1-0 */ if (y + 1 < h && x + 2 < w) Color(img.getRGB(x + 2, y + 1), hasAlpha) else Color(0, 0, 0),
                    /* 2-0 */ if (y + 2 < h) Color(img.getRGB(x, y + 2), hasAlpha) else Color(0, 0, 0),
                    /* 2-0 */ if (y + 2 < h && x + 1 < w) Color(img.getRGB(x + 1, y + 2), hasAlpha) else Color(0, 0, 0),
                    /* 2-0 */ if (y + 2 < h && x + 2 < w) Color(img.getRGB(x + 2, y + 2), hasAlpha) else Color(0, 0, 0),
                )
                val blockFinal =
                    colors.map { 3 - ((it.red / 255.0 * 0.3 + it.green / 255.0 * 0.59 + it.blue / 255.0 * 0.11) * 3).toInt() }
                if (blockFinal.sum() > 0) {
                    val color = AnsiColorRgb(colors.reduce { a, b -> a.fade(b) })
                    if (!color.isTransparent()) {
                        set(x / 3, y / 3, AnsiColorChar(fgColor = color, char = distance(blockFinal)))
                    }
                }
            }
        }
    }

    private fun distance(y: List<Int>): String {
        return ASCII_ART_CHARS_SUBPIXELS.keys.associate { key ->
            Pair(key, ASCII_ART_CHARS_SUBPIXELS[key]!!.zip(y).sumOf { e -> Math.abs(e.first - e.second) })
        }.entries.minByOrNull { it.value }!!.key
    }
}
