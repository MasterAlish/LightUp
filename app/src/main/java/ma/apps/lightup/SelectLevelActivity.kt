package ma.apps.lightup

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.apps.lightup.listener.OnItemClickListener

class SelectLevelActivity : AppCompatActivity(R.layout.activity_select_level), OnItemClickListener {
    private lateinit var backBtn: View
    private lateinit var levelsRecycler: RecyclerView
    private lateinit var adapter: LevelsAdapter
    private var size: Int = 7
    private var lastLevel: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initParams()
        initUI()
    }

    private fun initParams() {
        size = intent.extras!!.getInt("size", 7)
        App.cache.setLastPlayedSize(size)
    }

    private fun initUI() {
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener { onBackPressed() }

        levelsRecycler = findViewById(R.id.levelsRecycler)
        levelsRecycler.layoutManager = GridLayoutManager(this, 4)
        adapter = LevelsAdapter((16 * 3), this)
        levelsRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        lastLevel = App.cache.loadLastLevel(size)
        adapter.lastLevel = lastLevel
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        startActivity(
            Intent(this, GameActivity::class.java)
                .putExtra("size", size)
                .putExtra("level", position)
        )
    }
}