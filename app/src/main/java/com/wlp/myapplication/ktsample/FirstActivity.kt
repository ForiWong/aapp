package com.wlp.myapplication.ktsample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myapplication.R
import com.wlp.myapplication.ktsample.cor.NetworkUtils
import com.wlp.myapplication.ktsample.vm.SampleVmActivity
import com.wlp.myapplication.ktsample.vm.Vm3Activity
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by wlp on 2022/6/9
 * Description: 创建一个简单的Activity例子
 *
 * //显示启动
 * val intent = Intent(this, FirstActivity::class.java)//注释2
 * startActivity(intent)
 *
 * //隐式启动
 *  val intent = Intent("android.intent.action.START_FIRST")
 *  startActivity(intent)
 *

//启动活动，传递数据
MainActivity val intent = Intent(this, IntentMainActivity::class.java)
//向该IntentActivity传输数据,putExtra(name，value)
intent.putExtra("test_data", "显式intent")
startActivity(intent)

//IntentActivity
//接受上一个activity传输的数据getStringExtra(name)
val data = intent.getStringExtra("test_data")

//启动活动，返回数据
//MainActivity中修改startactivity()为startActivityForResult()
val intent = Intent(this, IntentMainActivity::class.java)
startActivityForResult(intent,1)
//因为使用了startActivityForResult()方法，
//在IntentActivity销毁之后会回调MainActivity的onActivityResult()方法,
//所以我们需要重写onActivityResult来得到返回的数据
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when(requestCode){ 1->if (resultCode== RESULT_OK){
        val returnedData= data?.getStringExtra("data_return")
        Log.d("MainActivity","returned data is $returnedData")
    }
}
//IntentActivity中创建一个intent对象来传递数据
val intent =Intent() intent.putExtra("data_return","hello,MainActivity")
setResult(RESULT_OK,intent) finish()

//扩展
intent不仅仅是可以跳转至你创建的Activity，还可以跳转至系统应用

如浏览器：
bt3.setOnClickListener {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.baidu.com")
    startActivity(intent)
}

如跳转至电话：
bt4.setOnClickListener {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:10010") startActivity(intent)
 }
 */

private const val TAG = "FirstActivity"

class FirstActivity : AppCompatActivity(){
    private var count = 0

    /**
     * companion object 类似于 java 的static
     * 使用：FirstActivity.actionStart(this, 123, "456")
     */
    companion object{
        fun actionStart(context: Context, param1:Int, param2:String){
            var intent= Intent(context, FirstActivity::class.java)
            intent.putExtra("param1",param1)
            intent.putExtra("param2",param2)
            context.startActivity(intent)
        }
    }

    /**
     * 异常退出，数据的持久化
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data1","数据持久化在这里")
        outState.putString("data2","哈哈")
        outState.putString("data3","从onCreate的bundle中读取")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState!=null){
            val data1 = savedInstanceState.getString("data1")
            val data2 = savedInstanceState.getString("data2")
            val data3 = savedInstanceState.getString("data3")
            Log.d(TAG,"data1=$data1  data2=$data2  data3=$data3")
        }

        setContentView(R.layout.activity_first)

        sample_binding.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            count ++
            tv_1.text = "click : $count"

            val intent = Intent(this, SampleBindingActivity::class.java)
            startActivity(intent)
        }

        sample_vm.setOnClickListener {
            val intent = Intent(this, SampleVmActivity::class.java)
            startActivity(intent)
        }

        sample_vm3.setOnClickListener {
            val intent = Intent(this, Vm3Activity::class.java)
            startActivity(intent)
        }

        sample_cor.setOnClickListener {
            queryData()
        }
    }

    private fun queryData() {
        val networkService = NetworkUtils().getNetworkService()
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { networkService.query() }
            tv_1.text = result.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.first_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.item_1 -> Toast.makeText(this, "click item 1", Toast.LENGTH_SHORT).show()
            R.id.item_2 -> Toast.makeText(this, "click item 2", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}