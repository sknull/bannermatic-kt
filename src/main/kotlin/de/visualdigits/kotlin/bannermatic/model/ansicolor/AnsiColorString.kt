package de.visualdigits.kotlin.bannermatic.model.ansicolor

class AnsiColorString(
    var bgColor: AnsiColor<*> = AnsiColor4bit(),
    var fgColor: AnsiColor<*>? = null,
    var value: String = " "
) {

    fun clone(): AnsiColorString {
        return AnsiColorString(
            bgColor = bgColor.clone(),
            fgColor = fgColor?.clone(),
            value = value
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
            "$bgColor$fgColor$value"
        } else {
            "$bgColor$value"
        }
    }
}
