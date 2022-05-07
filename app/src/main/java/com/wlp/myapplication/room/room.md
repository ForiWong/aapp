# https://developer.android.google.cn/jetpack/androidx/releases/room

1、简单使用，依赖
2、Entity 数据表，相关的注解
3、Dao数据访问对象，接口文件
4、创建数据库public abstract class MyDatabase extends RoomDatabase
5、进阶：表关联、嵌套、多表查询、数据库升级迁移。

Android Room 框架学习
https://www.jianshu.com/p/3e358eb9ac43

Room是一个对象关系映射(ORM)库。Room抽象了SQLite的使用，可以在充分利用SQLite的同时访问流畅的数据库。

Room官方文档介绍 https://developer.android.com/training/data-storage/room/

Room由三个重要的组件组成：Database、Entity、DAO。

Database：包含数据库持有者，并作为与应用持久关联数据的底层连接的主要访问点。而且Database对应的类必须满足下面几个条件：
1. 必须是abstract类而且的extends RoomDatabase。
2. 必须在类头的注释中包含与数据库关联的实体列表(Entity对应的类)。
3. 包含一个具有0个参数的抽象方法，并返回用@Dao注解的类。
   在运行时，你可以通过Room.databaseBuilder() 或者 Room.inMemoryDatabaseBuilder()获取Database实例。

Entity：代表数据库中某个表的实体类。
每个Entity代表数据库中某个表的实体类。默认情况下Room会把Entity里面所有的字段对应到表上的每一列。  
如果需要制定某个字段不作为表中的一列需要添加@Ignore注解。


DAO：包含用于访问数据库的方法。
一、Entity(实体)  
每个Entity代表数据库中某个表的实体类。默认情况下Room会把Entity里面所有的字段对应到表上的每一列。如果需要制定某个字段不作为表中的一列需要  
添加@Ignore注解。
Entity的实体类都需要添加@Entity注解。而且Entity类中需要映射到表中的字段需要保证外部能访问到这些字段(你要么把字段什么为public、要不实现字段  
的getter和setter方法)。

@Entity注解包含的属性有：
tableName：设置表名字。默认是类的名字。
indices：设置索引。
inheritSuperIndices：父类的索引是否会自动被当前类继承。
primaryKeys：设置主键。
foreignKeys：设置外键。

@ColumnInfo(name = "first_name")

@Entity(primaryKeys = {"firstName",
                       "lastName"})

@PrimaryKey
public String firstName;
	
@Entity(indices = {@Index("firstName"), @Index(value = {"last_name", "address"})})
		
@Entity(foreignKeys = @ForeignKey(entity = User.class,
                                  parentColumns = "id",
                                  childColumns = "user_id"))
								  
@Embedded
public Address address;
		
设置表名、列名、主键、索引、外键，创建嵌套对象。


二、DAO(数据访问对象)
这个组件代表了作为DAO的类或者接口。DAO是Room的主要组件，负责定义访问数据库的方法。Room使用过程中一般使用抽象DAO类来定义数据库的CRUD操作。  
DAO可以是一个接口也可以是一个抽象类。如果它是一个抽象类，它可以有一个构造函数，它将RoomDatabase作为其唯一参数。Room在编译时创建每个DAO实现。

DAO里面所有的操作都是依赖方法来实现的。

2.1、Insert(插入)
当DAO里面的某个方法添加了@Insert注解。Room会生成一个实现，将所有参数插入到数据库中的一个单个事务。

@Insert注解可以设置一个属性：

onConflict：默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。OnConflictStrategy封装了Room解决冲突的相关策略：
1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
3. OnConflictStrategy.ABORT：冲突策略是终止事务。
4. OnConflictStrategy.FAIL：冲突策略是事务失败。
5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。

一个简单的实例如下：

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);
}

当@Insert注解的方法只有一个参数的时候，这个方法也可以返回一个long，当@Insert注解的方法有多个参数的时候则可以返回long[]或者r List<Long>。  
long都是表示插入的rowId。

2.2、Update(更新)
当DAO里面的某个方法添加了@Update注解。Room会把对应的参数信息更新到数据库里面去(会根据参数里面的primary key做更新操作)。

@Update和@Insert一样也是可以设置onConflict来表明冲突的时候的解决办法。

@Dao
public interface UserDao {
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUsers(User... users);
}

@Update注解的方法也可以返回int变量。表示更新了多少行。

2.3、Delete(删除)
当DAO里面的某个方法添加了@Delete注解。Room会把对应的参数信息指定的行删除掉(通过参数里面的primary key找到要删除的行)。

@Delete也是可以设置onConflict来表明冲突的时候的解决办法。

@Dao
public interface UserDao {
    @Delete
    void deleteUsers(User... users);
}

@Delete对应的方法也是可以设置int返回值来表示删除了多少行。

