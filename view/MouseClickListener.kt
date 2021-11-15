package view

import model.Field
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseClickListener(
        private val field: Field,
        private val onLeftButtom: (Field) -> Unit,
        private val onRightButtom: (Field) -> Unit
) : MouseListener {

    override fun mousePressed(e: MouseEvent?) {
        when(e?.button) {
            MouseEvent.BUTTON1 -> onLeftButtom(field)
            MouseEvent.BUTTON3 -> onRightButtom(field)
        }
    }

    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
}