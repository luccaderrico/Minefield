package view

import model.Field
import model.FieldEvent
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

// BG = background
private val COLOR_NORMAL_BG = Color(184,184,184)
private val COLOR_MARKED_BG = Color(8,179,247)
private val COLOR_EXPLOSION_BG = Color(189,66,68)
private val COLOR_TXT_VERDE = Color(0,100,0)

class FieldButtom(private val field: Field) : JButton() {

    init {
        font = font.deriveFont(Font.BOLD)
        background = COLOR_NORMAL_BG
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, { it.open() }, { it.changeMarkdown() }))
        field.onEvent(this :: applyStyle)
    }

    private fun applyStyle(field: Field, event: FieldEvent) {
        when(event) {
            FieldEvent.EXPLOSION -> applyExplosionStyle()
            FieldEvent.OPEN -> applyOpenedStyle()
            FieldEvent.MARK -> applyMarkedStyle()
            else -> applyCustomStyle()
        }

        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyExplosionStyle() {
        background = COLOR_EXPLOSION_BG
        text = "X"
    }

    private fun applyOpenedStyle() {
        background = COLOR_NORMAL_BG
        border = BorderFactory.createLineBorder(Color.GRAY)
        foreground = when(field.qtyMinedNeightbours) {
            1 -> COLOR_TXT_VERDE
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }

        text = if (field.qtyMinedNeightbours > 0) field.qtyMinedNeightbours.toString() else ""
    }

    private fun applyMarkedStyle() {
        background = COLOR_MARKED_BG
        foreground = Color.BLACK
        text = "M"
    }

    private fun applyCustomStyle() {
        background = COLOR_NORMAL_BG
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }
}

