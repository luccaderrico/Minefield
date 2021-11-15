package model

enum class FieldEvent { OPEN, MARK, UNMARK, EXPLOSION, RESET }

data class Field(val linha: Int, val coluna: Int) {

    private val neightbours = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var marked: Boolean = false
    var opened: Boolean = false
    var mined: Boolean = false

    // Somente leitura
    val unmarked: Boolean get() = !marked
    val closed: Boolean get() = !opened
    val safe: Boolean get() = !mined
    val missionAcomplished: Boolean get() = safe && opened || mined && marked
    val qtyMinedNeightbours: Int get() = neightbours.filter { it.mined }.size
    val safeNeightbourhood: Boolean
        get() = neightbours.map { it.safe }.reduce {resultado, seguro -> resultado && seguro}

    fun addNeightbor(neightbor: Field) {
        neightbours.add(neightbor)
    }

    fun onEvent(callback: (Field, FieldEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (closed) {
            opened = true
            if (mined) {
                callbacks.forEach { it(this, FieldEvent.EXPLOSION) }
            }
            else {
                callbacks.forEach { it(this, FieldEvent.OPEN) }
                neightbours.filter { it.closed && it.safe && safeNeightbourhood }.forEach { it.open() }
            }
        }
    }

    fun changeMarkdown(){
        if(closed){
            marked = unmarked
            val event = if(marked) FieldEvent.MARK else FieldEvent.UNMARK
            callbacks.forEach { it(this, event) }
        }
    }

    fun undermine() {
        mined = true
    }

    fun reset() {
        opened = false
        mined = false
        marked = false
        callbacks.forEach { it(this, FieldEvent.RESET) }
    }
}