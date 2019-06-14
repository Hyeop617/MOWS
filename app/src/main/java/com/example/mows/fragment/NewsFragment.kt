package com.example.mows.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mows.NewsAdapter
import com.example.mows.NewsData
import com.example.mows.R
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import kotlin.collections.ArrayList

/*
    코루틴을 사용하기 위하여 uiDispatcher와 dataProvider 선언
 */


class NewsFragment : Fragment() {

    val uiDispatcher: CoroutineDispatcher =Dispatchers.Main
    val dataProvider = DataProvider()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            MyPref 파일(SharedPreference)안의 ID 값을 받아서 id 에 저장.
            그리고 설정(com.example.mows_preferences) 파일을 열어 gender와 age 값을 가져옴.

            그 후 loadData(id)를 실행 해 코루틴 실행.
         */


        val id = requireActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE).getString("ID",null)
        val prefs = requireActivity().getSharedPreferences("com.example.mows_preferences",Context.MODE_PRIVATE)
        var gender = prefs.getString("list_preference_1","1")
        var age = prefs.getString("list_preference_2","1")
        if(gender == "1")
            tvGender.text = "남자"
        else tvGender.text = "여자"
        if(age == "1") tvAge.text = "20대 이하"
        else if(age == "2") tvAge.text = "30대"
        else if(age == "3") tvAge.text = "40대"
        else tvAge.text = "50대 이상"

        loadData(id)

    }

    fun loadData(id:String) = GlobalScope.launch(uiDispatcher){
        showLoading()
        val age = tvAge.text.toString()
        val gender = tvGender.text.toString()
        val resultList = dataProvider.loadData(age,gender)
        showText(resultList,id)
        hideLoading()
    }



    // 로딩바 보여주는 함수.
    fun showLoading(){
        progressBar.visibility= View.VISIBLE
    }
    // 로딩바 숨겨주는 함수
    fun hideLoading(){
        progressBar.visibility = View.GONE
    }

    /*
        RecyclerView를 사용하기 때문에, RecyclerViewAdapter 설정.
        Adapter에 보여줄 뉴스가 들어있는 ArrayList와 context, 그리고 사용자의 id를 같이 넘겨주었음.
        그리고 클릭 이벤트 발생시 NewsAdapter에서 선언한 onClick을 상속받아,
        클릭된 항목의 link를 브라우저로 띄워줌.
     */
    fun showText(mList: ArrayList<NewsData>,id:String){
        val newsAdapter = NewsAdapter(mList,requireContext(),id)
        newsAdapter.itemClick = object:NewsAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mList[position].link))
                startActivity(intent)
            }
        }
        reclycerView.adapter = newsAdapter
        reclycerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /*
        JSoup를 이용해서 크롤링 하는 클래스
        처음 SSL을 위해 UnSafeOKHttp.setSSL() 실행.
        그리고 다음 뉴스의 연령대, 성별별로 많이 본 뉴스 상위 5개를 크롤링 해서,
        Arraylist<NewsData>에 title, link, company (제목, url, 신문사)값을 선택해 넣어주었음.


     */

    class DataProvider(private val dispatcher: CoroutineDispatcher = Dispatchers.IO){
        var url = "https://media.daum.net/ranking/age/"

        suspend fun loadData(age:String,gender:String): ArrayList<NewsData> = withContext(dispatcher){
            UnSafeOKHttp.setSSL()

            val document = Jsoup.connect(url).get()

            val elements : Elements = document.select("div.rank_news")

            val mlist = ArrayList<NewsData>()
            val element:Element = elements.first()
            for(i in 1..5){
                if(age.equals("20대 이하")){
                    if(gender.equals("남자"))
                        mlist.add(NewsData(element.select("div.item_age.item_20s > div.rank_male > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_20s > div.rank_male > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_20s > div.rank_male > ol > li.num_news.num${i} > span").text()))
                    else
                        mlist.add(NewsData(element.select("div.item_age.item_20s > div.rank_female > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_20s > div.rank_female > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_20s > div.rank_female > ol > li.num_news.num${i} > span").text()))
                }else if(age.equals("30대")){
                    if(gender.equals("남자"))
                        mlist.add(NewsData(element.select("div.item_age.item_30s > div.rank_male > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_30s > div.rank_male > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_30s > div.rank_male > ol > li.num_news.num${i} > span").text()))
                    else
                        mlist.add(NewsData(element.select("div.item_age.item_30s > div.rank_female > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_30s > div.rank_female > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_30s > div.rank_female > ol > li.num_news.num${i} > span").text()))
                }else if(age.equals("40대")){
                    if(gender.equals("남자"))
                        mlist.add(NewsData(element.select("div.item_age.item_40s > div.rank_male > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_40s > div.rank_male > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_40s > div.rank_male > ol > li.num_news.num${i} > span").text()))
                    else
                        mlist.add(NewsData(element.select("div.item_age.item_40s > div.rank_female > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_40s > div.rank_female > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_40s > div.rank_female > ol > li.num_news.num${i} > span").text()))
                }else if(age.equals("50대 이상")){
                    if(gender.equals("남자"))
                        mlist.add(NewsData(element.select("div.item_age.item_50s > div.rank_male > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_50s > div.rank_male > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_50s > div.rank_male > ol > li.num_news.num${i} > span").text()))
                    else
                        mlist.add(NewsData(element.select("div.item_age.item_50s > div.rank_female > ol > li.num_news.num${i} > a").text(),
                            element.select("div.item_age.item_50s > div.rank_female > ol > li.num_news.num${i} > a").attr("href"),
                            element.select("div.item_age.item_50s > div.rank_female > ol > li.num_news.num${i} > span").text()))
                }
            }

            mlist
        }


    }

    /*
        Http로 연결하였을 때는 필요가 없으나,
        Https로 연결시 SSL 인증서 문제가 발생하였으므로
        SSL 인증서 관련문제 해결 위해서 사용하였음.
     */
    class UnSafeOKHttp{
        companion object {
            fun setSSL() {
                val trustAllCerts = arrayOf<TrustManager> (object:X509TrustManager{
                    override fun getAcceptedIssuers(): Array<X509Certificate>? {
                        return arrayOf()
                    }

                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }
                })

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null,trustAllCerts,java.security.SecureRandom())
                HttpsURLConnection.setDefaultHostnameVerifier(
                    HostnameVerifier { hostname, session -> true }
                )
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            }
        }

    }






}
