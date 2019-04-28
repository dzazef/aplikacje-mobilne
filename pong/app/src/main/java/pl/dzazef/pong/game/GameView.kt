package pl.dzazef.pong.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private var userPaddle = RectF()
    private var botPaddle = RectF()
    private var sizeY = 0f

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        sizeY = (width.toFloat() / 4)
        userPaddle.left = width.toFloat() / 2 - sizeY / 2
        userPaddle.top = height.toFloat() / 75
        userPaddle.right = userPaddle.left + width.toFloat() / 4
        userPaddle.bottom = userPaddle.top + height.toFloat() / 35

        botPaddle.left = width.toFloat() / 2 - sizeY / 2
        botPaddle.right = userPaddle.left + width.toFloat() / 4
        botPaddle.bottom = height - height.toFloat() / 75
        botPaddle.top = botPaddle.bottom - height.toFloat() / 35

        val canvas = holder!!.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val white = Paint().also { it.setARGB(255, 255, 255, 255) }
        canvas?.drawRect(userPaddle, white) ?: return
        canvas.drawRect(botPaddle, white)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        userPaddle.left = event!!.x - sizeY / 2
        userPaddle.right = event.x + sizeY / 2
        if (userPaddle.left < 0) {
            userPaddle.left = 0f
            userPaddle.right = sizeY
        }
        if (userPaddle.right > width.toFloat()) {
            userPaddle.left = width.toFloat() - sizeY
            userPaddle.right = width.toFloat()
        }

        val canvas = holder!!.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        return true
    }

}