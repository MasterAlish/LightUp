package ma.apps.lightup.game

data class Cell(val type: CellType, var number: Int = 0)

enum class CellType {
    EMPTY,
    WALL,
    WALL_NUMBERED,
    BULB,
    RED_BULB
}