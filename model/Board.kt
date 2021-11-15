package model

import java.util.*
import kotlin.collections.ArrayList

enum class BoardEvent { VICTORY, DEFEAT }

class Board(val qtyLines: Int, val qtyColumns: Int, private val qtyMines: Int) {
    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvent) -> Unit>()

    init {
        createField()
        associateNeightbors()
        drawMines()
    }

    private fun createField() {
        for(line in 0 until qtyLines) {
            fields.add(ArrayList())
            for (column in 0 until qtyColumns) {
                val newField = Field(line, column)
                newField.onEvent(this::checkVictoryOrDefeat)
                fields[line].add(newField)
            }
        }
    }

    private fun associateNeightbors() {
        forEachField { associateNeightbors(it) }
    }

    private fun associateNeightbors(field: Field) {
        val (line, column) = field
        val lines = arrayOf(line - 1, line, line + 1)
        val columns = arrayOf(column - 1, column, column + 1)

        lines.forEach { l ->
            columns.forEach { c ->
                val current = fields.getOrNull(l)?.getOrNull(c)
                current?.takeIf { field != it }?.let { field.addNeightbor(it) }
            }
        }
    }

    private fun drawMines() {
        val generator = Random()

        var drawnLine = -1
        var drawnColumn = -1
        var currentQtyMines = 0

        while(currentQtyMines < this.qtyMines) {
            drawnLine = generator.nextInt(qtyLines)
            drawnColumn = generator.nextInt(qtyColumns)

            val drawnField = fields[drawnLine][drawnColumn]
            if (drawnField.safe) {
                drawnField.undermine()
                currentQtyMines++
            }
        }
    }

    private fun missionAcomplished(): Boolean {
        var winner = true
        forEachField { if (!it.missionAcomplished) winner = false }
        return winner
    }

    private fun checkVictoryOrDefeat(field: Field, event: FieldEvent){
        if (event == FieldEvent.EXPLOSION) {
            callbacks.forEach { it(BoardEvent.DEFEAT) }
        }
        else if (missionAcomplished()) {
            callbacks.forEach { it(BoardEvent.VICTORY) }
        }
    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { lines -> lines.forEach(callback) }
    }

    fun onEvent(callback: (BoardEvent) -> Unit){
        callbacks.add(callback)
    }

    fun reset(){
        forEachField { it.reset() }
        drawMines()
    }
}