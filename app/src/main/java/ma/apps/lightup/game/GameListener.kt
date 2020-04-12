package ma.apps.lightup.game

interface GameListener {
    fun onCellUpdate(coord: Coord, cell: Cell)
}
