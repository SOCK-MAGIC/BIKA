package com.shizq.bika.adapter

import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.ChatRoomBlackListBean
import com.shizq.bika.databinding.ItemChatRoomBlacklistBinding

//新聊天室黑名单
class ChatRoomBlackListAdapter :
    BaseBindingAdapter<ChatRoomBlackListBean.Doc, ItemChatRoomBlacklistBinding>(R.layout.item_chat_room_blacklist) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ChatRoomBlackListBean.Doc,
        binding: ItemChatRoomBlacklistBinding,
        position: Int
    ) {
    }
}