package com.shizq.bika.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shizq.bika.R
import com.shizq.bika.bean.GameInfoBean

class GameScreenshotAdapter(val context: Context) :
    RecyclerView.Adapter<GameScreenshotAdapter.ViewHolder>() {
    private lateinit var bean: GameInfoBean.Game
    private var datas = ArrayList<GameInfoBean.Game.Screenshot>()

    fun addData(bean: GameInfoBean.Game) {
        this.bean = bean
        datas.clear()
        datas.addAll(bean.screenshots)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, parent: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_game_screenshot, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(bean, position)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(it, datas[position])
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    lateinit var onItemClick: (v: View, data: GameInfoBean.Game.Screenshot) -> Unit

    fun setOnItemClickListener(onItemClick: (v: View, data: GameInfoBean.Game.Screenshot) -> Unit) {
        this.onItemClick = onItemClick
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun setData(bean: GameInfoBean.Game, position: Int) {
            val item = bean.screenshots[position]
        }

    }

}