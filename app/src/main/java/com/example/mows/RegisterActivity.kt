package com.example.mows

import android.content.Context
import android.content.Intent
import android.os.Build.ID
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

        /*
            DB에 접근하기 위하여 helper(DBHelper) 선언
         */
        val helper by lazy{
            DBHelper(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*
            checkPassword()함수가 True를 반환하면
            DBHelper 클래스의 checkAccount()함수에 ID를 넣어서 실행.
            True를 반환할 시에는 DB에 해당 아이디가 존재한다는 뜻.
            ID가 이미 있으면 Toast 메시지로 이미 존재한다고 띄워줌.

            False를 반환하면 DB에 해당 아이디가 없다는 뜻이므로,
            DBHelper 클래스의 addAcccount()함수에 ID와 패스워드를 담아 실행.
            그 후 Toast 메시지로 회원가입이 완료되었다고 띄워줌.

         */

        btRegisterRegi.setOnClickListener {
            var fine = checkPassword()
            if(fine == true){
                intent = Intent(this,MainActivity::class.java)
                if(helper.checkAccount(etIdRegi.text.toString())){
                    Toast.makeText(this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show()
                }else{
                    helper.addAccount(etIdRegi.text.toString(),etPasswordRegi.text.toString())
                    Toast.makeText(this,"회원가입 완료",Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }



            }
        }
    }

    /*
        비밀번호를 입력하는 EditText의 두 text 값이 동일한지,
        비밀번호가 너무 짧지는 않은지 검사하는 함수
     */

    fun checkPassword():Boolean{

        if (etPasswordRegi.text.toString() != etPassword2Regi.text.toString()){
            Toast.makeText(this,"비밀번호를 확인해주세요",Toast.LENGTH_LONG).show()
            return false
        }
        else if (etPasswordRegi.text.toString().length < 4 ){
            Toast.makeText(this,"비밀번호가 너무 짧습니다",Toast.LENGTH_LONG).show()
            return false
        }else{
            return true
        }
    }

}
