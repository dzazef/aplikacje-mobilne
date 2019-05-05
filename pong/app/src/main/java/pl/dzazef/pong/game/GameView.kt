package pl.dzazef.pong.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private var userPaddle = Paddle()
    private var botPaddle = Paddle()
    private var ball = Ball()
    private var ballSize = 0f

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        userPaddle.paddleLength = (height.toFloat() / 4)
        userPaddle.top = height.toFloat() / 2 - userPaddle.paddleLength / 2
        userPaddle.bottom = userPaddle.top + height.toFloat() / 4
        userPaddle.left = width.toFloat() / 75
        userPaddle.right = userPaddle.left + width.toFloat() / 35

        botPaddle.paddleLength = (height.toFloat() / 4)
        botPaddle.top = height.toFloat() / 2 - botPaddle.paddleLength / 2
        botPaddle.bottom = userPaddle.top + height.toFloat() / 4
        botPaddle.right = width - width.toFloat() / 75
        botPaddle.left = botPaddle.right - width.toFloat() / 35

        ballSize = height.toFloat() / 10
        ball.top = height.toFloat() / 2 - ballSize / 2
        ball.bottom = height.toFloat() / 2 + ballSize / 2
        ball.left = width.toFloat() / 2 - ballSize / 2
        ball.right = width.toFloat() / 2 + ballSize / 2

        val canvas = holder!!.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val white = Paint().also { it.setARGB(255, 255, 255, 255) }
        canvas?.drawRect(userPaddle, white) ?: return
        canvas.drawRect(botPaddle, white)
        canvas.drawOval(ball, white)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        userPaddle.top = event!!.y + userPaddle.paddleLength / 2
        userPaddle.bottom = event.y - userPaddle.paddleLength / 2
        if (userPaddle.bottom < 0) {
            userPaddle.bottom = 0f
            userPaddle.top = userPaddle.paddleLength
        }
        if (userPaddle.top > height.toFloat()) {
            userPaddle.bottom = height.toFloat() - userPaddle.paddleLength
            userPaddle.top = height.toFloat()
        }

        val canvas = holder!!.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        return true
    }

}