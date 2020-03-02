package com.cis.todoapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.cis.todoapplication.com.cis.todoapplication.ItemRowListner

class TODOAdapter(context:Context,itemList:MutableList<Todoitem>) :BaseAdapter() {
    var items = itemList
    val mInflater = LayoutInflater.from((context))
    var rowListner =context as ItemRowListner

    override fun getView(position: Int, convertView: View?, parent:  ViewGroup?): View {
        val objID = items.get(position).objID as String
        val todoName = items.get(position).todoName as String
        val status = items.get(position).status as Boolean
        val view:View
        val vh :ListRowHolder
        if(convertView==null){
            view = mInflater.inflate(R.layout.list_itrm,parent,false)
            vh = ListRowHolder(view)
            view.tag = vh
        }else{
            view =convertView
            vh =view.tag as ListRowHolder
        }
        vh.label.text =todoName
        vh.checkBox.isChecked = status
        vh.checkBox.setOnClickListener{
            rowListner.modifyItemState(objID,position,status )
        }
        return view
    }

    private class ListRowHolder(row:View){
        val label = row!!.findViewById<TextView>(R.id.textView)
        val checkBox = row!!.findViewById<TextView>(R.id.checkBox)as CheckBox
        val deleteButton = row!!.findViewById<TextView>(R.id.imageButton)

    }

    override fun getItemId(p0: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}