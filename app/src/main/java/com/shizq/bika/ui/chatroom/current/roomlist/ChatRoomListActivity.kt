package com.shizq.bika.ui.chatroom.current.roomlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shizq.bika.BR
import com.shizq.bika.R
import com.shizq.bika.adapter.ChatRoomListAdapter
import com.shizq.bika.base.BaseActivity
import com.shizq.bika.databinding.ActivityChatRoomListBinding
import com.shizq.bika.network.Result
import com.shizq.bika.ui.account.AccountActivity
import com.shizq.bika.ui.chatroom.current.ChatRoomActivity
import com.shizq.bika.ui.chatroom.current.blacklist.ChatBlacklistActivity
import com.shizq.bika.utils.SPUtil
import com.shizq.bika.utils.TimeUtil
import kotlinx.coroutines.launch

/**
 * 新聊天室列表
 */

class ChatRoomListActivity : BaseActivity<ActivityChatRoomListBinding, ChatRoomListViewModel>() {
    private lateinit var mChatRoomsAdapter: ChatRoomListAdapter

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_chat_room_list
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.chatroomInclude.toolbar.title = "哔咔聊天室"
        setSupportActionBar(binding.chatroomInclude.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mChatRoomsAdapter = ChatRoomListAdapter()
        binding.chatroomRv.layoutManager = LinearLayoutManager(this)
        binding.chatroomRv.adapter = mChatRoomsAdapter

        //检查是否有token 没有就进行登录 显示登录提示框
        if (SPUtil.get<String>("chat_token", "") == "") {
            //没有token 登录聊天室
            showProgressBar(true, "获取用户信息...")
            viewModel.chatSignIn()
        } else {
            //有token 获取聊天室列表
            if (mChatRoomsAdapter.itemCount == 0) {
                //这个判断是防止重复请求
                showProgressBar(true, "")
                viewModel.chatRoomList()
            }
        }

        //网络重试点击事件监听
        binding.chatroomLoadLayout.setOnClickListener {
            showProgressBar(true, "")
            viewModel.chatRoomList()

        }

        binding.chatroomRv.setOnItemClickListener { _, position ->
            val userLevel = SPUtil.get("user_level", 1) as Int
            val data = mChatRoomsAdapter.getItemData(position)
            //注册天数和等级全符合才能跳转
            if (userLevel >= data.minLevel && TimeUtil().registerDays(data.minRegisterDays)) {
                val intent = Intent(this, ChatRoomActivity::class.java)
                intent.putExtra("title", mChatRoomsAdapter.getItemData(position).title)
                intent.putExtra("id", mChatRoomsAdapter.getItemData(position).id)
                startActivity(intent)
            } else {
                Toast.makeText(this, "你不能进入此聊天室", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_chat2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.action_blacklist -> {
                startActivity(Intent(this, ChatBlacklistActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewObservable() {
        viewModel.liveDataSignIn.observe(this) {
            //TODO 中途修改密码会登录失败，直接跳转到登录界面
            if (it.token != "") {
                SPUtil.put("chat_token", it.token)
                viewModel.chatRoomList()
//            } else if (it.statusCode == 401 && it.message == "API_ERROR_INVALIID_PASSWORD") {
//                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("网络错误")
                    .setMessage("code=${it.statusCode} error=${it.error} message=${it.message}")
                    .setPositiveButton("重新登录") { _, _ ->
                        SPUtil.remove("token")
                        SPUtil.remove("chat_token")
                        startActivity(Intent(this, AccountActivity::class.java))
                        finishAffinity()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        finish()
                    }
                    .show()
            }
        }
//        viewModel.liveDataRoomList.observe(this) {
//            if (it.rooms != null) {
//                binding.chatroomLoadLayout.visibility = ViewGroup.GONE
//                mChatRoomsAdapter.clear()
//                mChatRoomsAdapter.addData(it.rooms)
//            } else {
//                showProgressBar(
//                    false,
//                    "网络错误，点击重试\ncode=${it.statusCode} error=${it.error} message=${it.message}"
//                )
//            }
//        }

        lifecycleScope.launch {
            viewModel.roomListFlow.collect() {
                when (it) {
                    is Result.Success -> {
                        binding.chatroomLoadLayout.visibility = ViewGroup.GONE
                        mChatRoomsAdapter.clear()
                        mChatRoomsAdapter.addData(it.data.rooms)
                    }

                    is Result.Error -> {
                        val message = it.message
                        showProgressBar(
                    false,
                    "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                )
                    }

                    is Result.Loading -> {

                    }

                    else -> {}
                }
            }
        }
    }

    private fun showProgressBar(show: Boolean, string: String) {
        binding.chatroomLoadProgressBar.visibility =
            if (show) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.chatroomLoadError.visibility = if (show) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.chatroomLoadText.text = string
        binding.chatroomLoadLayout.isEnabled = !show
    }
}