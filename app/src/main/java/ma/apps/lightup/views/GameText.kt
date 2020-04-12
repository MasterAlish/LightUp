package ma.apps.lightup.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import ma.apps.lightup.R

class GameText(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    private var text: String = ""
    private var textSize: Float = 20f
    private var textColor: Int = Color.WHITE
    private lateinit var textMain: TextView

    init {
        initStyleAttrs(context, attrs)
        initLayout()
        setText(text)
    }

    @SuppressLint("Recycle")
    private fun initStyleAttrs(context: Context, attrs: AttributeSet?) {
        val styleAttrs = context.obtainStyledAttributes(attrs,
            R.styleable.GameText, 0, 0) as TypedArray
        text = styleAttrs.getString(R.styleable.GameText_android_text) ?: ""
        textSize = styleAttrs.getDimension(R.styleable.GameText_android_textSize, 20f)
        textColor = styleAttrs.getColor(R.styleable.GameText_android_textColor, Color.WHITE)
        styleAttrs.recycle()
    }

    private fun initLayout() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_text, this, true)

        textMain = findViewById(R.id.textMain)
        textMain.textSize = textSize
        textMain.setTextColor(textColor)
    }

    fun setText(text: String) {
        this.text = text
        textMain.text = text
    }
}