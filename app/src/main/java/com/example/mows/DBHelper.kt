package com.example.mows

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// db의 이름은 MOWS.db로 설정.
// onUpgrade는 쓰지 않았음.(항목 값이 바뀔 일이 없으므로)
class DBHelper(context: Context):SQLiteOpenHelper(context, "MOWS.db",null,1){
    // Helper 호출 시 account 라는 테이블 생성. ( 사용자들의 ID 와 PW가 적힌 테이블)
    // 이미 생성된 경우에는 생략하게끔 하였음
    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "create table if not exists account (_id integer PRIMARY KEY autoincrement, id text, password text)"
        db!!.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /*
        사용자들의 ID와 PW가 적힌 테이블 (account)에
        새로운 사용자의 ID와 PW를 집어 넣는 함수.
     */
    fun addAccount(id:String, pw:String){
        val database = this.writableDatabase
        var sql = "insert into account(id,password) values(?,?)"
        val params =arrayOf(id,pw)
        database.execSQL(sql,params)
    }
    /*
        해당 ID가 존재하는지 검사하는 함수.
        이미 해당 ID가 DB 내에 저장되어 있으면 True를 반환.
     */

    fun checkAccount(id:String): Boolean{
        var exist:Boolean = false
        val database = this.readableDatabase
        val sql = "select id from account"
        val cursor = database.rawQuery(sql,null)

        for(i in 0 until cursor.count){
            cursor.moveToNext()
            val idCursor = cursor.getString(0)
            if(id.equals(idCursor))
                exist = true
        }
        cursor.close()
        return exist
    }
    /*
        해당 ID와 PW가 DB 내에 존재하는지 검사해주는 함수
        DB내에 존재하면(사용자가 정상적으로 로그인 시도) True를 반환
     */
    fun checkLogin(id:String, pw:String):Boolean {
        var found = false;
        val database = this.readableDatabase
        val cursor = database.rawQuery("select id, password from account", null)

        for (i in 0 until cursor.count) {
            cursor.moveToNext()
            var idCursor = cursor.getString(0)
            var pwCursor = cursor.getString(1)

            if (id.equals(idCursor) && pw.equals(pwCursor))
                found = true
        }

        return found
    }
    /*
        DB내에서 북마크를 insert, delete를 하는 함수.
        처음 account_id(해당 id의 북마크가 저장된 테이블)이 없을 시 생성.

        checkBookmark()가 True일 시,
            (parameter로 받은 title, link, company가 존재(이미 해당 뉴스가 DB내에 존재)
        DB에서 삭제해주는 delete sql문을 실행

        checkBookmark()가 False일 시,
            (parameter로 받은 title, link, company가 존재하지 않음(이미 해당 뉴스가 DB내에 존재하지 않음)
        insert sql문을 실행
     */
    fun insertBookmark(title:String, link:String, company:String,id: String): Boolean{
        var add = false
        val database = this.writableDatabase
        var sql = "create table if not exists account_"+ id + " (_id integer PRIMARY KEY autoincrement, title text, link text, company text)"
        database!!.execSQL(sql)
        if(!checkBookmark(title,link,company,id)){
//            Log.d("DB","추가")
            sql = "insert into account_"+id+"(title,link,company) values(?,?,?)"
            val params =arrayOf(title,link,company)
            database!!.execSQL(sql,params)
            add = true
        }else{
//            Log.d("DB","삭제")
            sql = "delete from account_"+id+" where link=\""+link +"\""
            database!!.execSQL(sql)
            add = false
        }

        Log.d("DB","${title+link+company+id}")
        return add
    }


    /*
        Bookmark을 select 해주는 함수.
        Bookmark가 들어있는 테이블의 값을 모두 읽어와,
        ArrayList로 반환
     */

    fun selectBookmark(id:String): ArrayList<NewsData>{
        val mList = ArrayList<NewsData>()
        val database = this.readableDatabase
        var sql = "select title, link, company from account_" + id
        val cursor = database.rawQuery(sql,null)
        for(i in 0 until cursor.count){
            cursor.moveToNext()
            mList.add(NewsData(cursor.getString(0),cursor.getString(1),cursor.getString(2)))
        }
        cursor.close()
        return mList
    }

    /*
        News의 title과 link, company의 값이
        account_id(해당 아이디의 북마크가 들은 테이블)에 존재하는지 검사하는 함수.
        존재 한다면 True를 반환
     */

    fun checkBookmark(title:String, link:String, company:String,id: String):Boolean{
        var found : Boolean = false
        val database = this.readableDatabase
        var sql = "select title, link, company from account_" + id
        val cursor = database.rawQuery(sql,null)
        for(i in 0 until cursor.count){
            cursor.moveToNext()
            var titleCursor = cursor.getString(0)
            var linkcursor = cursor.getString(1)
            var companyCursor = cursor.getString(2)

            if(title.equals(titleCursor) && link.equals(linkcursor) && company.equals(companyCursor)){
                found = true
            }

        }
        cursor.close()
        return found
    }
}