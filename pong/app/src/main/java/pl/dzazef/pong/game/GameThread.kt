package pl.dzazef.pong.game

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class GameThread(private val surfaceHolder: SurfaceHolder,
                 private val gameView: GameView) : Thread() {

    private var running = true
    private val targetFPS = 60
    private var canvas: Canvas? = null

    override fun run() {
        var startTime : Long
        var timeMillis : Long
        var waitTime : Long
        val targetTime = (1000 / targetFPS).toLong()

        while(running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            sleep(if (waitTime>0) waitTime else 0)
        }
    }

    fun setRunning(b: Boolean) {
        this.running = b
    }
}