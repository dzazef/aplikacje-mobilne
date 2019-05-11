package pl.dzazef.pong.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.concurrent.thread

class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private var userPaddle = Paddle()
    private var botPaddle = Paddle()
    private var ball = Ball()
    private var ballSize = 0f

    private val gameThread : GameThread

    init {
        holder.addCallback(this)
        gameThread = GameThread(holder, this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        gameThread.setRunning(false)
        gameThread.join()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        gameThread.start()

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

        for (i in 0..event!!.pointerCount) {
            val paddle = if (event!!.x < width / 2) userPaddle else botPaddle

            paddle.top = event.y + paddle.paddleLength / 2
            paddle.bottom = event.y - paddle.paddleLength / 2
            if (paddle.bottom < 0) {
                paddle.bottom = 0f
                paddle.top = paddle.paddleLength
            }
            if (paddle.top > height.toFloat()) {
                paddle.bottom = height.toFloat() - paddle.paddleLength
                paddle.top = height.toFloat()
            }
        }

        return true
    }

}