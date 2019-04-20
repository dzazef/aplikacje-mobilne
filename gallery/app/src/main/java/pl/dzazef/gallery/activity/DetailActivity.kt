package pl.dzazef.gallery.activity

//import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_detail.*
import pl.dzazef.gallery.EXTRA_FILE_PATH
import pl.dzazef.gallery.R
import pl.dzazef.gallery.camera.Utils

class DetailActivity : AppCompatActivity() {
    private var editable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val path = intent.getStringExtra(EXTRA_FILE_PATH)
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
    }

    fun onEditClick(v: View) {
        if (!editable) {
            val parent = detail_txt.parent as ViewGroup
            parent.removeView(detail_txt)

            val editText = EditText(this).also { it.setText("EditText") }
            editText.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

            parent.addView(editText)
            editable = true
        }

    }
}
