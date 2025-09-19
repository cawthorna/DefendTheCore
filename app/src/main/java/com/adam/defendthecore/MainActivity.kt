package com.adam.defendthecore

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.adam.defendthecore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GameView.GameStateListener, ShopFragment.ShopListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.gameView.setPadding(0, insets.top, 0, 0)
            binding.waveTransitionControls.setPadding(
                binding.waveTransitionControls.paddingLeft,
                binding.waveTransitionControls.paddingTop,
                binding.waveTransitionControls.paddingRight,
                insets.bottom
            )

            windowInsets
        }

        binding.gameView.gameStateListener = this

        binding.nextWaveButton.setOnClickListener {
            binding.gameView.startNextWave()
        }

        binding.shopButton.setOnClickListener {
            ShopFragment().show(supportFragmentManager, ShopFragment.TAG)
        }

        binding.newGameButton.setOnClickListener {
            binding.gameView.resetGame()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.gameView.pause()
    }

    override fun onGameStateChanged(newState: GameView.GameState) {
        runOnUiThread {
            binding.newGameButton.visibility = if (newState == GameView.GameState.GAME_OVER) View.VISIBLE else View.GONE
            when (newState) {
                GameView.GameState.WAVE_TRANSITION -> {
                    binding.waveTransitionControls.visibility = View.VISIBLE
                    binding.nextWaveButton.visibility = View.VISIBLE
                    binding.nextWaveButton.text =
                        getString(R.string.start_wave, binding.gameView.getWaveNumber() + 1)
                }
                GameView.GameState.PLAYING -> {
                    binding.waveTransitionControls.visibility = View.VISIBLE
                    binding.nextWaveButton.visibility = View.GONE
                }
                GameView.GameState.GAME_OVER -> {
                    binding.waveTransitionControls.visibility = View.GONE
                    // You could show a game over screen here
                }
            }
        }
    }

    // --- ShopListener Implementation ---

    override fun onUpgradeHealth() {
        binding.gameView.upgradeHealth()
        requestStatsUpdate()
    }

    override fun onUpgradeDamage() {
        binding.gameView.upgradeDamage()
        requestStatsUpdate()
    }

    override fun onUpgradeFireRate() {
        binding.gameView.upgradeFireRate()
        requestStatsUpdate()
    }

    override fun onUpgradeDamageResistance() {
        binding.gameView.upgradeDamageResistance()
        requestStatsUpdate()
    }

    override fun onUpgradeMoneyMultiplier() {
        binding.gameView.upgradeMoneyMultiplier()
        requestStatsUpdate()
    }

    override fun onUpgradeLifesteal() {
        binding.gameView.upgradeLifesteal()
        requestStatsUpdate()
    }

    override fun onUpgradeCritChance() {
        binding.gameView.upgradeCritChance()
        requestStatsUpdate()
    }

    override fun onUpgradeCritDamage() {
        binding.gameView.upgradeCritDamage()
        requestStatsUpdate()
    }

    override fun onHeal() {
        binding.gameView.healCore()
        requestStatsUpdate()
    }

    override fun requestStatsUpdate() {
        val shopFragment = supportFragmentManager.findFragmentByTag(ShopFragment.TAG) as? ShopFragment
        shopFragment?.let {
            val stats = binding.gameView.getGameStats()
            val gameState = binding.gameView.getGameState()
            it.updateStats(stats, gameState)
        }
    }
}
