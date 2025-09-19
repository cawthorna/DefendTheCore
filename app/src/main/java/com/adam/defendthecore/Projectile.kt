package com.adam.defendthecore

data class Projectile(
    var x: Float,
    var y: Float,
    val velocityX: Float,
    val velocityY: Float,
    var radius: Float = 10f
)
