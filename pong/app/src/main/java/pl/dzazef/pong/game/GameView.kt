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

    private var leftPaddle = Paddle()
    private var rightPaddle = Paddle()
    private var ball = Ball(0f, 0f, 0f)

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

        leftPaddle.paddleLength = (height.toFloat() / 4)
        leftPaddle.top = height.toFloat() / 2 - leftPaddle.paddleLength / 2
        leftPaddle.bottom = leftPaddle.top + height.toFloat() / 4
        leftPaddle.left = width.toFloat() / 75
        leftPaddle.right = leftPaddle.left + width.toFloat() / 35

        rightPaddle.paddleLength = (height.toFloat() / 4)
        rightPaddle.top = height.toFloat() / 2 - rightPaddle.paddleLength / 2
        rightPaddle.bottom = rightPaddle.top + height.toFloat() / 4
        rightPaddle.right = width - width.toFloat() / 75
        rightPaddle.left = rightPaddle.right - width.toFloat() / 35

        ball.reset(height, width)

        val canvas = holder!!.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val white = Paint().also { it.setARGB(255, 255, 255, 255) }
        canvas?.drawRect(leftPaddle, white) ?: return
        canvas.drawRect(rightPaddle, white)
        canvas.drawOval(ball, white)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        for (i in 0..event!!.pointerCount) {
            val paddle = if (event.x < width / 2) leftPaddle else rightPaddle

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

    fun ballUpdate() {
        if (ball.bottom + ball.dy > height) {
            ball.dy *= -1
        }
        if (ball.top + ball.dy < 0) {
            ball.dy *= -1
        }
        if (ball.right + ball.dx > rightPaddle.left) {
            if (ball.bottom < rightPaddle.top && ball.top > rightPaddle.bottom) {
                ball.dx *= -1
            } else {
                ball.reset(height, width)
            }
        }
        if (ball.left + ball.dx < leftPaddle.right) {
            if (ball.bottom < leftPaddle.top && ball.top > leftPaddle.bottom) {
                ball.dx *= -1
            } else {
                ball.reset(height, width)
            }        }
        ball.right += ball.dx
        ball.left += ball.dx
        ball.top += ball.dy
        ball.bottom += ball.dy
    }

}