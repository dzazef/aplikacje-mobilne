package pl.dzazef.gallery.activity

//import com.squareup.picasso.Picasso
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detail.*
import pl.dzazef.gallery.EXTRA_DESCRIPTION
import pl.dzazef.gallery.EXTRA_FILE_PATH
import pl.dzazef.gallery.EXTRA_RATING
import pl.dzazef.gallery.R
import pl.dzazef.gallery.camera.Utils
import pl.dzazef.gallery.controllers.SharedPreferencesController

class DetailActivity : AppCompatActivity() {
    private var editable = false
    private lateinit var recentView : View
    private lateinit var path: String
    private var lastDescription : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        path = intent.getStringExtra(EXTRA_FILE_PATH)
        Thread{
            var bitmap : Bitmap? = null
            val thread = Thread{bitmap = BitmapFactory.decodeFile(path)}.also { it.start() }
            thread.join()
            runOnUiThread { if(bitmap!=null) {
                    detail_imv.setImageBitmap(bitmap)
                    detail_imv.rotation = Utils().getRotation(path)
                }
            }
        }.start()
//        Picasso.with(this).load(File(path)).into(imageView) - tried, too slow :C
        lastDescription = intent.getStringExtra(EXTRA_DESCRIPTION)
        detail_txt.text = lastDescription
        recentView = detail_txt
    }

    fun onEditClick(v: View) {
        val parent = recentView.parent as ViewGroup
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        parent.removeView(recentView)
        if (!editable) {
            val editText = EditText(this).also { it.setText(lastDescription) }
            editText.gravity = Gravity.TOP
            editText.layoutParams = layoutParams
            parent.addView(editText)
            detail_fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_24px))
            recentView = editText
            editable = true
        } else {
            val text = (recentView as EditText).text.toString()
            lastDescription = text
            val textView = TextView(this).also { it.text = lastDescription }
            textView.layoutParams = layoutParams
            parent.addView(textView)
            recentView = textView
            editable = false
        }

    }

    fun onSaveClick(v: View) {
        val intent = Intent()
        intent.putExtra(EXTRA_DESCRIPTION, lastDescription)
        intent.putExtra(EXTRA_RATING, 2)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
