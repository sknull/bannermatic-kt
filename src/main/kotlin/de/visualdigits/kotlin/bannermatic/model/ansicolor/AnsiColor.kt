package de.visualdigits.kotlin.bannermatic.model.ansicolor

import java.awt.Color

interface AnsiColor<T : AnsiColor<T>> {
    val name: String
    var bgColor: AnsiCode
    val fgColor: AnsiCode?

    fun clone(): T

    fun toGray(): Double

    fun color(): Color
}
