package com.shizq.bika.ui.chatroom.current.blacklist

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shizq.bika.BR
import com.shizq.bika.R
import com.shizq.bika.adapter.ChatRoomBlackListAdapter
import com.shizq.bika.base.BaseActivity
import com.shizq.bika.databinding.ActivityChatRoomBlacklistBinding
import com.shizq.bika.network.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 推荐
 */

class ChatBlacklistActivity :
    BaseActivity<ActivityChatRoomBlacklistBinding, ChatBlacklistViewModel>() {
    private lateinit var mChatBlackListAdapter: ChatRoomBlackListAdapter

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_chat_room_blacklist
    }

    override fun initVariableId(): Int {
        return 1
    }

    override fun initData() {
        binding.blacklistInclude.toolbar.title = "哔咔聊天室黑名单"
        setSupportActionBar(binding.blacklistInclude.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mChatBlackListAdapter = ChatRoomBlackListAdapter()
        binding.blacklistRv.layoutManager = LinearLayoutManager(this)
        binding.blacklistRv.adapter = mChatBlackListAdapter


        if (mChatBlackListAdapter.itemCount == 0) {
            showProgressBar(true, "")
            viewModel.getChatBlackList()
        }
//
//        //网络重试点击事件监听
        binding.blacklistLoadLayout.setOnClickListener {
            showProgressBar(true, "")
            viewModel.getChatBlackList()

        }

        binding.blacklistRv.setOnItemChildClickListener { view, position ->
            if (view.id == R.id.chat_blacklist_delete) {
                binding.blacklistLoadLayout.visibility = ViewGroup.VISIBLE
                showProgressBar(true, "")
                viewModel.deleteChatBlackList(mChatBlackListAdapter.getItemData(position).id)
            }
        }
        binding.blacklistRv.setOnLoadMoreListener { }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewObservable() {
//        viewModel.liveDataBlackList.observe(this) {
//            if (it.limit != 0) {
//                binding.blacklistLoadLayout.visibility = ViewGroup.GONE
//                mChatBlackListAdapter.clear()
//                mChatBlackListAdapter.addData(it.docs)
//
////                if (it.docs.pages <= it.data.comics.page) {
//                binding.blacklistRv.loadMoreEnd()//没有更多数据
////                } else {
////                    binding.comiclistRv.loadMoreComplete()//加载成功
////                }
//
//            } else {
//                showProgressBar(
//                    false,
//                    "网络错误，点击重试\ncode=${it.statusCode} error=${it.error} message=${it.message}"
//                )
//            }
//        }

//        viewModel.liveDataBlackListDelete.observe(this) {
//            if (it.id != null && it.id != "") {
//                binding.blacklistLoadLayout.visibility = ViewGroup.GONE
//                for (i in 0 until mChatBlackListAdapter.data.size) {
//                    if (it.id == mChatBlackListAdapter.getItemData(i).id) {
//                        mChatBlackListAdapter.data.remove(mChatBlackListAdapter.getItemData(i))
//                        mChatBlackListAdapter.notifyItemRemoved(i)
//                        break
//                    }
//                }
//
//
//            } else {
//                showProgressBar(
//                    false,
//                    "网络错误，点击重试\ncode=${it.statusCode} error=${it.error} message=${it.message}"
//                )
//            }
//        }
        lifecycleScope.launch {
            viewModel.blackListFlow.collect() {
                when (it) {
                    is Result.Success -> {
                        binding.blacklistLoadLayout.visibility = ViewGroup.GONE
                        mChatBlackListAdapter.clear()
                        mChatBlackListAdapter.addData(it.data.docs)

//                if (it.docs.pages <= it.data.comics.page) {
                        binding.blacklistRv.loadMoreEnd()//没有更多数据
//                } else {
//                    binding.comiclistRv.loadMoreComplete()//加载成功
//                }
                    }

                    is Result.Error -> {
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
        lifecycleScope.launch {
            viewModel.blackListDeleteFlow.collect() {
                when (it) {
                    is Result.Success -> {
                        binding.blacklistLoadLayout.visibility = ViewGroup.GONE
                        for (i in 0 until mChatBlackListAdapter.data.size) {
                            if (it.data.id == mChatBlackListAdapter.getItemData(i).id) {
                                mChatBlackListAdapter.data.remove(
                                    mChatBlackListAdapter.getItemData(
                                        i
                                    )
                                )
                                mChatBlackListAdapter.notifyItemRemoved(i)
                                break
                            }
                        }
                    }

                    is Result.Error -> {
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

    private fun showProgressBar(show: Boolean, string: String = "") {
        binding.blacklistLoadProgressBar.visibility =
            if (show) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.blacklistLoadError.visibility = if (show) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.blacklistLoadText.text = string
        binding.blacklistLoadLayout.isEnabled = !show
    }
}