package ma.apps.lightup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ma.apps.lightup.listener.OnItemClickListener
import ma.apps.lightup.views.GameButton

class LevelsAdapter(val levels: Int, val listener: OnItemClickListener) :
    RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>() {
    var lastLevel: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.level_item, parent, false)
        return LevelViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return levels
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(position + 1, lastLevel + 1)
    }

    class LevelViewHolder(view: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        val levelBtn = view.findViewById<GameButton>(R.id.levelBtn)
        val lockedIcon = view.findViewById<View>(R.id.lockedIcon)

        init {
            levelBtn.setOnClickListener { listener.onItemClick(adapterPosition) }
        }

        fun bind(level: Int, lastLevel: Int) {
            levelBtn.setText(level.toString())
            levelBtn.setHighlighted(level == lastLevel)

            if (level > lastLevel) {
                levelBtn.visibility = View.GONE
                lockedIcon.visibility = View.VISIBLE
            } else {
                levelBtn.visibility = View.VISIBLE
                lockedIcon.visibility = View.GONE
            }
        }
    }
}
