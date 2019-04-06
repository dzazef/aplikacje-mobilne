package pl.dzazef.todo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_detail.*

import pl.dzazef.todo.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dateTime : String = intent.getStringExtra("DATE_TIME")
        val message : String = intent.getStringExtra("MESSAGE")
        val color : Int = intent.getIntExtra("COLOR", 0)
        val priority : String = intent.getStringExtra("PRIORITY")

        text_message.setBackgroundColor(color)
        text_date_time.text = dateTime
        text_message.text = message
        text_priority.text = priority
    }
}
