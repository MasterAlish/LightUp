package ma.apps.lightup

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ma.apps.lightup.game.*
import ma.apps.lightup.views.GameText
import java.util.*

class GameActivity : AppCompatActivity() {
    private val cells: MutableMap<Coord, FrameLayout> = mutableMapOf()
    private lateinit var gameField: LinearLayout
    private lateinit var game: Game
    private lateinit var levelLabel: GameText
    private lateinit var timeLabel: GameText
    private val sounds = GameSounds()
    private val timer = Timer()
    private var totalSeconds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_game)
        initGame()
        initUI()
        initAnimations()
        sounds.init(this)

        startTimer()
    }

    private fun initGame() {
        val level = LevelManager.loadLevel(14, Difficulty.HARD, 4)
        game = Game(level, gameListener)
    }

    private fun initUI() {
        gameField = findViewById(R.id.gameField)
        initGameField(gameField, game)

        levelLabel = findViewById(R.id.levelLabel)
        timeLabel = findViewById(R.id.timeLabel)

        levelLabel.setText(getString(R.string.level_d, game.level.index()))

        updateTime()
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                totalSeconds += 1
                runOnUiThread {
                    updateTime()
                }
            }
        }, 1000, 1000)
    }

    private fun updateTime() {
        var formatted = totalSeconds.toString()

        if (totalSeconds >= 60) {
            formatted = "${totalSeconds / 60}:${(totalSeconds % 60).toString().padStart(2, '0')}"
        }

        timeLabel.setText(getString(R.string.time_s, formatted))
    }

    private val onCellClickListener = View.OnClickListener { cell ->
        game.onClick(cell.tag as Coord)
    }

    private val gameListener = object : GameListener {
        override fun onCellUpdate(coord: Coord, cell: Cell) {
            cells[coord]!!.removeAllViews()
            when (cell.type) {
                CellType.EMPTY -> {
                    if (cell.number > 0) {
                        cells[coord]!!.setBackgroundResource(R.drawable.cell_lighted)
                    } else {
                        cells[coord]!!.setBackgroundResource(R.drawable.cell_empty)
                    }
                }
                CellType.BULB -> {
                    cells[coord]!!.setBackgroundResource(R.drawable.cell_lighted)
                    cells[coord]!!.addView(makeBulb())
                }
                CellType.RED_BULB -> {
                    cells[coord]!!.setBackgroundResource(R.drawable.cell_lighted)
                    cells[coord]!!.addView(makeBulb(true))
                }
                else -> Unit
            }
        }
    }

    private fun makeBulb(red: Boolean = false): ImageView {
        val margin = App.dimens.dpToPx(4f)
        val image = ImageView(this)
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(margin, margin, margin, margin)
        image.layoutParams = lp
        if (red) {
            image.setImageResource(R.drawable.ic_bulb_conflict)
        } else {
            image.setImageResource(R.drawable.ic_bulb)
        }
        return image
    }

    private fun initGameField(gameField: LinearLayout, game: Game) {
        val fieldPadding = App.dimens.dpToPx(4f)
        val textMargin = App.dimens.dpToPx(2f)
        val fieldWidth = App.dimens.deviceWidth - fieldPadding * 2
        val cellMargin = App.dimens.dpToPx(0.5f)
        val cellSize = fieldWidth / game.level.size - cellMargin * 2

        gameField.setPadding(fieldPadding, fieldPadding, fieldPadding, fieldPadding)
        gameField.clipChildren = false

        for (y in 0 until game.level.size) {
            val row = LinearLayout(this)
            row.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            row.clipChildren = false

            for (x in 0 until game.level.size) {
                val coord = Coord(x, y)

                val cell = FrameLayout(this)
                val cellParams = FrameLayout.LayoutParams(cellSize, cellSize)
                cellParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin)
                cell.clipChildren = false
                cell.layoutParams = cellParams
                when (game.level.cells[y][x].type) {
                    CellType.EMPTY -> cell.setBackgroundResource(R.drawable.cell_empty)
                    CellType.WALL -> cell.setBackgroundResource(R.drawable.cell_wall)
                    CellType.WALL_NUMBERED -> cell.setBackgroundResource(R.drawable.cell_wall)
                }

                if (game.level.cells[y][x].type == CellType.WALL_NUMBERED) {
                    val text = TextView(this)
                    val layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.gravity = Gravity.CENTER
                    layoutParams.bottomMargin = textMargin
                    val typeface = Typeface.createFromAsset(assets, "bellota_regular.ttf")
                    text.layoutParams = layoutParams
                    text.typeface = typeface
                    text.textSize = App.dimens.pxToDp((cellSize * 0.6).toInt())
                    text.setTextColor(Color.WHITE)
                    text.text = game.level.cells[y][x].number.toString()
                    cell.addView(text)
                }
                cell.clipToPadding = false
                cell.tag = coord
                cell.setOnClickListener(onCellClickListener)
                row.addView(cell)

                cells[coord] = cell
            }
            gameField.addView(row)
        }
    }

    private fun initAnimations() {
        val cellPadding = App.dimens.dpToPx(3f)

        val set = AnimationSet(true)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.duration = 200

        val jump = TranslateAnimation(0f, 0f, -cellPadding.toFloat(), cellPadding.toFloat())
        jump.repeatMode = Animation.REVERSE
        jump.repeatCount = Animation.INFINITE
        jump.duration = 200
        set.addAnimation(jump)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@GameActivity)
        val dialog = builder
            .setTitle(R.string.app_name)
            .setMessage(R.string.do_you_want_finish_this_game)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .create()
        dialog.show()
    }
}