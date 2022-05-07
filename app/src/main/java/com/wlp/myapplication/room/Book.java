package com.wlp.myapplication.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * 上述代码通过foreignKeys之后Book表中的userId来源于User表中的id。
 *
 * @ForeignKey属性介绍： entity：parent实体类(引用外键的表的实体)。
 * <p>
 * parentColumns：parent外键列(要引用的外键列)。
 * <p>
 * childColumns：child外键列(要关联的列)。
 * <p>
 * onDelete：默认NO_ACTION，当parent里面有删除操作的时候，child表可以做的Action动作有：
 * 1. NO_ACTION：当parent中的key有变化的时候child不做任何动作。
 * 2. RESTRICT：当parent中的key有依赖的时候禁止对parent做动作，做动作就会报错。
 * 3. SET_NULL：当paren中的key有变化的时候child中依赖的key会设置为NULL。
 * 4. SET_DEFAULT：当parent中的key有变化的时候child中依赖的key会设置为默认值。
 * 5. CASCADE：当parent中的key有变化的时候child中依赖的key会跟着变化。
 * <p>
 * onUpdate：默认NO_ACTION，当parent里面有更新操作的时候，child表需要做的动作。Action动作方式和onDelete是一样的。
 * <p>
 * deferred：默认值false，在事务完成之前，是否应该推迟外键约束。这个怎么理解，当我们启动一个事务插入很多数据的时候，事务还没完成之前。当parent引起key变化的时候。可以设置deferred为ture。让key立即改变。
 **/
@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id"))
public class Book {
    @PrimaryKey
    public int bookId;

    public String title;

    @ColumnInfo(name = "user_id")
    public int userId;
}