package ma.apps.lightup.game

import java.io.Serializable

data class Coord(val x: Int, val y: Int) : Serializable {
    override fun toString(): String {
        return "[$x:$y]"
    }
}