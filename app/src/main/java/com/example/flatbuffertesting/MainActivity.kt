package com.example.flatbuffertesting

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.flatbuffertesting.product.ProductList
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    private val TAG = "Flat_Buffers"
    private var mTvFlatStats: TextView? = null
    private var mTvJsonStats: TextView? = null
    private var tvProductPrice: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvFlatStats = findViewById(R.id.tv_Flat_buffer_time)
        mTvJsonStats = findViewById(R.id.tv_json_time)
        tvProductPrice = findViewById(R.id.tvProductPrice)
    }

    fun loadFromFlatBuffer(view: View) {
        val buffer = readRawResource(application, R.raw.product_flatbuff)
        val startTime = System.currentTimeMillis()
        val bb = ByteBuffer.wrap(buffer!!)
        val productList = ProductList.getRootAsProductList(bb)
        for (i in 0 until productList.productLength()) {
            Log.d(TAG, "Amount: " + productList.product(i).amount())
            tvProductPrice?.text =("Amount: " + productList.product(i).amount())
        }

        val timeTaken = System.currentTimeMillis() - startTime
        val logText = "FlatBuffer : " + timeTaken + "ms"
        mTvFlatStats?.setText(logText)
        Log.d(TAG, "loadFromFlatBuffer $logText")
    }

/*  public void loadFromFlatBuffer(View view) {
    byte[] buffer = readRawResource(getApplication(), R.raw.repos_flatbuff);
    long startTime = System.currentTimeMillis();
    ByteBuffer bb = ByteBuffer.wrap(buffer);
    ReposList reposList = ReposList.getRootAsReposList(bb);
    long timeTaken = System.currentTimeMillis() - startTime;
    String logText = "FlatBuffer : " + timeTaken + "ms";
    mTvFlatStats.setText(logText);
    Log.d(TAG, "loadFromFlatBuffer " + logText);
  }*/

    fun loadFromJson(view: View) {
      /*  val jsonText = String(readRawResource(application, R.raw.repos_json)!!)
        val startTime = System.currentTimeMillis()
        val reposList = Gson().fromJson(jsonText, ReposList::class.java)
        val timeTaken = System.currentTimeMillis() - startTime
        val logText = "Json : " + timeTaken + "ms"
        mTvJsonStats.setText(logText)
        Log.d(TAG, "loadFromJson $logText")*/
    }

    private fun readRawResource(
        context: Context,
        resId: Int
    ): ByteArray? {
        var stream: InputStream? = null
        var buffer: ByteArray? = null
        try {
            stream = context.resources.openRawResource(resId)
            buffer = ByteArray(stream.available())
            while (stream.read(buffer) != -1);
        } catch (ignored: IOException) {
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (ignored: IOException) {
                }

            }
        }
        return buffer
    }

}
