package pl.dzazef.todo.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_add_item.*
import pl.dzazef.todo.R
import java.text.SimpleDateFormat
import java.util.*


class AddItemActivity : AppCompatActivity() {
    var color : Int = pl.dzazef.todo.R.color.lightYellow
    var icon : Int = R.drawable.ic_baseline_done_24px

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(pl.dzazef.todo.R.layout.activity_add_item)

        val cal = Calendar.getInstance().time
        val dateFormat = "dd/MM/yy"
        add_text_date.text = SimpleDateFormat(dateFormat, Locale.US).format(cal.time)
        val timeFormat = "HH:mm"
        add_text_time.text = SimpleDateFormat(timeFormat, Locale.GERMANY).format(cal.time)



        val listener = object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                add_text_priority.text = (progress+1).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        }
        seekBar.setOnSeekBarChangeListener(listener)
    }

    fun onClickColorButton(view: View) {
        val button = findViewById<Button>(view.id)
        when (button) {
            add_button_yellow -> color = pl.dzazef.todo.R.color.lightYellow
            add_button_orange -> color = pl.dzazef.todo.R.color.lightOrange
            add_button_red -> color = pl.dzazef.todo.R.color.lightRed
            add_button_green -> color = pl.dzazef.todo.R.color.lightGreen
            add_button_blue -> color = pl.dzazef.todo.R.color.lightBlue
            add_button_gray -> color = pl.dzazef.todo.R.color.lightGray
        }
        editText.background = getDrawable(color)
    }

    fun onAcceptItem(view: View) {
        val intent = Intent()
        intent.putExtra("DATE_TIME", add_text_date.text.toString() + " " + add_text_time.text.toString())
        intent.putExtra("MESSAGE", editText.text.toString())
        intent.putExtra("COLOR", ContextCompat.getColor(this, color))
        intent.putExtra("PRIORITY", add_text_priority.text.toString())
        intent.putExtra("ICON", icon)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onDateClick(v: View) {
        val cal = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(cal)
        }
        DatePickerDialog(this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onTimeClick(v: View) {
        val cal = Calendar.getInstance()
        val time = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            updateTime(cal)
        }
        TimePickerDialog(this, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun updateDate(cal: Calendar) {
        val myFormat = "dd/MM/yy" //In which you need put here
        val date = SimpleDateFormat(myFormat, Locale.US)
        add_text_date.text = date.format(cal.time)
    }

    fun updateTime(cal: Calendar) {
        val myFormat = "HH:mm"
        val time = SimpleDateFormat(myFormat, Locale.US)
        add_text_time.text = time.format(cal.time)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("COLOR", color)
        outState?.putString("DATE", add_text_date.text.toString())
        outState?.putString("TIME", add_text_time.text.toString())
        outState?.putInt("ICON", icon)
    }

    fun onImageClick(view: View) {
        val intent = Intent(this, ChooseIconActivity::class.java)
        startActivityForResult(intent, 9002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9002 && resultCode == Activity.RESULT_OK) {
            val drawableId = data!!.getIntExtra("ICON", 0)
            icon = drawableId
            add_imv_icon.setImageResource(icon)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        color = savedInstanceState!!.getInt("COLOR")
        editText.background = getDrawable(color)
        add_text_date.text = savedInstanceState.getString("DATE")
        add_text_time.text = savedInstanceState.getString("TIME")
        icon = savedInstanceState.getInt("ICON")
        add_imv_icon.setImageResource(icon)
    }
}
