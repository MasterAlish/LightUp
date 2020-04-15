package ma.apps.lightup.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import ma.apps.lightup.R
import ma.apps.lightup.listener.FinishDialogListener

class FinishDialog(context: Context, private val listener: FinishDialogListener ) : Dialog(context) {
    private lateinit var buttonNextLevel: GameButton
    private lateinit var buttonClose: GameButton
    private lateinit var titleView: TextView
    private lateinit var messageView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()

        buttonNextLevel.setOnClickListener { listener.onNextLevel(); this.dismiss() }
        buttonClose.setOnClickListener { listener.onFinish(); this.cancel() }
    }

    private fun initUI() {
        setContentView(R.layout.finish_dialog)
        this.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        titleView = findViewById(R.id.dialogTitle)
        messageView = findViewById(R.id.dialogMessage)
        buttonNextLevel = findViewById(R.id.dialogNextLevel)
        buttonClose = findViewById(R.id.dialogFinish)
    }
}