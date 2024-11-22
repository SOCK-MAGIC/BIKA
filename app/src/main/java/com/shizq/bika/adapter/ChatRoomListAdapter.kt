package com.shizq.bika.adapter

import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.ChatRoomListBean
import com.shizq.bika.databinding.ItemChatRoomListBinding

//新聊天室列表
class ChatRoomListAdapter :
    BaseBindingAdapter<ChatRoomListBean.Room, ItemChatRoomListBinding>(R.layout.item_chat_room_list) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ChatRoomListBean.Room,
        binding: ItemChatRoomListBinding,
        position: Int
    ) {
    }
}