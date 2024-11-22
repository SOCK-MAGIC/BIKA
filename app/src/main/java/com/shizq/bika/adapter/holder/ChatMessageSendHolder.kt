package com.shizq.bika.adapter.holder

import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.ChatMessageBean
import com.shizq.bika.databinding.ItemChatMessageSendBinding

//新聊天室 我发送的消息
class ChatMessageSendHolder (viewGroup: ViewGroup, layoutId: Int) :
    BaseBindingHolder<ChatMessageBean, ItemChatMessageSendBinding>(viewGroup,layoutId) {

    override fun onBindingView(
        holder: BaseBindingHolder<*, *>,
        bean: ChatMessageBean,
        position: Int
    ) {
        val profile = bean.data.profile

        binding.chatNameR.text = profile.name

        if (profile.id.isNullOrEmpty()) {
            binding.chatMessageProgress.visibility = View.VISIBLE
        } else {
            binding.chatMessageProgress.visibility = View.GONE
        }

        //等级
        if (profile.level >= 0) {
            binding.chatLevelR.text = "Lv." + profile.level
        }

        //管理员 骑士等 图标和哔咔聊天室显示一致
        binding.chatKnightR.visibility = View.GONE
        binding.chatOfficialR.visibility = View.GONE
        binding.chatManagerR.visibility = View.GONE
        if (profile.characters.isNotEmpty()) {
            for (i in profile.characters) {
                when (i) {
                    "knight" -> {
                        binding.chatKnightR.visibility = View.VISIBLE
                    }
                    "official" -> {
                        binding.chatOfficialR.visibility = View.VISIBLE

                    }
                    "manager" -> {
                        binding.chatManagerR.visibility = View.VISIBLE

                    }
                }

            }
        }

        //回复的信息
        val reply = bean.data.reply
        if (reply != null) {
            binding.chatReplyLayoutR.visibility = ViewGroup.VISIBLE
            binding.chatReplyNameR.text = reply.name
            if (reply.type == "TEXT_MESSAGE") {
                binding.chatReplyR.text = reply.message
            }
            if (reply.type == "IMAGE_MESSAGE") {
                binding.chatReplyR.text = "[图片]"
            } else {
                binding.chatReplyImage.visibility = View.GONE

            }
        } else {
            binding.chatReplyLayoutR.visibility = ViewGroup.GONE
        }

        //消息
        val message = bean.data.message
        //艾特某人
        if (bean.data.userMentions.isNotEmpty()) {
            binding.chatAtGroupR.visibility = View.VISIBLE
            binding.chatAtGroupR.removeAllViews()
            for (i in bean.data.userMentions) {
                val chip = Chip(holder.itemView.context)
                chip.text = i.name
//                chip.textSize = 12f.dp
//                chip.height = 24.dp
                chip.setEnsureMinTouchTargetSize(false)//去除视图的顶部和底部的额外空间
                binding.chatAtGroupR.addView(chip)
            }
        } else {
            binding.chatAtGroupR.visibility = View.GONE
        }

        if (bean.type == "TEXT_MESSAGE") {
            binding.chatContentR.visibility = View.VISIBLE
            binding.chatContentR.text = message.message
        } else {
            binding.chatContentR.visibility = View.GONE
        }

        if (bean.type == "IMAGE_MESSAGE") {
            binding.chatContentImageR.visibility = View.VISIBLE
            if (message.caption != null && message.caption != "") {
                binding.chatContentR.visibility = View.VISIBLE
                binding.chatContentR.text = message.caption
            } else {
                binding.chatContentR.visibility = View.GONE
            }
        } else {
            binding.chatContentImageR.visibility = View.GONE
        }

        holder.addOnClickListener(R.id.chat_message_layout_r)
        holder.addOnClickListener(R.id.chat_reply_layout_r)
    }
}