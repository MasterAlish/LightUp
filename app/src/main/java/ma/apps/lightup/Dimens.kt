package ma.apps.lightup

import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager
import kotlin.math.roundToInt

class Dimens(app: App) {
    val pixelsInDp: Float
    val deviceWidth: Int
    val deviceHeight: Int

    init {
        val windowManager = app.getSystemService(WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        deviceWidth = size.x
        deviceHeight = size.y
        pixelsInDp =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, app.resources.displayMetrics)
    }

    fun dpToPx(dp: Float): Int {
        return (pixelsInDp * dp).roundToInt()
    }

    fun pxToDp(px: Int): Float {
        return px / pixelsInDp
    }
}