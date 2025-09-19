package com.adam.defendthecore

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), Runnable {

    // --- Game State & Thread ---
    private var gameThread: Thread? = null
    private var surfaceHolder: SurfaceHolder = holder
    @Volatile
    private var isPlaying: Boolean = false
    private var canvas: Canvas? = null

    // --- Game State Listener ---
    interface GameStateListener {
        fun onGameStateChanged(newState: GameState)
    }
    var gameStateListener: GameStateListener? = null

    enum class GameState {
        WAVE_TRANSITION,
        PLAYING,
        GAME_OVER
    }

    private var gameState: GameState = GameState.WAVE_TRANSITION
        set(value) {
            field = value
            gameStateListener?.onGameStateChanged(value)
        }

    // --- Paints ---
    private val backgroundPaint: Paint = Paint()
    private val corePaint: Paint = Paint()
    private val projectilePaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val waveTextPaint: Paint = Paint()


    // --- Game Objects ---
    private lateinit var core: Core
    private val enemies = mutableListOf<Enemy>()
    private val projectiles = mutableListOf<Projectile>()

    // --- Player Stats ---
    private var money = 100

    // --- Upgradeable Stats ---
    private var healthLevel = 1
    private var healthCost = 50
    private var maxHealth = 100

    private var damageLevel = 1
    private var damageCost = 75
    private var projectileDamage = 10

    private var fireRateLevel = 1
    private var fireRateCost = 100
    private var fireRatePerSecond = 1.0f

    private var damageResistanceLevel = 1
    private var damageResistanceCost = 120
    private var damageResistance = 0.0f

    private var moneyMultiplierLevel = 1
    private var moneyMultiplierCost = 150
    private var moneyMultiplier = 1.0f


    // --- Wave Properties ---
    private var waveNumber = 0
    private var enemiesToSpawnInWave = 0
    private var timeBetweenSpawns = 500L
    private var timeSinceLastSpawn = 0L
    private var timeSinceLastShot = 0L


    init {
        backgroundPaint.color = Color.DKGRAY
        corePaint.color = Color.CYAN
        projectilePaint.color = Color.YELLOW
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        waveTextPaint.color = Color.GREEN
        waveTextPaint.textSize = 60f
        waveTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        core = Core(x = w / 2f, y = h / 2f, health = maxHealth)
        gameStateListener?.onGameStateChanged(gameState)
    }

    override fun run() {
        var lastTime = System.currentTimeMillis()
        while (isPlaying) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastTime
            if (elapsedTime > 0) {
                update(elapsedTime)
                draw()
            }
            lastTime = currentTime
        }
    }

    private fun update(elapsedTime: Long) {
        if (!this::core.isInitialized || gameState == GameState.GAME_OVER) return

        if (gameState == GameState.PLAYING) {
            if (enemies.isEmpty() && enemiesToSpawnInWave == 0) {
                gameState = GameState.WAVE_TRANSITION
                return
            }

            timeSinceLastSpawn += elapsedTime
            if (enemiesToSpawnInWave > 0 && timeSinceLastSpawn >= timeBetweenSpawns) {
                spawnEnemy()
                enemiesToSpawnInWave--
                timeSinceLastSpawn = 0
            }

            timeSinceLastShot += elapsedTime
            val timeBetweenShots = (1000 / fireRatePerSecond).toLong()
            if (timeSinceLastShot >= timeBetweenShots && enemies.isNotEmpty()) {
                findNearestEnemy()?.let { shootAt(it) }
                timeSinceLastShot = 0
            }

            projectiles.removeAll { p ->
                p.x += p.velocityX
                p.y += p.velocityY
                p.x < 0 || p.x > width || p.y < 0 || p.y > height
            }

            val enemiesToRemove = mutableListOf<Enemy>()
            enemies.forEach { enemy ->
                val dx = core.x - enemy.x
                val dy = core.y - enemy.y
                val distance = sqrt(dx * dx + dy * dy)
                if (distance > enemy.radius + core.radius) {
                    val moveX = enemy.speed * dx / distance
                    val moveY = enemy.speed * dy / distance
                    enemy.x += moveX
                    enemy.y += moveY
                } else {
                    val damageTaken = (10 * (1 - damageResistance)).toInt()
                    core.health -= damageTaken
                    enemiesToRemove.add(enemy)
                    if (core.health <= 0) {
                        core.health = 0
                        gameState = GameState.GAME_OVER
                    }
                }
            }

            val projectileIterator = projectiles.iterator()
            while (projectileIterator.hasNext()) {
                val projectile = projectileIterator.next()
                enemies.forEach { enemy ->
                    if (!enemiesToRemove.contains(enemy)) {
                        val dx = projectile.x - enemy.x
                        val dy = projectile.y - enemy.y
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance < projectile.radius + enemy.radius) {
                            enemy.health -= projectileDamage
                            projectileIterator.remove()
                            if (enemy.health <= 0) {
                                enemiesToRemove.add(enemy)
                                money += (enemy.moneyValue * moneyMultiplier).toInt()
                            }
                            return@forEach
                        }
                    }
                }
            }
            enemies.removeAll(enemiesToRemove)
        }
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas?.let {
                it.drawRect(0f, 0f, it.width.toFloat(), it.height.toFloat(), backgroundPaint)
                if (this::core.isInitialized) {
                    it.drawCircle(core.x, core.y, core.radius, corePaint)
                    it.drawText("Health: ${core.health} / $maxHealth", 20f, 60f, textPaint)
                    it.drawText("Wave: $waveNumber", width - 200f, 60f, textPaint)
                    it.drawText("Money: $money", 20f, 120f, textPaint)
                }
                enemies.forEach { enemy -> it.drawCircle(enemy.x, enemy.y, enemy.radius, enemy.paint) }
                projectiles.forEach { p -> it.drawCircle(p.x, p.y, p.radius, projectilePaint) }
                if (gameState == GameState.GAME_OVER) {
                    it.drawText("GAME OVER", width / 2f, height / 2f, waveTextPaint)
                }
                surfaceHolder.unlockCanvasAndPost(it)
            }
        }
    }

    fun startNextWave() {
        if (gameState == GameState.WAVE_TRANSITION) {
            waveNumber++
            enemiesToSpawnInWave = 3 * waveNumber
            gameState = GameState.PLAYING
        }
    }

    fun getWaveNumber(): Int = waveNumber

    fun getGameStats() = GameStats(
        money, maxHealth, healthLevel, healthCost,
        projectileDamage, damageLevel, damageCost,
        fireRatePerSecond, fireRateLevel, fireRateCost,
        damageResistance, damageResistanceLevel, damageResistanceCost,
        moneyMultiplier, moneyMultiplierLevel, moneyMultiplierCost
    )

    fun upgradeHealth() {
        if (money >= healthCost) {
            money -= healthCost
            healthLevel++
            maxHealth += 25
            core.health = maxHealth
            healthCost = (healthCost * 1.5).toInt()
        }
    }

    fun upgradeDamage() {
        if (money >= damageCost) {
            money -= damageCost
            damageLevel++
            projectileDamage += 5
            damageCost = (damageCost * 1.8).toInt()
        }
    }

    fun upgradeFireRate() {
        if (money >= fireRateCost) {
            money -= fireRateCost
            fireRateLevel++
            fireRatePerSecond *= 1.2f
            fireRateCost = (fireRateCost * 2.0).toInt()
        }
    }

    fun upgradeDamageResistance() {
        if (money >= damageResistanceCost && damageResistance < 0.8f) { // Cap at 80%
            money -= damageResistanceCost
            damageResistanceLevel++
            damageResistance += 0.05f
            damageResistanceCost = (damageResistanceCost * 1.7).toInt()
        }
    }

    fun upgradeMoneyMultiplier() {
        if (money >= moneyMultiplierCost) {
            money -= moneyMultiplierCost
            moneyMultiplierLevel++
            moneyMultiplier += 0.1f
            moneyMultiplierCost = (moneyMultiplierCost * 2.2).toInt()
        }
    }

    private fun findNearestEnemy(): Enemy? = enemies.minByOrNull { e -> (core.x - e.x).let { it * it } + (core.y - e.y).let { it * it } }

    private fun shootAt(enemy: Enemy) {
        val dx = enemy.x - core.x
        val dy = enemy.y - core.y
        val angle = atan2(dy, dx)
        val projectileSpeed = 15f
        val velocityX = projectileSpeed * cos(angle)
        val velocityY = projectileSpeed * sin(angle)
        projectiles.add(Projectile(core.x, core.y, velocityX, velocityY))
    }

    private fun spawnEnemy() {
        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()
        if (screenWidth == 0f || screenHeight == 0f) return
        val side = Random.nextInt(4)
        val (x, y) = when (side) {
            0 -> Random.nextFloat() * screenWidth to -50f
            1 -> screenWidth + 50f to Random.nextFloat() * screenHeight
            2 -> Random.nextFloat() * screenWidth to screenHeight + 50f
            else -> -50f to Random.nextFloat() * screenHeight
        }

        val newEnemy = when {
            waveNumber >= 7 && Random.nextInt(10) < 2 -> Enemy.Tank(x, y)
            waveNumber >= 4 && Random.nextInt(10) < 4 -> Enemy.Fast(x, y)
            else -> Enemy.Regular(x, y)
        }
        enemies.add(newEnemy)
    }

    fun pause() {
        isPlaying = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        isPlaying = true
        gameThread = Thread(this)
        gameThread?.start()
    }
}
