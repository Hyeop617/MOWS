package com.example.mows

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {

    /*
        SharedPreference를 사용해 설정파일을 저장 하기위하여 사용
     */
    val prefs by lazy {
        getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }

    /*
        사용자의 ID와 비밀번호를 확인하기 위하여 DB에 접근해야함.
        SQLiteOpenHelper를 이용해 DB를 구현하였으므로, DBHelper 클래스를 사용.
        또한 현재 activity(MainActivity)의 context를 보내주었음.
     */
    val helper by lazy {
        DBHelper(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            DBHelper의 checkLogin 함수 호출.
            checkLogin이 True를 반환하면 성공적으로 로그인이 완료된 것.
            로그인이 완료되면 사용자의 ID를 같이 SharedPreference에 저장한 뒤,
            BottomMainActivity를 보여줌.

            False를 반환시,(ID와 비밀번호가 맞는 행을 DB에서 찾지 못했을 시) Toast 메시지를 띄워줌.
         */
        btLogin.setOnClickListener {
            if (helper.checkLogin(etID.text.toString(), etPassword.text.toString())) {
                prefs.edit().putString("ID", etID.text.toString()).apply()
                val intent = Intent(this, BottomMainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "ID와 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
        /*
            회원가입 화면으로 이동.
         */
        btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
