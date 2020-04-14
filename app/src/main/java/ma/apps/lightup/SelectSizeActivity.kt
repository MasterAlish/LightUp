package ma.apps.lightup

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SelectSizeActivity : AppCompatActivity(R.layout.activity_select_size) {
    private lateinit var backBtn: View
    private lateinit var size7Btn: View
    private lateinit var size10Btn: View
    private lateinit var size14Btn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initUI()
    }

    private fun initUI() {
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener { onBackPressed() }

        size7Btn = findViewById(R.id.size7Btn)
        size7Btn.setOnClickListener { onSizeSelected(7) }

        size10Btn = findViewById(R.id.size10Btn)
        size10Btn.setOnClickListener { onSizeSelected(10) }

        size14Btn = findViewById(R.id.size14Btn)
        size14Btn.setOnClickListener { onSizeSelected(14) }
    }

    private fun onSizeSelected(size: Int) {
        startActivity(Intent(this, SelectLevelActivity::class.java)
            .putExtra("size", size))
    }
}