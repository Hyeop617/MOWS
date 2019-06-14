package com.example.mows

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.coroutines.coroutineContext

class NewsAdapter(mList:ArrayList<NewsData>,context:Context,id:String): RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    var mList = ArrayList<NewsData>()
    var context:Context? = null             // Toast 메세지를 띄워주기 위해 context 값을 저장
    var id :String? = null
    var helper: DBHelper? = null            // DB에 접근하기 위해 DBHelper 가 필요 하였음.

    /*
        onClick 메소드를 인터페이스로 선언(나중에 재사용 위함)
     */
    interface ItemClick{
        fun onClick(view:View, position:Int)
    }
    var itemClick : ItemClick? = null

    init {
        this.mList = mList
        this.context = context
        this.id = id
    }
    /*
        어댑터가 생성될 때, DBHelper도 같이 생성.
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        Log.d("RECYCLE","onCreateViewHolder")
        helper = DBHelper(context!!)
        val adapterView = LayoutInflater.from(p0.context).inflate(R.layout.list_item,p0,false)
        return ViewHolder(adapterView)
    }

    override fun getItemCount(): Int {
        println("mList.size : ${mList.size}")
        return mList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if(itemClick != null){
            p0?.itemView?.setOnClickListener{ v->
                itemClick?.onClick(v,p1)
            }
        }
        p0.bind(mList[p1],helper!!,id!!,context!!)
    }
     /*
        NewsData(리스트에 보여지는 아이템)에 항목의 값을 할당하는 메소드.
        북마크 버튼을 누를 시에 onClickListener도 같이 설정 하였음.
        북마크 버튼을 누를 시, DBHelper내의 insertBookmark 메소드 실행.
                                        (Bookmark를 insert,delete 해주는 메소드)
      */
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(  itemView){

        fun bind(newsData : NewsData,helper : DBHelper,id:String,context:Context){
            itemView.tvNewsSubject.text = newsData.title
            itemView.tvNewsCompany.text = newsData.company
            itemView.btBookmark.setOnClickListener {
                val add : Boolean = helper.insertBookmark(newsData.title,newsData.link,newsData.company,id)
//                helper.insertBookmark(newsData.title,newsData.link,newsData.company,id)
                if(add){
                    Toast.makeText(context,"북마크 등록됨",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"북마크 해제됨",Toast.LENGTH_SHORT).show()
                }


            }

        }


    }

}
