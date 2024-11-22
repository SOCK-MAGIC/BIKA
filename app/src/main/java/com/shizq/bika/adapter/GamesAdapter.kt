package com.shizq.bika.adapter

import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.GamesBean
import com.shizq.bika.databinding.ItemGamesBinding

import com.shizq.bika.utils.GlideUrlNewKey

class GamesAdapter :
    BaseBindingAdapter<GamesBean.Games.Docs, ItemGamesBinding>(R.layout.item_games) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: GamesBean.Games.Docs,
        binding: ItemGamesBinding,
        position: Int
    ) {

    }
}