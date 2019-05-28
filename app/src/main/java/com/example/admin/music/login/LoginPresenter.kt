package com.example.admin.music.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.admin.music.data.LoginData
import com.example.admin.music.network.LoginNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val loginView: LoginContact.View) : LoginContact.Presenter {
    private val tag = javaClass.name

    companion object {
        //    private final String CODE_200 = "Code 200: Login successfully.";
        private const val CODE_400 = "Code 400: Invalid phone number."
        private const val CODE_415 = "Code 415: ip high frequency."
        private const val CODE_501 = "Code 501: Invalid user name."
        private const val CODE_502 = "Code 502: Incorrect password."
        private const val CODE_509 = "Code 509: Too many incorrect password."
        private const val OTHER_ERROR = "Unknown mistake occurred."
        private const val NULL_BODY = "Response body is null"
    }

    override fun login(phone: String, password: String) {
        val context = loginView as Context
        val sharedPreferences = context.getSharedPreferences("login", 0)
        val userName = sharedPreferences.getString("userName", null)
        val id = sharedPreferences.getString("id", null)
        val avatar = sharedPreferences.getString("avatar", null)
        val nickName = sharedPreferences.getString("nickName", null)
        if (id != null && userName != null && avatar != null && nickName != null) {
            if (phone == sharedPreferences.getString(
                    "phone",
                    null
                ) && password == sharedPreferences.getString("password", null)
            ) {
                loginView.startMain(userName, id, avatar, nickName)
            } else {
                Toast.makeText(context, "Invalid phone number or password.", Toast.LENGTH_SHORT).show()
            }
        } else {
            /*I want to get IPAddress dynamically, but I failed.
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            Log.e("TEXT", String.valueOf(ipAddress));*/
            LoginNetwork.loginCall(phone, password).enqueue(object : Callback<LoginData> {
                override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                    val code = response.body()?.code ?: 0
                    loginView.saveMessage(code)
                    when (code) {
                        200 -> {
                            try {
                                loginView.startMain(response.body()!!)
                                val editor = (loginView as Context).getSharedPreferences("login", 0).edit()
                                editor.putString("userName", response.body()!!.account.userName)
                                editor.putString("id", response.body()!!.account.id)
                                editor.putString("avatar", response.body()!!.profile.avatarUrl)
                                editor.putString("nickName", response.body()!!.profile.nickname)
                                editor.apply()
                            } catch (e: NullPointerException) {
                                Toast.makeText(loginView as Context, NULL_BODY, Toast.LENGTH_SHORT).show()
                            }
                        }

                        400 -> Toast.makeText(loginView as Context, CODE_400, Toast.LENGTH_SHORT).show()
                        415 -> Toast.makeText(loginView as Context, CODE_415, Toast.LENGTH_SHORT).show()
                        501 -> Toast.makeText(loginView as Context, CODE_501, Toast.LENGTH_SHORT).show()
                        502 -> Toast.makeText(loginView as Context, CODE_502, Toast.LENGTH_SHORT).show()
                        509 -> Toast.makeText(loginView as Context, CODE_509, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(loginView as Context, OTHER_ERROR, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginData>, t: Throwable) {
                    Log.e(tag, "Login network failure", t)
                    Toast.makeText(context, "Login network failure", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
