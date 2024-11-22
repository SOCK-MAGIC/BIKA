package com.shizq.bika.ui.main

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager

import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity
import com.shizq.bika.databinding.ActivityMainBinding
import com.shizq.bika.ui.chatroom.current.roomlist.ChatRoomListActivity
import com.shizq.bika.ui.comiclist.ComicListActivity
import com.shizq.bika.ui.comment.CommentsActivity
import com.shizq.bika.ui.games.GamesActivity
import com.shizq.bika.ui.image.ImageActivity
import com.shizq.bika.ui.mycomments.MyCommentsActivity
import com.shizq.bika.ui.notifications.NotificationsActivity
import com.shizq.bika.ui.settings.SettingsActivity
import com.shizq.bika.ui.user.UserActivity
import com.shizq.bika.utils.GlideUrlNewKey
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.AndroidEntryPoint
import me.jingbin.library.skeleton.ByRVItemSkeletonScreen
import me.jingbin.library.skeleton.BySkeleton

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private lateinit var skeletonScreen: ByRVItemSkeletonScreen

    private val ERROR_PROFILE = 0
    private val ERROR_CATEGORIES = 1
    private var ERROR = this.ERROR_PROFILE // 这里标记哪个网络请求异常

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return 1
    }

    override fun initData() {
        binding.toolbar.title = "哔咔"
        setSupportActionBar(binding.toolbar)

        binding.mainRv.layoutManager = GridLayoutManager(
            this@MainActivity,
            if (getWindowWidth() > getWindowHeight()) 6 else 3
        ) // 初步适配平板（没卵用
        skeletonScreen = BySkeleton
            .bindItem(binding.mainRv)
            .load(R.layout.item_categories_skeleton) // item骨架图
            .duration(2000) // 微光一次显示时间
            .count(18) // item个数
            .show()

        initListener()
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        // 页面显示时调用
        if (isTopResumedActivity) {
            initProfile() // 显示用户信息
        }
    }

    // 显示用户信息
    private fun initProfile() {
        viewModel.fileServer = SPUtil.get("user_fileServer", "") as String
        viewModel.path = SPUtil.get("user_path", "") as String
        // 用户名
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_name)!!
                ).text = SPUtil.get("user_name", "") as String
        // 等级
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_user_ver)!!
                ).text =
            "Lv.${SPUtil.get("user_level", 1) as Int}(${SPUtil.get("user_exp", 0) as Int}/${
                exp(
                    SPUtil.get("user_level", 1) as Int
                )
            })"
        // 性别
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_gender)!!
                ).text =
            when (SPUtil.get("user_gender", "") as String) {
                "m" -> "(绅士)"
                "f" -> "(淑女)"
                else -> "(机器人)"
            }
        // title
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_title)!!
                ).text =
            SPUtil.get("user_title", "萌新") as String
        // 自我介绍 签名
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_slogan)!!
                ).text =
            if (SPUtil.get(
                    "user_slogan",
                    ""
                ) as String == ""
            ) resources.getString(R.string.slogan) else SPUtil.get("user_slogan", "") as String
    }

    override fun onResume() {
        super.onResume()
        binding.mainNavView.setCheckedItem(R.id.drawer_menu_home)
    }

    // toolbar菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_main, menu)
        return true
    }

    private fun initListener() {
        // 侧滑
        binding.drawerLayout.addDrawerListener(
            ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                binding.toolbar,
                R.string.drawer_show,
                R.string.drawer_hide
            )
        )
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_modify)!!
                ).setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_punch_in)!!
                ).setOnClickListener {
                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_punch_in)!!
                        ).visibility = View.GONE
            }
        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<ImageView>(R.id.main_drawer_character)!!
                ).setOnClickListener {
                if (viewModel.userId != "" && viewModel.fileServer != "") {
                    // 判断用户是否登录是否有头像，是就查看头像大图
                    val intent = Intent(this, ImageActivity::class.java)
                    intent.putExtra("fileserver", viewModel.fileServer)
                    intent.putExtra("imageurl", viewModel.path)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        (
                                binding.mainNavView.getHeaderView(0)
                                    .findViewById<ImageView>(R.id.main_drawer_imageView)!!
                                ),
                        "image"
                    )
                    startActivity(intent, options.toBundle())
                }
            }
        binding.mainNavView.setNavigationItemSelectedListener {
            binding.mainNavView.setCheckedItem(it)
            when (it.itemId) {
                R.id.drawer_menu_mail -> {
                    startActivity(NotificationsActivity::class.java)
                }

                R.id.drawer_menu_chat -> {
                    startActivity(MyCommentsActivity::class.java)
                }

                R.id.drawer_menu_settings -> {
                    startActivity(SettingsActivity::class.java)
                }
            }
            true
        }

        binding.mainRv.setOnItemClickListener { _, position ->

        }
        // 网络重试点击事件监听
        binding.mainLoadLayout.setOnClickListener {
            skeletonScreen.show()
            if (ERROR == ERROR_PROFILE) {
                showProgressBar(true, "检查账号信息...")
                viewModel.getProfile()
            } else {
                showProgressBar(true, "获取主页信息...")
                viewModel.getCategories()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        // user信息
        viewModel.liveData_profile.observe(this) {
            if (it.code == 200) {
                var fileServer = ""
                var path = ""
                var character = ""
                viewModel.userId = it.data.user._id

                if (it.data.user.avatar != null) { // 头像
                    fileServer = it.data.user.avatar.fileServer
                    path = it.data.user.avatar.path
                }
                if (it.data.user.character != null) { // 头像框 新用户没有

                    character = it.data.user.character
                }

                val name = it.data.user.name
                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_name)!!
                        ).text = name // 用户名

                val level = it.data.user.level // 等级
                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_user_ver)!!
                        ).text =
                    "Lv.${it.data.user.level}(${it.data.user.exp}/${exp(it.data.user.level)})" // 等级

                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_title)!!
                        ).text = it.data.user.title // 称号

                // 性别
                val gender = it.data.user.gender
                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_gender)!!
                        ).text =
                    when (it.data.user.gender) {
                        "m" -> "(绅士)"
                        "f" -> "(淑女)"
                        else -> "(机器人)"
                    }

                // 用户签名
                val slogan = if (it.data.user.slogan.isNullOrBlank()) "" else it.data.user.slogan
                (
                        binding.mainNavView.getHeaderView(0)
                            .findViewById<TextView>(R.id.main_drawer_slogan)!!
                        ).text =
                    if (slogan == "") resources.getString(R.string.slogan) else slogan

                if (!it.data.user.isPunched) { // 当前用户未打卡时
                    // 是否设置自动打卡
                    if (SPUtil.get("setting_punch", true) as Boolean) {
                    } else {
                        (
                                binding.mainNavView.getHeaderView(0)
                                    .findViewById<TextView>(R.id.main_drawer_punch_in)!!
                                ).visibility = View.VISIBLE
                    }
                }

                // 存一下当前用户信息 用于显示个人评论
                SPUtil.put("user_fileServer", fileServer)
                SPUtil.put("user_path", path)
                SPUtil.put("user_character", character)
                SPUtil.put("user_name", name)
                SPUtil.put("user_birthday", it.data.user.birthday)
                SPUtil.put("user_created_at", it.data.user.created_at)
                SPUtil.put("user_gender", gender)
                SPUtil.put("user_level", level)
                SPUtil.put("user_exp", it.data.user.exp)
                SPUtil.put("user_title", it.data.user.title)
                SPUtil.put("user_slogan", slogan)
                SPUtil.put("user_id", it.data.user._id)
                SPUtil.put("user_verified", it.data.user.verified)

                if (viewModel.cList().size <= 10) {
                    // 更换头像会重新加载个人信息 防止重复加载
                    showProgressBar(true, "获取主页信息...")
                    viewModel.getCategories() // 获得主页信息
                }
            } else if (it.code == 401) {
                if (it.error == "1005") {
                    // token 过期 进行自动登录
                    showProgressBar(true, "账号信息已过期，进行自动登录...")
                    viewModel.getSignIn() // 自动登录 重新获取token
                }
            } else {
                ERROR = this.ERROR_PROFILE
                showProgressBar(
                    false,
                    "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }

        // 加载主页
        viewModel.getCategories()
        viewModel.liveData.observe(this) {
            skeletonScreen.hide()
            if (it.code == 200) {
                binding.mainRv.loadMoreEnd()
                binding.mainLoadLayout.visibility = ViewGroup.GONE
                viewModel.categoriesList = viewModel.cList()
                viewModel.categoriesList.addAll(it.data.categories)
            } else {
                ERROR = this.ERROR_CATEGORIES
                showProgressBar(
                    false,
                    "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }

        // 打卡签到

        (
                binding.mainNavView.getHeaderView(0)
                    .findViewById<TextView>(R.id.main_drawer_punch_in)!!
                ).visibility = View.VISIBLE
    }

    private fun showProgressBar(show: Boolean, string: String) {
        binding.mainLoadProgressBar.visibility = if (show) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.loadCategoriesError.visibility = if (show) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.mainLoadText.text = string
        binding.mainLoadLayout.isEnabled = !show
    }

    private fun exp(i: Int): Int {
        // 等级计算是反编译源码找到的
        return 100 * i * i + (100 * i)
    }

    private fun getWindowHeight(): Int {
        return resources.displayMetrics.heightPixels
    }

    private fun getWindowWidth(): Int {
        return resources.displayMetrics.widthPixels
    }
}
