package com.example.mows

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.mows.fragment.BookmarkFragment
import com.example.mows.fragment.HomeFragment
import com.example.mows.fragment.NewsFragment
import com.example.mows.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_bottom_main.*

class BottomMainActivity : AppCompatActivity() {

    /*
        네비게이선 버튼 선택시 해당 fragement를 띄워줌.
     */



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.ctFrame, HomeFragment()).commit()
            }
            R.id.navigation_news -> {

                supportFragmentManager.beginTransaction().replace(R.id.ctFrame,NewsFragment()).commit()
            }
            R.id.navigation_bookmark -> {
                supportFragmentManager.beginTransaction().replace(R.id.ctFrame,BookmarkFragment()).commit()
            }
            R.id.navigation_setting ->{
                supportFragmentManager.beginTransaction().replace(R.id.ctFrame,SettingFragment()).commit()
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        /*
            시작 화면을 HomeFragement로 설정.
         */
        supportFragmentManager.beginTransaction().replace(R.id.ctFrame, HomeFragment()).commit()
    }


}
