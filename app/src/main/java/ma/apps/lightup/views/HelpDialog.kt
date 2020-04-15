package ma.apps.lightup.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import ma.apps.lightup.R

class HelpDialog(context: Context) : Dialog(context) {
    private lateinit var buttonClose: GameButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setContentView(R.layout.help_dialog)
        this.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        buttonClose = findViewById(R.id.gotIt)
        buttonClose.setOnClickListener { this.cancel() }
    }
}