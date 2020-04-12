package ma.apps.lightup.game

class Level(val size: Int, val difficulty: Difficulty, val number: Int, val cells: List<List<Cell>>) {
    fun index(): Int {
        return LevelManager.levelIndex(size, difficulty, number)
    }
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}
