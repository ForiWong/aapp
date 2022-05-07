package com.wlp.myapplication.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.wlp.myapplication.room.entity.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Book... books);

//	@Query("SELECT * from book")
//	Flowable<List<Book>> load();
}
