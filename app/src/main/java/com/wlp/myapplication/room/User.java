package com.wlp.myapplication.room;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity的实体类都需要添加@Entity注解。而且Entity类中需要映射到表中的字段需要保证外部能访问到这些字段(你要么把字段什么为public、要不实现字段的getter和setter方法)。
 *
 * @Entity注解包含的属性有：
 * tableName：设置表名字。默认是类的名字。
 * indices：设置索引。
 * inheritSuperIndices：父类的索引是否会自动被当前类继承。
 * primaryKeys：设置主键。
 * foreignKeys：设置外键。
 *
 **/
//设置表名、主键、索引
@Entity(tableName = "user", primaryKeys = {"first_name", "last_name"},
        indices = {@Index("firstName"), @Index(value = {"last_name", "address"})})
public class User {
    //设置主键 primaryKeys 或 @PrimaryKey
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")//设置列名
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @Embedded
    public Address address;

    @Ignore //忽略
    Bitmap picture;
}