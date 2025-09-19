package com.adam.defendthecore

import org.junit.Assert.assertEquals
import org.junit.Test

class CoreTest {

    @Test
    fun `core initializes with correct default values`() {
        val core = Core()
        assertEquals(0f, core.x)
        assertEquals(0f, core.y)
        assertEquals(50f, core.radius)
        assertEquals(100, core.health)
    }

    @Test
    fun `core initializes with specified values`() {
        val core = Core(x = 10f, y = 20f, radius = 30f, health = 80)
        assertEquals(10f, core.x)
        assertEquals(20f, core.y)
        assertEquals(30f, core.radius)
        assertEquals(80, core.health)
    }
}
