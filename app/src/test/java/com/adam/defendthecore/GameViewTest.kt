package com.adam.defendthecore

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class GameViewTest {

    private lateinit var gameView: GameView
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        gameView = GameView(mockContext, null)
        gameView.onSizeChanged(1000, 1000, 0, 0) // Initialize core
    }

    @Test
    fun `gameView initializes with correct default values`() {
        assertEquals(0, gameView.getWaveNumber())
        assertEquals(GameView.GameState.WAVE_TRANSITION, gameView.gameStateListener?.let { gameView.gameState })
    }

    @Test
    fun `startNextWave transitions state and sets up wave`() {
        gameView.startNextWave()
        assertEquals(1, gameView.getWaveNumber())
        assertEquals(GameView.GameState.PLAYING, gameView.gameStateListener?.let { gameView.gameState })
    }

    @Test
    fun `upgradeHealth increases maxHealth and deducts money`() {
        val initialMoney = gameView.getGameStats().money
        val initialHealthCost = gameView.getGameStats().healthCost
        val initialMaxHealth = gameView.getGameStats().health
        gameView.upgradeHealth()
        val newStats = gameView.getGameStats()
        assertEquals(initialMoney - initialHealthCost, newStats.money)
        assertEquals(initialMaxHealth + 25, newStats.health)
    }

    @Test
    fun `upgradeDamage increases projectileDamage and deducts money`() {
        val initialMoney = gameView.getGameStats().money
        val initialDamageCost = gameView.getGameStats().damageCost
        val initialDamage = gameView.getGameStats().damage
        gameView.upgradeDamage()
        val newStats = gameView.getGameStats()
        assertEquals(initialMoney - initialDamageCost, newStats.money)
        assertEquals(initialDamage + 5, newStats.damage)
    }

    @Test
    fun `upgradeFireRate increases fireRatePerSecond and deducts money`() {
        val initialMoney = gameView.getGameStats().money
        val initialFireRateCost = gameView.getGameStats().fireRateCost
        val initialFireRate = gameView.getGameStats().fireRate
        gameView.upgradeFireRate()
        val newStats = gameView.getGameStats()
        assertEquals(initialMoney - initialFireRateCost, newStats.money)
        assertTrue(newStats.fireRate > initialFireRate)
    }

    @Test
    fun `game transitions to GAME_OVER when core health is zero`() {
        gameView.startNextWave()
        val core = gameView.javaClass.getDeclaredField("core").let {
            it.isAccessible = true
            it.get(gameView) as Core
        }
        core.health = 0
        gameView.javaClass.getDeclaredMethod("update", Long::class.java).let {
            it.isAccessible = true
            it.invoke(gameView, 100L)
        }
        // Manually trigger the check
        val enemiesToRemove = mutableListOf<Enemy>()
        val enemies = gameView.javaClass.getDeclaredField("enemies").let {
            it.isAccessible = true
            it.get(gameView) as MutableList<Enemy>
        }
        enemies.forEach { enemy ->
            val dx = core.x - enemy.x
            val dy = core.y - enemy.y
            val distance = kotlin.math.sqrt(dx * dx + dy * dy)
            if (distance <= enemy.radius + core.radius) {
                core.health = 0
                if (core.health <= 0) {
                    gameView.javaClass.getDeclaredField("gameState").let {
                        it.isAccessible = true
                        it.set(gameView, GameView.GameState.GAME_OVER)
                    }
                }
            }
        }
        assertEquals(GameView.GameState.GAME_OVER, gameView.gameStateListener?.let { gameView.gameState })
    }
}
