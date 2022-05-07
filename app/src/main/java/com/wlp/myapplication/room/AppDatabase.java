package com.wlp.myapplication.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.wlp.myapplication.room.daos.BookDao;
import com.wlp.myapplication.room.daos.UserDao;

@Database(entities = {User.class, Book.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract BookDao bookDao();

}