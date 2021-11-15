package view

import model.Board
import model.BoardEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() {
    MainScreen()
}

class MainScreen : JFrame() {

    private val board = Board(qtyLines = 16, qtyColumns = 30, qtyMines = 50)
    private val boardPanel = BoardPanel(board)

    init {
        board.onEvent(this::showResult)
        add(boardPanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun showResult(event: BoardEvent) {
        SwingUtilities.invokeLater {
            val msg = when(event){
                BoardEvent.VICTORY -> "Victory!"
                BoardEvent.DEFEAT -> "Try Again!"
            }

            JOptionPane.showMessageDialog(this, msg)
            board.reset()

            boardPanel.repaint()
            boardPanel.validate()
        }
    }
}

