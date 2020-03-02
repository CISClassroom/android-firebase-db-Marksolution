package com.cis.todoapplication

class Todoitem {
    companion object Factory{
        fun create():Todoitem= Todoitem()
    }
    var objID:String? = null
    var todoName:String? = null
    var status:Boolean?=null


}
