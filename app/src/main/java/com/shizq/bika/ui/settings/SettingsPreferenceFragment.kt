package com.shizq.bika.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shizq.bika.R
import com.shizq.bika.network.base.BaseHeaders
import com.shizq.bika.utils.SPUtil

// TODO 没有bug 以后再优化 懒
class SettingsPreferenceFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        val setting_close: Preference? = findPreference("setting_close")//清理缓存
        val setting_app_channel: Preference? = findPreference("setting_app_channel")//分流节点
        val setting_punch: Preference? = findPreference("setting_punch")//自动打卡
        val setting_night: Preference? = findPreference("setting_night")//夜间模式
        val setting_app_ver: Preference? = findPreference("setting_app_ver")//应用版本
        val setting_change_password: Preference? =
            findPreference("setting_change_password")//修改密码
        val setting_exit: Preference? = findPreference("setting_exit")//账号退出

        setting_close?.onPreferenceClickListener = this
        setting_app_channel?.onPreferenceChangeListener = this
        setting_punch?.onPreferenceChangeListener = this
        setting_night?.onPreferenceChangeListener = this
        setting_app_ver?.onPreferenceClickListener = this
        setting_change_password?.onPreferenceClickListener = this
        setting_exit?.onPreferenceClickListener = this

        //自动打卡
        setting_punch as SwitchPreferenceCompat
        setting_punch.summary = if (setting_punch.isChecked) "开启" else "关闭"

        //夜间模式
        setting_night as DropDownPreference
        setting_night.summary = setting_night.value

        //分流
        setting_app_channel?.summary =
            when (SPUtil.get("setting_app_channel", "2") as String) {
                "1" -> "分流一"
                "2" -> "分流二"
                else -> "分流三"
            }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "setting_close" -> {
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle("是否清理图片缓存？")
                        .setPositiveButton("确定") { dialog, which ->
                            preference.summary = "0.0Byte"
                            Toast.makeText(activity, "清理完成", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("取消", null)
                        .show()

                }
                return true
            }

            "setting_app_ver" -> {
                checkUpdates()
                return true
            }

            "setting_change_password" -> {

                activity?.let {
                    val dia = MaterialAlertDialogBuilder(it)
                        .setTitle("修改密码")
                        .setView(R.layout.view_dialog_edit_text_change_password)
                        .setCancelable(false)
                        .setPositiveButton("修改", null)
                        .setNegativeButton("取消", null)
                        .show();//在按键响应事件中显示此对话框 }
                    dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        dia as AlertDialog
                        val newPasswordLayout: TextInputLayout? =
                            dia.findViewById(android.R.id.icon1)
                        val confirmPasswordLayout: TextInputLayout? =
                            dia.findViewById(android.R.id.icon2)
                        val newPassword: TextInputEditText? = dia.findViewById(android.R.id.text1)
                        val confirmPassword: TextInputEditText? =
                            dia.findViewById(android.R.id.text2)

                        newPassword?.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable) {
                                newPasswordLayout?.isErrorEnabled = false
                            }
                        })
                        confirmPassword?.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable) {
                                confirmPasswordLayout?.isErrorEnabled = false
                            }
                        })
                        if (newPassword?.text.toString().trim().isEmpty()) {
                            newPasswordLayout?.isErrorEnabled = true
                            newPasswordLayout?.error = "新密码不能为空！"
                        } else if (newPassword?.text.toString().trim().length < 8) {
                            newPasswordLayout?.isErrorEnabled = true
                            newPasswordLayout?.error = "密码不能小于8字！"
                        }
                        if (confirmPassword?.text.toString().trim().isEmpty()) {
                            confirmPasswordLayout?.isErrorEnabled = true
                            confirmPasswordLayout?.error = "确认密码不能为空！"
                        } else if (confirmPassword?.text.toString()
                                .trim() != newPassword?.text.toString().trim()
                        ) {
                            confirmPasswordLayout?.isErrorEnabled = true
                            confirmPasswordLayout?.error = "确认密码与新密码不符！"
                        }
                        if (confirmPassword?.text.toString().trim().isNotEmpty()
                            && newPassword?.text.toString().trim().isNotEmpty()
                            && newPassword?.text.toString().trim().length >= 8
                            && (confirmPassword?.text.toString().trim()
                                    == newPassword?.text.toString().trim())
                        ) {
                            changePassword("", newPassword?.text.toString().trim())
                            // TODO 添加加载进度条
                            dia.dismiss()
                        }
                    }

                }
                return false
            }

            "setting_exit" -> {
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle("你确定要退出登录吗")
                        .setPositiveButton("确定") { _, _ ->
                            SPUtil.remove("token")
                            SPUtil.remove("chat_token")
                            activity?.finishAffinity()
                        }
                        .setNegativeButton("取消", null)
                        .show()

                }
                return false
            }

        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, value: Any?): Boolean {
        when (preference.key) {
            "setting_punch" -> {
                // TODO 一个小bug 开关会影响toolbar颜色变化
                preference as SwitchPreferenceCompat
                preference.summary = if (value as Boolean) "开启" else "关闭"
                return true
            }
            "setting_app_channel" -> {
                value as String
                preference as DropDownPreference
                preference.value = value
                preference.summary =
                    when (value) {
                        "1" -> "分流一"
                        "2" -> "分流二"
                        else -> "分流三"
                    }
                return true
            }
            "setting_night" -> {
                value as String
                preference as DropDownPreference
                preference.summary = value
                preference.value = value
                AppCompatDelegate.setDefaultNightMode(
                    when (value) {
                        "开启" -> AppCompatDelegate.MODE_NIGHT_YES
                        "关闭" -> AppCompatDelegate.MODE_NIGHT_NO
                        "跟随系统" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        else -> AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
                return true
            }
        }
        return true
    }

    private fun changePassword(oldpassword: String, password: String) {
        var old = oldpassword
        if (oldpassword == "") {
            old = SPUtil.get("password", "") as String
        }


        val headers = BaseHeaders("users/password", "PUT").getHeaderMapAndToken()
    }

    fun showAlertDialog() {
        activity?.let {
            val dia: AlertDialog = MaterialAlertDialogBuilder(it)
                .setTitle("修改密码失败，请重试")
                .setView(R.layout.view_dialog_edit_text_change_password_old)
                .setCancelable(false)
                .setPositiveButton("修改", null)
                .setNegativeButton("取消", null)
                .show();//在按键响应事件中显示此对话框 }
            dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val oldPasswordLayout: TextInputLayout? = dia.findViewById(R.id.old_password_layout)
                val oldPassword: TextInputEditText? = dia.findViewById(R.id.new_password)
                val newPasswordLayout: TextInputLayout? = dia.findViewById(R.id.old_password)
                val newPassword: TextInputEditText? = dia.findViewById(R.id.confirm_password_layout)
                val confirmPasswordLayout: TextInputLayout? =
                    dia.findViewById(R.id.new_password_layout)
                val confirmPassword: TextInputEditText? = dia.findViewById(R.id.confirm_password)

                oldPassword?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        oldPasswordLayout?.isErrorEnabled = false
                    }
                })

                newPassword?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        newPasswordLayout?.isErrorEnabled = false
                    }
                })

                confirmPassword?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        confirmPasswordLayout?.isErrorEnabled = false
                    }
                })


                if (oldPassword?.text.toString().trim().isEmpty()) {
                    oldPasswordLayout?.isErrorEnabled = true
                    oldPasswordLayout?.error = "旧密码不能为空！"
                } else if (oldPassword?.text.toString().trim().length < 8) {
                    oldPasswordLayout?.isErrorEnabled = true
                    oldPasswordLayout?.error = "密码不能小于8字！"
                }

                if (newPassword?.text.toString().trim().isEmpty()) {
                    newPasswordLayout?.isErrorEnabled = true
                    newPasswordLayout?.error = "新密码不能为空！"
                } else if (newPassword?.text.toString().trim().length < 8) {
                    newPasswordLayout?.isErrorEnabled = true
                    newPasswordLayout?.error = "密码不能小于8字！"
                }

                if (confirmPassword?.text.toString().trim().isEmpty()) {
                    confirmPasswordLayout?.isErrorEnabled = true
                    confirmPasswordLayout?.error = "确认密码不能为空！"
                } else if (confirmPassword?.text.toString().trim() != newPassword?.text.toString()
                        .trim()
                ) {
                    confirmPasswordLayout?.isErrorEnabled = true
                    confirmPasswordLayout?.error = "确认密码与新密码不符！"
                }

                if (oldPassword?.text.toString().trim().isNotEmpty()
                    && oldPassword?.text.toString().trim().length >= 8
                    && confirmPassword?.text.toString().trim().isNotEmpty()
                    && newPassword?.text.toString().trim().isNotEmpty()
                    && newPassword?.text.toString().trim().length >= 8
                    && (confirmPassword?.text.toString().trim() == newPassword?.text.toString()
                        .trim())
                ) {
                    changePassword(
                        oldPassword?.text.toString().trim(),
                        newPassword?.text.toString().trim()
                    )
                    // TODO 添加加载进度条
                    dia.dismiss()
                }
            }

        }
    }

    fun checkUpdates() {
    }
}