package pl.dzazef.todo.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_choose_icon.*
import pl.dzazef.todo.R

class ChooseIconActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_icon)

        icon_imv_1.tag = R.drawable.ic_baseline_add_shopping_cart_24px
        icon_imv_2.tag = R.drawable.ic_baseline_info_24px
        icon_imv_3.tag = R.drawable.ic_baseline_face_24px
        icon_imv_4.tag = R.drawable.ic_baseline_label_24px
        icon_imv_5.tag = R.drawable.ic_baseline_room_24px
        icon_imv_6.tag = R.drawable.ic_baseline_done_24px
    }

    fun onIconClick(v: View) {
        val iv : ImageView = findViewById(v.id)
        val intent = Intent()
        intent.putExtra("ICON", iv.tag as Int)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
