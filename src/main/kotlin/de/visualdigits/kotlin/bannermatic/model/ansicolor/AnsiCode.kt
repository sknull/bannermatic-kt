package de.visualdigits.kotlin.bannermatic.model.ansicolor

enum class AnsiCode(
    val value: Int,
    val r: Int = 0,
    val g: Int = 0,
    val b: Int = 0,
) {

    NORMAL(0),
    BRIGHT(1),
    DIM(2),

    BLACK_FG(30),
    RED_FG(31, 255, 0, 0),
    GREEN_FG(32, 0, 255, 0),
    YELLOW_FG(33, 255, 255, 0),
    BLUE_FG(34, 0, 0, 255),
    MAGENTA_FG(35, 255, 0, 255),
    CYAN_FG(36, 0, 255, 255),
    WHITE_FG(37, 255, 255, 255),
    FOREGROUND(38),
    FOREGROUND_DEFAULT(39),

    BLACK_BG(40),
    RED_BG(41, 255, 0, 0),
    GREEN_BG(42, 0, 255, 0),
    YELLOW_BG(43, 255, 255, 0),
    BLUE_BG(44, 0, 0, 255),
    MAGENTA_BG(45, 255, 0, 255),
    CYAN_BG(46, 0, 255, 255),
    WHITE_BG(47, 255, 255, 255),
    BACKGROUND(48),
    BACKGROUND_DEFAULT(49),

    UNDERLINE(4),
    FRAMED(51),
    ENCIRCLED(52),
    OVERLINED(53),

    BLINK_SLOW(5),
    BLINK_RAPID(6),
    BLINK_OFF(25)
}