package com.shizq.bika.ui.signin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shizq.bika.R
import com.shizq.bika.base.BaseActivity2
import com.shizq.bika.databinding.FragmentSigninBinding
import com.shizq.bika.ui.main.MainActivity
import com.shizq.bika.ui.signup.SignUpActivity
import com.shizq.bika.utils.bindView

class SignInActivity : BaseActivity2() {
    override val layoutId: Int = R.layout.fragment_signin
    private val binding = bindView<FragmentSigninBinding>(layoutId)
    private val viewModel by viewModels<SignInViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        supportActionBar?.title = "登录"

        binding.signinBtnSignin.setOnClickListener {
            hideProgressBar(false)
            val email = viewModel.email.get()
            val password = viewModel.password.get()
            if (email.isNullOrBlank() || password.isNullOrBlank()) {
                Toast.makeText(this, "账号/密码不能为空！", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signIn(
                    email,
                    password,
                    {
                        startActivity(MainActivity::class.java)
                        this.finish()
                    },
                    {
                        Toast.makeText(this, it.message ?: "", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
            hideProgressBar(true)
        }
        binding.signinBtnSignup.setOnClickListener {
            startActivity(SignUpActivity::class.java,)
        }
        binding.signinBtnForgot.setOnClickListener {
            MaterialAlertDialogBuilder(this)
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

    private fun hideProgressBar(boolean: Boolean) {
        binding.signinProgressBar.visibility = if (boolean) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.signinUsername.isEnabled = boolean
        binding.signinPassword.isEnabled = boolean
        binding.signinBtnSignin.isEnabled = boolean
        binding.signinBtnSignup.isEnabled = boolean
        binding.signinBtnForgot.isEnabled = boolean
    }
}
