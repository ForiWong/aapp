package com.wlp.myapplication.kt4

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myapplication.R

/**
 * 导入自动生成的视图注入类，在代码中可以直接使用控件
 */
import kotlinx.android.synthetic.main.activity_trans.*

/**
 * LiveData结合协程
 */
class TranslateActivity : AppCompatActivity() {

    /**
     * 通过ComponentActivity的扩展函数viewModels()方便获取ViewModel
     * TODO 这个是什么操作，学一下
     */
    private val viewModel: TranslateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans)

        /**
         * 观察每日一词结果
         * TODO return@observe
         */
        viewModel.dailyWordLiveData.observe(this) { result ->
            val dailyWordResult = result.getOrNull()
            if (null == dailyWordResult) {
                tvDailyWord.text = "获取失败"
                return@observe
            }
            tvDailyWord.text = dailyWordResult.data
        }

        /**
         * 观察翻译结果
         */
        viewModel.translateResult.observe(this) { result ->
            val translateResult = result.getOrNull()
            if (null == translateResult) {
                tvTranslate.text = "翻译失败"
                return@observe
            }

            tvTranslate.text = translateResult.data
        }

        /**
         * 按钮点击监听
         * 获取每日一词
         */
        btnDailyWord.setOnClickListener {
            viewModel.requestDailyWord()
        }

        /**
         * 按钮点击监听
         * 获取EditText输入并且通知ViewModel开始翻译
         */
        btnTranslate.setOnClickListener {
            val input = tvDailyWord.text.toString().trim()
            viewModel.requestTranslate(input)
        }
    }
}