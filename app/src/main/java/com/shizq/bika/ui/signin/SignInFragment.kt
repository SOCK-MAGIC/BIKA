package com.shizq.bika.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shizq.bika.BR
import com.shizq.bika.R
import com.shizq.bika.base.BaseFragment
import com.shizq.bika.databinding.FragmentSigninBinding
import com.shizq.bika.ui.main.MainActivity
import com.shizq.bika.utils.SPUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * 登录
 */
@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSigninBinding, SignInViewModel>() {

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_signin
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        (activity as AppCompatActivity).supportActionBar?.title = "登录"

        binding.clickListener = ClickListener()

        binding.signinBtnSignin.setOnClickListener {
            hideProgressBar(false)
            val email = viewModel.email.get()
            val password = viewModel.password.get()
            if (email.isNullOrBlank() || password.isNullOrBlank()) {
                Toast.makeText(requireContext(), "账号/密码不能为空！", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signIn(
                    email,
                    password,
                    {
                        startActivity(MainActivity::class.java)
                        requireActivity().finish()
                    },
                    {
                        Toast.makeText(requireContext(), it.message ?: "", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            hideProgressBar(true)
        }
    }

    inner class ClickListener {
        fun SignUp() {
            Navigation.findNavController(requireActivity(), R.id.login_fcv)
                .navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        fun Forgot() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("忘记密码")
                .setMessage("请输入需要找回密码的账号")
                .setView(R.layout.view_dialog_edit_text)
                .setPositiveButton("确定") { dialog, which ->
                    val input: TextView? = (dialog as AlertDialog).findViewById(android.R.id.text1)
                    viewModel.forgot_email = input?.text.toString().trim()
                    hideProgressBar(false)
                    viewModel.getForgot()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    override fun initViewObservable() {
        binding.signinPassword.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                // 监听密码输入框回车键
                // 按回车键后进行登录操作
                binding.signinBtnSignin.performClick()
            }
            false
        }

        // 忘记密码 网络请求结果
        viewModel.liveData_forgot.observe(this) {
            hideProgressBar(true)
            if (it.code == 200) {
                val choices =
                    arrayOf<CharSequence>(it.data.question1, it.data.question2, it.data.question3)
                MaterialAlertDialogBuilder(activity as AppCompatActivity)
                    .setTitle("选择回答一条安全问题")
                    .setItems(choices) { var1, p ->
                        MaterialAlertDialogBuilder(activity as AppCompatActivity)
                            .setTitle("安全问题")
                            .setMessage(choices[p])
                            .setView(R.layout.view_dialog_edit_text_answer)
                            .setPositiveButton("确定") { dialog, which ->
                                val input: TextView? =
                                    (dialog as AlertDialog).findViewById(android.R.id.text1)
                                viewModel.forgot_questionNo = (p + 1).toString()
                                viewModel.forgot_answer = input?.text.toString().trim()
                                hideProgressBar(false)
                                viewModel.resetPassword()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                    .show()
            } else {
                Toast.makeText(
                    context,
                    "网络错误code=${it.code} error=${it.error} message=${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 忘记密码回答问题后的 网络请求结果
        viewModel.liveData_password.observe(this) {
            hideProgressBar(true)
            if (it.code == 200) {
                // 获得临时密码成功
                binding.signinUsername.setText(viewModel.forgot_email)
                binding.signinPassword.setText(it.data.password)
                MaterialAlertDialogBuilder(activity as AppCompatActivity)
                    .setTitle("忘记密码")
                    .setMessage("已获得新密码，是否继续进行登录")
                    .setPositiveButton("确定") { dialog, which ->
//                        viewModel.email = viewModel.forgot_email
//                        viewModel.password = it.data.password
                        hideProgressBar(false)
//                        viewModel.signIn()
                    }
                    .setNegativeButton("取消", null)
                    .show()
            } else {
                Toast.makeText(
                    context,
                    "网络错误code=${it.code} error=${it.error} message=${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hideProgressBar(boolean: Boolean) {
        binding.signinProgressBar.visibility = if (boolean) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.signinUsername.isEnabled = boolean
        binding.signinPassword.isEnabled = boolean
        binding.signinBtnSignin.isEnabled = boolean
        binding.signinBtnSignup.isEnabled = boolean
        binding.signinBtnForgot.isEnabled = boolean
    }
}
