package ma.apps.lightup.game

interface GameListener {
    fun onCellUpdate(coord: Coord, cell: Cell)
    fun onGameFinished()
    fun onGameCheckErrors(
        allCellsLighted: Boolean,
        noRedBulbs: Boolean,
        notFinishedWalls: Set<Coord>
    )
}
