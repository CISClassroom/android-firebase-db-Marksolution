package com.cis.todoapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.cis.todoapplication.com.cis.todoapplication.ItemRowListner
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition

class MainActivity : AppCompatActivity(),ItemRowListner {
    override fun OnItemDelete(itemID: String, index: Int ) {
        todoItemList!!.removeAt((index))
        adapter.notifyDataSetChanged()
        val itemRef =mDB.child("TODO_item").child(itemID)
        itemRef.removeValue()
    }

    override fun modifyItemState(itemID: String, index: Int, status: Boolean) {
        todoItemList!!.get(index).status =status
        adapter.notifyDataSetChanged()
        val itemRef =mDB.child("TODO_item").child(itemID)
        itemRef.child("status").setValue(status)
    }

    lateinit var  mDB: DatabaseReference
    var todoItemList: MutableList<Todoitem>? = null
    lateinit var  adapter: TODOAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mDB = FirebaseDatabase.getInstance().reference
        var listviewitem : ListView = findViewById<View>(R.id.items_list)as ListView


        todoItemList = mutableListOf<Todoitem>()
        adapter = TODOAdapter(this,todoItemList!!)
        listviewitem!!.setAdapter(adapter)
        mDB.orderByKey().addListenerForSingleValueEvent(itemListener)

        fab.setOnClickListener { view ->
            addNewTODOItem()
        }
    }
    var itemListener:ValueEventListener = object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(p0: DataSnapshot) {
            addDataToList()

        }
    }
    private fun addDataToList(dataSnapshot: DataSnapshot){
        val items = dataSnapshot.children.iterator()
        if(items.hasNext()){
            val todoListIndex = items.next()
            val itemsIterator =todoListIndex.children.iterator()

            while (itemsIterator.hasNext()){
                val currentItem = itemsIterator.next()
                val map = currentItem.getValue()as HashMap<String,Any>
                val todoitem = Todoitem.create()
                todoitem.objID =currentItem.key
                todoitem.status = map.get("todoName")as String
                todoItemList!!.add(todoitem)
            }
        }
    }
    override fun getItem(position: Int):Any{
        return items.get(position)
    }

    private fun addNewTODOItem() {
        val dialog = AlertDialog.Builder(this)
        var et = EditText(this)

        dialog.setMessage("Add new TODO")
        dialog.setTitle("Enter TODO item")
        dialog.setView(et)

        dialog.setPositiveButton("Submit"){
            dialog,positiveButton ->
                val newTODO = Todoitem.create()
                newTODO.todoName =et.text.toString()
                newTODO.status =false
                mDB = FirebaseDatabase.getInstance().reference
                val newItemDB = mDB.child("TODO_item").push()
                newTODO.objID = newItemDB.key
                newItemDB.setValue(newTODO)
                dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
