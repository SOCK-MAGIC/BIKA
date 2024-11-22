package com.shizq.bika.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.set

import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.ComicListBean2
import com.shizq.bika.databinding.ItemComiclist2Binding
import com.shizq.bika.utils.GlideUrlNewKey


//漫画列表的第二种数据类型
class ComicListAdapter2 :
    BaseBindingAdapter<ComicListBean2.Comics, ItemComiclist2Binding>(R.layout.item_comiclist2) {
    private var dataSeal = ArrayList<CharSequence>()

    fun addSealData(data: ArrayList<CharSequence>) {
        dataSeal.clear()
        dataSeal.addAll(data)
        notifyDataSetChanged()
    }

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ComicListBean2.Comics,
        binding: ItemComiclist2Binding,
        position: Int
    ) {
        val dataIntersect = dataSeal intersect bean.categories
        if (dataIntersect.isNotEmpty()) {
            binding.comiclistItemContainer.visibility = View.GONE
            binding.comiclistItemSealText.visibility = View.VISIBLE
        } else {
            binding.comiclistItemContainer.visibility = View.VISIBLE
            binding.comiclistItemSealText.visibility = View.GONE
        }

        if (bean.likesCount != 0) {
            binding.comiclistItemLikeimage.visibility = View.VISIBLE
            binding.comiclistItemLiketext.visibility = View.VISIBLE
            binding.comiclistItemLiketext.text = bean.likesCount.toString()
        } else {
            binding.comiclistItemLikeimage.visibility = View.GONE
            binding.comiclistItemLiketext.visibility = View.GONE
        }
        if (bean.epsCount != 0) {
            if (bean.finished) {
                binding.comiclistItemBooktext.apply {
                    val content = "${bean.epsCount}E/${bean.pagesCount}P(完)"
                    val builder = SpannableStringBuilder(content)
                    val span = ForegroundColorSpan(resources.getColor(R.color.bika, null))
                    builder[content.length - 3, content.length] = span
                    text = builder
                }
            } else {
                binding.comiclistItemBooktext.text =
                    "${bean.epsCount}E/${bean.pagesCount}P"
            }
        } else {
            binding.comiclistItemBooktext.text = ""
        }

        if (bean.leaderboardCount != 0) {
            binding.comiclistItemClicks.visibility = View.VISIBLE
            binding.comiclistItemClicks.text = "指数：${bean.leaderboardCount}"
        } else {
            if (bean.totalViews != 0) {
                binding.comiclistItemClicks.visibility = View.VISIBLE
                binding.comiclistItemClicks.text = "指数：${bean.totalViews}"
            } else {
                binding.comiclistItemClicks.visibility = View.GONE
            }
        }

        var ategory = ""
        for (i in bean.categories) {
            ategory = "$ategory $i"
        }
        binding.comiclistItemCategory.text = "分类：$ategory"
    }
}