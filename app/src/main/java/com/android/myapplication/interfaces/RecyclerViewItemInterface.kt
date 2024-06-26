package com.android.myapplication.interfaces

interface RecyclerViewItemInterface {
    fun OnItemClick(position: Int)
    fun OnItemBookMarkClick(position: Int,id:Int)
    fun OnItemUnBookMarkClick(position: Int,id:Int)
}
