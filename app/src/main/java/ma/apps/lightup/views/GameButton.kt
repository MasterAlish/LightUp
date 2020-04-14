package ma.apps.lightup.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ma.apps.lightup.R

class GameButton(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    private var text: String = ""
    private var textSize: Float = 30f
    private var icon: Drawable? = null
    private var bgColor: Int = Color.BLACK
    private var isHighlighted: Boolean = false
    private lateinit var normalBg: GradientDrawable
    private lateinit var hightlightBg: GradientDrawable

    private lateinit var textMain: TextView
    private lateinit var mainBgView: View
    private lateinit var iconView: ImageView

    init {
        initStyleAttrs(context, attrs)
        initDrawables()
        initLayout()
        setText(text)
        invalidateStyle()
    }

    @SuppressLint("Recycle")
    private fun initStyleAttrs(context: Context, attrs: AttributeSet?) {
        val styleAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.GameButton, 0, 0
        ) as TypedArray
        bgColor = styleAttrs.getColor(R.styleable.GameButton_backgroundColor, Color.CYAN)
        text = styleAttrs.getString(R.styleable.GameButton_android_text) ?: ""
        textSize = styleAttrs.getDimension(R.styleable.GameButton_android_textSize, 30f)
        icon = styleAttrs.getDrawable(R.styleable.GameButton_icon)
        isHighlighted = styleAttrs.getBoolean(R.styleable.GameButton_isHighlighted, false)
        styleAttrs.recycle()
    }

    private fun initLayout() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_button, this, true)

        textMain = findViewById(R.id.textView)
        textMain.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        mainBgView = findViewById(R.id.bgView)
        iconView = findViewById(R.id.iconView)

        val textMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics)
        if (icon != null) {
            val lp = textMain.layoutParams as LayoutParams
            lp.setMargins(0, 0, textMargin.toInt(), 0)
            textMain.layoutParams = lp
            iconView.visibility = View.VISIBLE
        } else {
            iconView.visibility = View.GONE
        }
    }

    fun setText(text: String) {
        this.text = text
        textMain.text = text
    }

    fun setHighlighted(isHighlighted: Boolean) {
        this.isHighlighted = isHighlighted
        invalidateStyle()
    }

    private fun invalidateStyle() {
        if (isHighlighted) {
            mainBgView.background = hightlightBg
            textMain.setTextColor(resources.getColor(R.color.colorSecondary))
        } else {
            mainBgView.background = normalBg
            textMain.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }

    private fun initDrawables() {
        val cornerRadius =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)

        normalBg = GradientDrawable()
        normalBg.shape = GradientDrawable.RECTANGLE
        normalBg.cornerRadii = floatArrayOf(
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius
        )
        normalBg.setColor(resources.getColor(R.color.colorPrimaryBackground))

        hightlightBg = GradientDrawable()
        hightlightBg.shape = GradientDrawable.RECTANGLE
        hightlightBg.cornerRadii = floatArrayOf(
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius
        )
        hightlightBg.setColor(resources.getColor(R.color.colorSecondaryBackground))
    }
}