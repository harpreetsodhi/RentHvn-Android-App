package com.project.renthvn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter

class PageView : PagerAdapter{

    lateinit var con: Context
    lateinit var path: IntArray
    lateinit var inflater: LayoutInflater

    constructor(con: Context, path:IntArray): super(){
        this.con = con
        this.path = path
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object` as LinearLayout
    }

    override fun getCount(): Int {
        return path.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var img: ImageView
        inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rv:View = inflater.inflate(R.layout.swipe_layout, container, false)
        img =rv.findViewById(R.id.img) as ImageView
        img.setImageResource(path[position])
        container!!.addView(rv)
        return rv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as LinearLayout)

    }

}