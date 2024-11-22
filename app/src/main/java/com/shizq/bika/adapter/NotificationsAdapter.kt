package com.shizq.bika.adapter

import android.view.View
import com.shizq.bika.R
import com.shizq.bika.base.BaseBindingAdapter
import com.shizq.bika.base.BaseBindingHolder
import com.shizq.bika.bean.NotificationsBean
import com.shizq.bika.databinding.ItemNotificationsBinding

import com.shizq.bika.utils.GlideUrlNewKey
import com.shizq.bika.utils.TimeUtil

class NotificationsAdapter :
    BaseBindingAdapter<NotificationsBean.Notifications.Doc, ItemNotificationsBinding>(R.layout.item_notifications) {
    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: NotificationsBean.Notifications.Doc,
        binding: ItemNotificationsBinding,
        position: Int
    ) {
        binding.itemNotificationsTime.text=TimeUtil().time(bean.created_at)

        if (bean.cover != null) { //头像框 新用户没有
            binding.itemNotificationsCover.visibility = View.VISIBLE

        } else {
            binding.itemNotificationsCover.visibility = View.GONE
        }

        holder.addOnClickListener(R.id.item_notifications_image_layout)
    }
}