2.4、Query(查询)
@Query注解是DAO类中使用的主要注释。它允许您对数据库执行读/写操作。@Query在编译的时候会验证准确性，所以如果查询出现问题在编译的时候就会报错。

Room还会验证查询的返回值，如果返回对象中的字段名称与查询响应中的相应列名称不匹配的时候，Room会通过以下两种方式之一提醒您：

如果只有一些字段名称匹配，它会发出警告。
如果没有字段名称匹配，它会发生错误。

@Query注解value参数：查询语句，这也是我们查询操作最关键的部分。

2.4.1、简单的查询
查询所有的信息。

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    User[] loadAllUsers();
}

返回结果可以是数组，也可以是List。

2.4.2、带参数的查询
大多数情况下我们都需要查询满足特定的查询条件的信息。

@Dao
public interface UserDao {

@Query("SELECT * FROM user WHERE firstName == :name")
User[] loadAllUsersByFirstName(String name);

}

查询需要多个参数的情况

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE age BETWEEN :minAge AND :maxAge")
    User[] loadAllUsersBetweenAges(int minAge, int maxAge);

    @Query("SELECT * FROM user WHERE firstName LIKE :search " + "OR lastName LIKE :search")
    List<User> findUserWithName(String search);
}

2.4.3、查询返回列的子集
有的时候可能指向返回某些特定的列信息。

下来的例子只查询user表中的firstName和lastName信息。

@Entity
public class User {
    @PrimaryKey
    public String firstName;
    @PrimaryKey
    public String lastName;
    public int    age;
}

public class NameTuple {
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

@Dao
public interface UserDao {

    @Query("SELECT firstName, lastName FROM user")
    List<NameTuple> loadFullName();

}

2.4.4、查询的时候传递一组参数
在查询的时候您可能需要传递一组(数组或者List)参数进去。

@Dao
public interface UserDao {

    @Query("SELECT firstName, lastName FROM user WHERE region IN (:regions)")
    public List<NameTuple> loadUsersFromRegions(List<String> regions);

}

2.4.5、Observable的查询
意思就是查询到结果的时候，UI能够自动更新。Room为了实现这一效果，查询的返回值的类型为LiveData。

@Dao
public interface UserDao {

    @Query("SELECT firstName, lastName FROM user WHERE region IN (:regions)")
    LiveData<List<NameTuple>> loadUsersFromRegionsSync(List<String> regions);

}

关于LiveData的具体用法，我们这里就不做过多的讨论了。

2.4.6、使用RxJava作为查询的返回值
Room的查询也可以返回RxJava2的Publisher或者Flowable对象。当然了想要使用这一功能需要在build.gradle文件添加  
implementation "android.arch.persistence.room:rxjava2:1.1.1" 依赖。

@Dao
public interface UserDao {

    @Query("SELECT * from user")
    Flowable<List<User>> loadUser();

}


拿到Flowable<List<User>>之后就可以去调用
    mAppDatabase.userDao()
                .loadUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> entities) {
                    }
                });

2.4.7、查询结果直接返回Cursor
查询结果直接返回cursor。然后通过cursor去获取具体的结果信息。

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE age > :minAge LIMIT 5")
    Cursor loadRawUsersOlderThan(int minAge);
}

关于怎么从Cursor里面去获取结果，大家肯定都非常熟悉。

2.4.8、多表查询
有的时候可能需要通过多个表才能获取查询结果。这个就涉及到数据的多表查询语句了。

@Dao
public interface MyDao {
    @Query("SELECT * FROM book "
           + "INNER JOIN loan ON loan.book_id = book.id "
           + "INNER JOIN user ON user.id = loan.user_id "
           + "WHERE user.name LIKE :userName")
   public List<Book> findBooksBorrowedByNameSync(String userName);
}

也可以查询指定的某些列。

@Dao
public interface MyDao {
   @Query("SELECT user.name AS userName, pet.name AS petName "
          + "FROM user, pet "
          + "WHERE user.id = pet.user_id")
   public LiveData<List<UserPet>> loadUserAndPetNames();

   // You can also define this class in a separate file, as long as you add the
   // "public" access modifier.
   static class UserPet {
       public String userName;
       public String petName;
   }
}

@Database注解可以用来创建数据库的持有者。该注解定义了实体列表，该类的内容定义了数据库中的DAO列表。  
这也是访问底层连接的主要入口点。注解类应该是抽象的并且扩展自RoomDatabase。
Database对应的对象(RoomDatabase)必须添加@Database注解，@Database包含的属性：

entities：数据库相关的所有Entity实体类，他们会转化成数据库里面的表。
version：数据库版本。
exportSchema：默认true，也是建议传true，这样可以把Schema导出到一个文件夹里面。同时建议把这个文件夹上次到VCS。
在运行时，你可以通过调用Room.databaseBuilder()或者Room.inMemoryDatabaseBuilder()获取实例。因为每次创建Database实例  
都会产生比较大的开销，所以应该将Database设计成单例的，或者直接放在Application中创建。