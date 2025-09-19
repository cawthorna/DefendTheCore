package com.adam.defendthecore

import org.junit.Assert.assertEquals
import org.junit.Test

class EnemyTest {

    @Test
    fun `regular enemy has correct properties`() {
        val enemy = Enemy.Regular(x = 10f, y = 20f)
        assertEquals(10f, enemy.x)
        assertEquals(20f, enemy.y)
        assertEquals(20, enemy.health)
        assertEquals(2f, enemy.speed)
        assertEquals(30f, enemy.radius)
        assertEquals(10, enemy.moneyValue)
    }

    @Test
    fun `fast enemy has correct properties`() {
        val enemy = Enemy.Fast(x = 10f, y = 20f)
        assertEquals(10f, enemy.x)
        assertEquals(20f, enemy.y)
        assertEquals(10, enemy.health)
        assertEquals(4f, enemy.speed)
        assertEquals(20f, enemy.radius)
        assertEquals(15, enemy.moneyValue)
    }

    @Test
    fun `tank enemy has correct properties`() {
        val enemy = Enemy.Tank(x = 10f, y = 20f)
        assertEquals(10f, enemy.x)
        assertEquals(20f, enemy.y)
        assertEquals(50, enemy.health)
        assertEquals(1f, enemy.speed)
        assertEquals(45f, enemy.radius)
        assertEquals(25, enemy.moneyValue)
    }
}
