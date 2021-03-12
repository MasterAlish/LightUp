package ma.apps.lightup.game

data class Cell(val type: CellType, var value: Int = 0, var marked: Boolean = false)

enum class CellType {
    EMPTY,
    WALL,
    WALL_NUMBERED,
    BULB,
    RED_BULB
}