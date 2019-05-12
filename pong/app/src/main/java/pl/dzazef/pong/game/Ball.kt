package pl.dzazef.pong.game

import android.graphics.RectF

data class Ball(var dx : Float, var dy : Float, var size : Float) : RectF() {
    fun reset(height: Int, width: Int) {
        this.size = height.toFloat() / 10
        this.top = height.toFloat() / 2 - this.size / 2
        this.bottom = height.toFloat() / 2 + this.size / 2
        this.left = width.toFloat() / 2 - this.size / 2
        this.right = width.toFloat() / 2 + this.size / 2
        this.dx = 10f
        this.dy = 10f
    }
}