package pl.dzazef.pong.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.MotionEventCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import pl.dzazef.pong.SharedPreferenceManager

class GameView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val sharedPreferenceManager = SharedPreferenceManager(context)
    private val gameThread : GameThread

    private var leftPaddle = Paddle()
    private var rightPaddle = Paddle()

    private var leftScore = 0
    private var rightScore = 0
    private var highScore = 0

    private var ball = Ball(0f, 0f, 0f)


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
        highScore = sharedPreferenceManager.getHighScore()
        updateScore()

        gameThread.start()

        leftPaddle.paddleLength = (height.toFloat() / 4)
        leftPaddle.bottom = height.toFloat() / 2 + leftPaddle.paddleLength / 2
        leftPaddle.top = leftPaddle.bottom - height.toFloat() / 4
        leftPaddle.left = width.toFloat() / 75
        leftPaddle.right = leftPaddle.left + width.toFloat() / 50

        rightPaddle.paddleLength = (height.toFloat() / 4)
        rightPaddle.bottom = height.toFloat() / 2 + rightPaddle.paddleLength / 2
        rightPaddle.top = rightPaddle.bottom - height.toFloat() / 4
        rightPaddle.right = width - width.toFloat() / 75
        rightPaddle.left = rightPaddle.right - width.toFloat() / 50

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

        val highScorePaint = TextPaint().also { it.color = Color.GRAY; it.textSize = width/20f; it.textAlign = Paint.Align.CENTER; }
        val scorePaint = TextPaint().also { it.color = Color.GRAY; it.textSize = width/5f; it.textAlign = Paint.Align.CENTER; }
        canvas.drawText("High Score: $highScore", width/2f, highScorePaint.textSize, highScorePaint)
        canvas.drawText("$leftScore : $rightScore", width/2f, height/2f*1.3f, scorePaint)
        canvas.drawOval(ball, white)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        for (i in 0 until event!!.pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            val paddle = if (x < width / 2) leftPaddle else rightPaddle

            paddle.top = y - paddle.paddleLength / 2
            paddle.bottom = y + paddle.paddleLength / 2
            if (paddle.top < 0) {
                paddle.top = 0f
                paddle.bottom = paddle.paddleLength
            }
            if (paddle.bottom > height.toFloat()) {
                paddle.top = height.toFloat() - paddle.paddleLength
                paddle.bottom = height.toFloat()
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
            if ((ball.bottom + ball.dy) > rightPaddle.top && (ball.top + ball.dy) < rightPaddle.bottom) {
                ball.dx *= -1
            } else {
                ball.reset(height, width)
                leftScore++
                updateScore()
            }
        }
        if (ball.left + ball.dx < leftPaddle.right) {
            if ((ball.bottom + ball.dy) > leftPaddle.top && (ball.top + ball.dy) < leftPaddle.bottom) {
                ball.dx *= -1
            } else {
                ball.reset(height, width)
                rightScore++
                updateScore()
            }
        }
        ball.right += ball.dx
        ball.left += ball.dx
        ball.top += ball.dy
        ball.bottom += ball.dy
    }

    private fun updateScore() {
        if (leftScore > highScore) {
            highScore = leftScore
            sharedPreferenceManager.saveHighScore(highScore)
        }
        if (rightScore > highScore) {
            highScore = rightScore
            sharedPreferenceManager.saveHighScore(highScore)
        }
    }

}