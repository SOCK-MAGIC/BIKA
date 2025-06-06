package com.shizq.bika.adapter

import android.text.SpannableString
import android.view.View
import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.CommentsBean
import com.shizq.bika.databinding.ItemCommentsBinding
import com.shizq.bika.utils.TimeUtil

class CommentsAdapter :
    BaseBindingAdapter<CommentsBean.Comments.Doc, ItemCommentsBinding>(R.layout.item_comments) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: CommentsBean.Comments.Doc,
        binding: ItemCommentsBinding,
        position: Int,
    ) {
        if (bean._user != null) {
            // 有用户数据的评论
            binding.commentsName.text = bean._user.name // 用户名
            binding.commentsUserVer.text = "Lv.${bean._user.level}"// 等级
            binding.commentsUserGender.text =
                when (bean._user.gender) {
                    "m" -> "(绅士)"
                    "f" -> "(淑女)"
                    else -> "(机器人)"
                }
        } else {
            binding.commentsTime.text = TimeUtil().time(bean.created_at)// 时间
            binding.commentsLikeText.text = bean.likesCount.toString()// 爱心数
            binding.commentsLikeImage.setImageResource(if (bean.isLiked) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24)

            if (bean.isTop) {
                // 置顶评论
                binding.itemCommentsLine.visibility = View.GONE
//            comments_total++ //评论总数被置顶评论占用 所以加回去
                val stringBuilder = StringBuilder()
                stringBuilder.append(" ")
                stringBuilder.append(bean.content.trim())
                val spannableString = SpannableString(stringBuilder.toString())

                binding.commentsContentText.text = spannableString// 评论内容
                binding.commentsFloor.text = ""// 评论楼层
            } else if (bean.isMainComment) {
                // 主评论
                binding.itemCommentsLine.visibility = View.VISIBLE
                binding.commentsContentText.text = bean.content.trim()// 评论内容
                binding.commentsFloor.text = ""// 评论楼层
            } else {
                // 普通评论
                binding.itemCommentsLine.visibility = View.GONE
                binding.commentsContentText.text = bean.content.trim()// 评论内容
//            binding.commentsFloor.text = "${comments_total - modelPosition}F" //评论楼层
                binding.commentsFloor.text = ""
            }
//        bean._user.slogan

            if (bean.commentsCount == 0) {
                binding.commentsSubLayout.visibility = View.GONE
            } else {
                binding.commentsSubLayout.visibility = View.VISIBLE
                binding.commentsSubText.text = "共${bean.commentsCount}条回复"// 回复数
            }

            holder.addOnClickListener(R.id.comments_name)
            holder.addOnClickListener(R.id.comments_image_layout)
            holder.addOnClickListener(R.id.comments_like_layout)
            holder.addOnClickListener(R.id.comments_sub_layout)
        }
    }

    override fun bindViewPayloads(
        holder: BaseBindingHolder<*, *>,
        bean: CommentsBean.Comments.Doc,
        binding: ItemCommentsBinding,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        super.bindViewPayloads(holder, bean, binding, position, payloads)
        for (p in payloads) {
            bean.isLiked = p as Boolean
            if (bean.isLiked) {
                bean.likesCount++
            } else {
                bean.likesCount--
            }
            binding.commentsLikeText.text = bean.likesCount.toString()
            binding.commentsLikeImage.setImageResource(if (bean.isLiked) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24)
        }
    }

}