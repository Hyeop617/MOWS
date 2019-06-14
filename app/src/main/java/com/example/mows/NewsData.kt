package com.example.mows

/*
    title : 기사 제목
    link : 기사 url
    company : 기사의 신문사
 */

class NewsData(title:String, link:String,company:String){
    val title:String
    val link:String
    val company:String

    init {
        this.title = title
        this.link = link
        this.company = company
    }

}


