package view

import model.Board
import java.awt.GridLayout
import javax.swing.JPanel

class BoardPanel (board: Board) : JPanel() {

    init {
        layout = GridLayout(board.qtyLines, board.qtyColumns)
        board.forEachField { field ->
            val buttom = FieldButtom(field)
            add(buttom)
        }
    }
}