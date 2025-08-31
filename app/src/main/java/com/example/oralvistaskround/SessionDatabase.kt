package com.example.oralvistaskround

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SessionDatabase(context: Context) :
    SQLiteOpenHelper(context, "sessions.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE sessions (
                session_id TEXT PRIMARY KEY,
                name TEXT,
                age INTEGER,
                photo_paths TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS sessions")
        onCreate(db)
    }

    fun insertOrUpdateSession(sessionId: String, name: String, age: Int, photoPaths: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("session_id", sessionId)
            put("name", name)
            put("age", age)
            put("photo_paths", photoPaths)
        }
        db.insertWithOnConflict("sessions", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun sessionExists(sessionId: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM sessions WHERE session_id = ?", arrayOf(sessionId))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun getSession(sessionId: String): Session? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM sessions WHERE session_id = ?",
            arrayOf(sessionId)
        )

        var session: Session? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow("session_id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))
            val photoPathsStr = cursor.getString(cursor.getColumnIndexOrThrow("photo_paths"))
            val photoPaths = photoPathsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            session = Session(id, name, age, photoPaths)
        }
        cursor.close()
        return session
    }
}
