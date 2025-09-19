package com.adam.defendthecore

import android.graphics.Color
import android.graphics.Paint

sealed class Enemy(
    var x: Float,
    var y: Float,
    var health: Int,
    val speed: Float,
    val radius: Float,
    val moneyValue: Int,
    val paint: Paint
) {
    class Regular(x: Float, y: Float) : Enemy(
        x = x, y = y,
        health = 20,
        speed = 2f,
        radius = 30f,
        moneyValue = 10,
        paint = Paint().apply { color = Color.RED }
    )

    class Fast(x: Float, y: Float) : Enemy(
        x = x, y = y,
        health = 10, // Lower health
        speed = 4f,  // Higher speed
        radius = 20f, // Smaller
        moneyValue = 15,
        paint = Paint().apply { color = Color.YELLOW }
    )

    class Tank(x: Float, y: Float) : Enemy(
        x = x, y = y,
        health = 50, // Higher health
        speed = 1f,  // Lower speed
        radius = 45f, // Larger
        moneyValue = 25,
        paint = Paint().apply { color = Color.MAGENTA }
    )
}
