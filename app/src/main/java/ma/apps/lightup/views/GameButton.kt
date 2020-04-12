package ma.apps.lightup.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import ma.apps.lightup.R

class GameButton(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    private var text: String = ""
    private var bgColor: Int = Color.BLACK
    private lateinit var firstBg: GradientDrawable

    private lateinit var textMain: TextView
    private lateinit var  mainBgView: View

    init {
        initStyleAttrs(context, attrs)
        initDrawables()
        initLayout()
        setText()
        setDrawables()
    }

    @SuppressLint("Recycle")
    private fun initStyleAttrs(context: Context, attrs: AttributeSet?) {
        val styleAttrs = context.obtainStyledAttributes(attrs,
            R.styleable.GameButton, 0, 0) as TypedArray
        bgColor = styleAttrs.getColor(R.styleable.GameButton_backgroundColor, Color.CYAN)
        text = styleAttrs.getString(R.styleable.GameButton_android_text) ?: ""
        styleAttrs.recycle()
    }

    private fun initLayout() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_button, this, true)

        textMain = findViewById(R.id.textView)
        mainBgView = findViewById(R.id.bgView)
    }

    private fun setText() {
        textMain.text = text
    }

    private fun initDrawables() {
        val cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)

        firstBg = GradientDrawable()
        firstBg.shape = GradientDrawable.RECTANGLE
        firstBg.cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        firstBg.setColor(Color.WHITE)
    }

    private fun setDrawables() {
        mainBgView.background = firstBg
    }
}