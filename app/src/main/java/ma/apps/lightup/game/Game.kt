package ma.apps.lightup.game

class Game(val level: Level, val gameListener: GameListener) {
    private val cells: MutableMap<Coord, Cell> = mutableMapOf()
    private val bulbs: MutableSet<Coord> = mutableSetOf()

    init {
        for (y in 0 until level.size) {
            for (x in 0 until level.size) {
                cells[Coord(x, y)] = level.cells[y][x].copy()
            }
        }
    }

    fun onClick(coord: Coord) {
        when (cells[coord]!!.type) {
            CellType.EMPTY -> {
                cells[coord] = Cell(CellType.BULB)
                gameListener.onCellUpdate(coord, cells[coord]!!)
                bulbs.add(coord)

                onBulbsChanged()
            }
            CellType.BULB, CellType.RED_BULB -> {
                cells[coord] = Cell(CellType.EMPTY)
                gameListener.onCellUpdate(coord, cells[coord]!!)
                bulbs.remove(coord)

                onBulbsChanged()
            }
            else -> Unit
        }
    }

    private fun onBulbsChanged() {
        val lightedCellsNewState = calculateNewLightState()
        checkAndUpdateLightedCells(lightedCellsNewState)

        val conflictedBulbs = findConflictedBulbs(lightedCellsNewState)
        updateConflictedBulbs(conflictedBulbs)
    }

    private fun updateConflictedBulbs(conflictedBulbs: Set<Coord>) {
        bulbs.forEach { bulb ->
            val newType = when (bulb in conflictedBulbs) {
                true -> CellType.RED_BULB
                else -> CellType.BULB
            }
            if (cells[bulb]!!.type != newType) {
                cells[bulb] = Cell(newType)
                gameListener.onCellUpdate(bulb, cells[bulb]!!)
            }
        }
    }

    private fun findConflictedBulbs(lightedCellsNewState: Map<Coord, Int>): Set<Coord> {
        val conflicts = mutableSetOf<Coord>()
        bulbs.forEach { bulb ->
            if (lightedCellsNewState[bulb]!! > 0) {
                conflicts.add(bulb)
            }
        }
        return conflicts
    }

    private fun calculateNewLightState(): Map<Coord, Int> {
        val state = mutableMapOf<Coord, Int>()
        for (y in 0 until level.size) {
            for (x in 0 until level.size) {
                state[Coord(x, y)] = 0
            }
        }

        bulbs.forEach { bulb ->
            for (x in bulb.x + 1 until level.size) {
                val collide = traverseEmptyCell(Coord(x, bulb.y), state)
                if (collide) {
                    break
                }
            }
            for (x in bulb.x - 1 downTo 0) {
                val collide = traverseEmptyCell(Coord(x, bulb.y), state)
                if (collide) {
                    break
                }
            }
            for (y in bulb.y + 1 until level.size) {
                val collide = traverseEmptyCell(Coord(bulb.x, y), state)
                if (collide) {
                    break
                }
            }
            for (y in bulb.y - 1 downTo 0) {
                val collide = traverseEmptyCell(Coord(bulb.x, y), state)
                if (collide) {
                    break
                }
            }
        }

        return state
    }

    private fun traverseEmptyCell(coord: Coord, state: MutableMap<Coord, Int>): Boolean {
        val cell = cells[coord]!!
        if (cell.type == CellType.WALL_NUMBERED || cell.type == CellType.WALL) {
            return true
        } else {
            state[coord] = state[coord]!! + 1
        }
        return false
    }

    private fun checkAndUpdateLightedCells(newState: Map<Coord, Int>) {
        for (y in 0 until level.size) {
            for (x in 0 until level.size) {
                val coord = Coord(x, y)
                val cell = cells[coord]!!
                if (cell.type == CellType.EMPTY) {
                    if (cell.number != newState[coord]) {
                        cell.number = newState[coord]!!
                        gameListener.onCellUpdate(coord, cell)
                    }
                }
            }
        }
    }
}