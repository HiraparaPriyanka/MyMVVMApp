package com.android.myapplication.adapter.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.android.myapplication.R
import com.android.myapplication.base.BaseAdapter
import com.android.myapplication.interfaces.RecyclerViewItemInterface
import com.android.myapplication.model.AllUserModel
import com.android.myapplication.utils.extentions.click
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_user_item.view.imageViewBookMark
import kotlinx.android.synthetic.main.layout_user_item.view.textViewStarCount
import kotlinx.android.synthetic.main.layout_user_item.view.textViewUserName
import kotlinx.android.synthetic.main.layout_user_item.view.userMain

class UserAdapter(layout: Int) :
    BaseAdapter<AllUserModel>(layout), BaseAdapter.OnBind<AllUserModel> {

    private var viewItemInterface: RecyclerViewItemInterface? = null
    private var mRealm: Realm? = null
    var bookmark:Boolean = false


    init {
        setOnBinding(this)
    }

    fun setItemClickListener(viewItemInterface1: RecyclerViewItemInterface) {
        viewItemInterface = viewItemInterface1
    }

    override fun onBind(holder: ViewHolder, view: View, position: Int, item: AllUserModel) {
        Log.i(TAG, "onBind: " + item.name)
        view.run {
            textViewUserName.text = buildString {
                append("User Name: ")
                append(item.name)
            }
            textViewStarCount.text = buildString {
                append("Star Count: ")
                append(item.stargazersCount.toString())
            }
            userMain.setOnClickListener(View.OnClickListener {
                if (viewItemInterface != null) {
                    viewItemInterface!!.OnItemClick(position)
                    notifyDataSetChanged()
                }
            })
            bookmark = item.bookmark?:false

            if (bookmark) {
                imageViewBookMark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark))
            } else {
                imageViewBookMark.setImageDrawable(context.getDrawable(R.drawable.ic_unbookmark))
            }

            imageViewBookMark.click {
                var id: Int = item.id
                if (item.bookmark == true) {
                    if (viewItemInterface != null) {
                        imageViewBookMark.setImageDrawable(context.getDrawable(R.drawable.ic_unbookmark))
                        viewItemInterface!!.OnItemUnBookMarkClick(position, id)
                        notifyDataSetChanged()
                    }
                } else {
                    if (viewItemInterface != null) {
                        imageViewBookMark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark))
                        viewItemInterface!!.OnItemBookMarkClick(position, id)
                        notifyDataSetChanged()
                    }
                }


            }
        }
    }

}