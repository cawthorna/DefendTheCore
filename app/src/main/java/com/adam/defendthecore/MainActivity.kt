package com.adam.defendthecore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adam.defendthecore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GameView.GameStateListener, ShopFragment.ShopListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gameView.gameStateListener = this

        binding.nextWaveButton.setOnClickListener {
            binding.gameView.startNextWave()
        }

        binding.shopButton.setOnClickListener {
            ShopFragment().show(supportFragmentManager, ShopFragment.TAG)
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
            when (newState) {
                GameView.GameState.WAVE_TRANSITION -> {
                    binding.waveTransitionControls.visibility = View.VISIBLE
                    binding.nextWaveButton.text = "Start Wave ${binding.gameView.getWaveNumber() + 1}"
                }
                GameView.GameState.PLAYING -> {
                    binding.waveTransitionControls.visibility = View.GONE
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

    override fun requestStatsUpdate() {
        val shopFragment = supportFragmentManager.findFragmentByTag(ShopFragment.TAG) as? ShopFragment
        shopFragment?.let {
            val stats = binding.gameView.getGameStats()
            it.updateStats(stats)
        }
    }
}
