package ma.apps.lightup.game

object LevelManager {
    fun loadLevel(size: Int, difficulty: Difficulty, number: Int): Level {
        val index = levelIndex(size, difficulty, number)

        val cells = mutableListOf<List<Cell>>()
        val strRows = Levels[index].split("|")
        for (strRow in strRows) {
            val row = mutableListOf<Cell>()
            for (char in strRow) {
                row.add(
                    when (char) {
                        '*' -> Cell(CellType.WALL)
                        '1', '2', '3', '4' -> Cell(CellType.WALL_NUMBERED, char.toString().toInt())
                        else -> Cell(CellType.EMPTY)
                    }
                )
            }
            cells.add(row)
        }
        return Level(size, difficulty, number, cells)
    }

    fun levelIndex(
        size: Int,
        difficulty: Difficulty,
        number: Int
    ): Int {
        var index = when (size) {
            7 -> 0
            10 -> 16 * 3
            else -> 16 * 6
        }
        index += when (difficulty) {
            Difficulty.EASY -> 0
            Difficulty.MEDIUM -> 16
            Difficulty.HARD -> 16 * 2
        }

        index += number - 1
        return index
    }
}