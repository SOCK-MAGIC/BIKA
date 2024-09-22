package com.shizq.bika.ui.apps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shizq.bika.BR
import com.shizq.bika.R
import com.shizq.bika.adapter.ChatRoomListOldAdapter
import com.shizq.bika.adapter.PicaAppsAdapter
import com.shizq.bika.base.BaseFragment
import com.shizq.bika.databinding.FragmentAppsBinding
import com.shizq.bika.ui.chatroom.old.ChatRoomActivity
import com.shizq.bika.utils.SPUtil
import kotlinx.coroutines.launch
import com.shizq.bika.network.Result

class AppsFragment : BaseFragment<FragmentAppsBinding, AppsFragmentViewModel>() {
    private var str: String? = null
    private lateinit var mChatRoomListAdapter: ChatRoomListOldAdapter
    private lateinit var mPicaAppsAdapter: PicaAppsAdapter

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_apps
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        str = arguments?.getString("key")
        mChatRoomListAdapter = ChatRoomListOldAdapter()
        mPicaAppsAdapter = PicaAppsAdapter()
        binding.appsRv.layoutManager = LinearLayoutManager(context)
        when (str) {
            "chat" -> {
                binding.appsRv.adapter = mChatRoomListAdapter
                viewModel.getChatList()
            }

            "apps" -> {
                binding.appsRv.adapter = mPicaAppsAdapter
                viewModel.getPicaApps()
            }
        }
        binding.appsInclude.loadLayout.isEnabled = false
        initListener()
    }

    private fun initListener() {
        binding.appsRv.setOnItemClickListener { _, position ->
            if (str == "chat") {
                val intent = Intent(activity, ChatRoomActivity::class.java)
                intent.putExtra("title", mChatRoomListAdapter.getItemData(position).title)
                intent.putExtra("url", mChatRoomListAdapter.getItemData(position).url)
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = Uri.parse(
                    "${mPicaAppsAdapter.getItemData(position).url}/?token=${
                        SPUtil.get<String>("token", "")
                    }&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt"
                )
                startActivity(intent)
            }
        }

        //网络重试点击事件监听
        binding.appsInclude.loadLayout.setOnClickListener {
            binding.appsInclude.loadLayout.isEnabled = false
            binding.appsInclude.loadProgressBar.visibility = ViewGroup.VISIBLE
            binding.appsInclude.loadError.visibility = ViewGroup.GONE
            binding.appsInclude.loadText.text = ""
            if (this.str == "chat") {
                viewModel.getChatList()
            } else {
                viewModel.getPicaApps()
            }

        }
    }

    override fun initViewObservable() {
        //旧聊天室列表
        lifecycleScope.launch {
            viewModel.roomList.collect {
                when (it) {
                    is Result.Success -> {
                        mChatRoomListAdapter.clear()
                        binding.appsInclude.loadLayout.visibility = ViewGroup.GONE//隐藏加载进度条页面
                        mChatRoomListAdapter.addData(it.data.chatList)
                    }

                    is Result.Error -> {
                        binding.appsInclude.loadProgressBar.visibility = ViewGroup.GONE
                        binding.appsInclude.loadError.visibility = ViewGroup.VISIBLE
                        binding.appsInclude.loadText.text =
                            "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                        binding.appsInclude.loadLayout.isEnabled = true
                    }

                    is Result.Loading -> {

                    }

                    else -> {}
                }
            }
        }

        //跳转的网页
        lifecycleScope.launch {
            viewModel.appsFlow.collect {
                when (it) {
                    is Result.Success -> {
                        mPicaAppsAdapter.clear()
                        binding.appsInclude.loadLayout.visibility = ViewGroup.GONE//隐藏加载进度条页面
                        mPicaAppsAdapter.addData(it.data.apps)
                    }

                    is Result.Error -> {
                        val message = it.message
                        // TODO: 处理请求失败的错误信息
                        binding.appsInclude.loadProgressBar.visibility = ViewGroup.GONE
                        binding.appsInclude.loadError.visibility = ViewGroup.VISIBLE
                        binding.appsInclude.loadText.text =
                            "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                        binding.appsInclude.loadLayout.isEnabled = true
                    }

                    is Result.Loading -> {
                        // TODO: 处理请求正在加载中的状态
                    }

                    else -> {}
                }
            }

        }

    }
}