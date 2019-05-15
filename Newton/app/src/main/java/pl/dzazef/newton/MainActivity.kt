package pl.dzazef.newton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import pl.dzazef.newton.api.NewtonAPI
import pl.dzazef.newton.api.NewtonDTO
import pl.dzazef.newton.api.zeroes.NewtonZeroesAPI
import pl.dzazef.newton.api.zeroes.NewtonZeroesDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit : Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder().baseUrl("https://newton.now.sh").addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun onClick(v : View) {
        val button = findViewById<Button>(v.id)
        if (button == btn_findzeros) {
            val create = retrofit.create(NewtonZeroesAPI::class.java)
            val call = create.getResult(button.tag.toString(), txt_expression.text.toString())
            request(call)
        } else {
            val create = retrofit.create(NewtonAPI::class.java)
            val call = create.getResult(button.tag.toString(), txt_expression.text.toString())
            request(call)
        }
    }

    private fun <T>request(call : Call<T>) {
        txt_expression.setText("Wait for result...")
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                try {
                    val body = response.body()
                    Log.i("DEBUG_INFO", body.toString())
                    if (body is NewtonDTO)
                        txt_expression.setText(body.result)
                    else if (body is NewtonZeroesDTO)
                        txt_expression.setText(body.result.toString())
                } catch (e : NullPointerException) {
                    Log.e("DEBUG_ERROR", "returned null")
                    txt_expression.setText(getString(R.string.unable_calculation))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.d("DEBUG_ERROR", "FAIL")
                txt_expression.setText(getString(R.string.failed_read_result))
            }
        })

    }
}
