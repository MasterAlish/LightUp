package ma.apps.lightup

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ma.apps.lightup.game.*
import ma.apps.lightup.listener.FinishDialogListener
import ma.apps.lightup.views.FinishDialog
import ma.apps.lightup.views.GameText
import ma.apps.lightup.views.HelpDialog
import java.util.*

class GameActivity : AppCompatActivity(), FinishDialogListener {
    private val cells: MutableMap<Coord, FrameLayout> = mutableMapOf()
    private lateinit var gameField: LinearLayout
    private lateinit var checkBtn: View
    private lateinit var game: Game
    private lateinit var levelLabel: GameText
    private lateinit var timeLabel: GameText
    private val sounds = GameSounds()
    private val timer = Timer()
    private var totalSeconds = 0
    private var size = 7
    private var demo = false
    private var difficulty = Difficulty.EASY
    private var number = 1
    private var level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_game)
        initParams()
        initGame()
        initUI()
        initAnimations()
        sounds.init(this)

        startTimer()
    }

    private fun initParams() {
        demo = intent.extras!!.getBoolean("demo", false)
        size = intent.extras!!.getInt("size", 7)
        level = intent.extras!!.getInt("level", 0)
        difficulty = when (level / 16) {
            0 -> Difficulty.EASY
            1 -> Difficulty.MEDIUM
            else -> Difficulty.HARD
        }
        number = (level % 16) + 1
    }

    private fun initGame() {
        val level = when (demo) {
            true -> LevelManager.loadDemoLevel()
            else -> LevelManager.loadLevel(size, difficulty, number)
        }
        game = Game(level, gameListener)
    }

    private fun initUI() {
        gameField = findViewById(R.id.gameField)
        initGameField(gameField, game)

        checkBtn = findViewById(R.id.checkBtn)
        checkBtn.setOnClickListener { game.checkForFinish() }

        timeLabel = findViewById(R.id.timeLabel)
        levelLabel = findViewById(R.id.levelLabel)
        levelLabel.setText(getString(R.string.level_d, size, size, level + 1))

        updateTime()
    }

    override fun onStart() {
        super.onStart()
        if (demo) {
            Handler().postDelayed({
                HelpDialog(this).show()
            }, 1000)
        }
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
                    if (cell.value > 0) {
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

        override fun onGameCheckErrors(
            allCellsLighted: Boolean,
            noRedBulbs: Boolean,
            notFinishedWalls: Set<Coord>
        ) {
            if (!allCellsLighted) {
                Toast.makeText(
                    this@GameActivity,
                    R.string.you_have_not_lighted_cells,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!noRedBulbs) {
                Toast.makeText(
                    this@GameActivity,
                    R.string.some_bulbs_are_intersecting,
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (notFinishedWalls.isNotEmpty()) {
                Toast.makeText(
                    this@GameActivity,
                    R.string.wrong_bulbs_around_walls,
                    Toast.LENGTH_SHORT
                )
                    .show()

                for (wall in notFinishedWalls) {
                    cells[wall]!!.setBackgroundResource(R.drawable.cell_wall_error)
                }
                Handler().postDelayed({
                    for (wall in notFinishedWalls) {
                        cells[wall]!!.setBackgroundResource(R.drawable.cell_wall)
                    }
                }, 2000)
            }
        }

        override fun onGameFinished() {
            val lastSavedLevel = App.cache.loadLastLevel(size)
            if (level >= lastSavedLevel) {
                App.cache.saveLastLevel(size, level + 1)
            }

            FinishDialog(this@GameActivity, this@GameActivity).show()
        }
    }

    override fun onFinish() {
        App.cache.setDemoPlayed()
        finish()
    }

    override fun onNextLevel() {
        var nextLevel = level + 1
        var nextSize = size
        var hasNext = true

        if (demo) {
            App.cache.setDemoPlayed()
            hasNext = true
            nextSize = 7
            nextLevel = 0
        } else {
            if (level < 16 * 3 - 1) {
                hasNext = true
            } else {
                if (size < 14) {
                    hasNext = true
                    nextSize = when (size) {
                        7 -> 10
                        else -> 14
                    }
                    nextLevel = 0
                }
            }
        }
        if (hasNext) {
            startActivity(
                Intent(this, GameActivity::class.java)
                    .putExtra("size", nextSize)
                    .putExtra("level", nextLevel)
            )
        }
        finish()
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
                    text.text = game.level.cells[y][x].value.toString()
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

    fun onWantToExit() {
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