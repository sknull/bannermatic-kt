package de.visualdigits.kotlin.extensions

import java.awt.RenderingHints
import java.awt.image.BufferedImage


fun BufferedImage.scale(width: Int, height: Int): BufferedImage {
    val resized = BufferedImage(width, height, this.type)
    val g = resized.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    g.drawImage(this, 0, 0, width, height, 0, 0, this.width, this.height, null)
    g.dispose()
    return resized
}
