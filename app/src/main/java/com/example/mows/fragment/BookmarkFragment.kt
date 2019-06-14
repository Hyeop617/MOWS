package com.example.mows.fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mows.DBHelper
import com.example.mows.NewsAdapter

import com.example.mows.R
import kotlinx.android.synthetic.main.fragment_bookmark.*


class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    /*
        DB안에 저장된 북마크를 보여줘야 하기 떄문에, DBHelper 호출.
        북마크 테이블안의 값을 ArrayList로 저장.(Adapter에 담길 ArrayList)
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("ID",null)

        val helper= DBHelper(requireContext())
        val mList = helper.selectBookmark(id)
        val newsAdapter = NewsAdapter(mList,requireContext(),id)
        newsAdapter.itemClick = object:NewsAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mList[position].link))
                startActivity(intent)
            }
        }
        // 북마크에 저장된 항목이 있을시에만 어댑터 설정하도록 함.(없을 시 보여줄 것이 없기 때문에..)
        if(mList.size > 0){
            recycleViewBookmark.adapter = newsAdapter
            recycleViewBookmark.layoutManager = LinearLayoutManager(requireContext())
        }

    }


}
