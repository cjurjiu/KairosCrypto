package com.catalinj.cryptosmart.datastorage.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log

/**
 * Created by catalin on 05.02.18.
 */
class DummyProvider : ContentProvider() {

    private lateinit var c: Cursor

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        return Uri.EMPTY
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        return c
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(): Boolean {
        Log.d("Cata", "Provider loaded")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 0
    }


    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 0
    }

    override fun getType(uri: Uri?): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return "rofl"
    }
}