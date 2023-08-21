package de.visualdigits.kotlin.bannermatic.model.ansicolor

class AnsiColorChar(
    var bgColor: AnsiColor<*> = AnsiColor4bit(),
    var fgColor: AnsiColor<*>? = null,
    var char: String = " "
) {

    fun clone(): AnsiColorChar {
        return AnsiColorChar(
            bgColor = bgColor.clone(),
            fgColor = fgColor?.clone(),
            char = char
        )
    }

    init {
        when (bgColor) {
            is AnsiColorRgb -> (bgColor as AnsiColorRgb).bgColor = AnsiCode.BACKGROUND
        }
        when (fgColor) {
            is AnsiColorRgb -> (fgColor as AnsiColorRgb).bgColor = AnsiCode.FOREGROUND
        }
    }

    override fun toString(): String {
        return if (fgColor != null) {
            "$bgColor$fgColor$char"
        } else {
            "$bgColor$char"
        }
    }
}
