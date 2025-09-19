package com.adam.defendthecore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.adam.defendthecore.databinding.FragmentShopBinding

class ShopFragment : DialogFragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private var listener: ShopListener? = null

    interface ShopListener {
        fun onUpgradeHealth()
        fun onUpgradeDamage()
        fun onUpgradeFireRate()
        fun onUpgradeDamageResistance()
        fun onUpgradeMoneyMultiplier()
        fun requestStatsUpdate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            listener = context as ShopListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ShopListener"))
        }

        binding.healthUpgradeButton.setOnClickListener { listener?.onUpgradeHealth() }
        binding.damageUpgradeButton.setOnClickListener { listener?.onUpgradeDamage() }
        binding.fireRateUpgradeButton.setOnClickListener { listener?.onUpgradeFireRate() }
        binding.damageResistanceUpgradeButton.setOnClickListener { listener?.onUpgradeDamageResistance() }
        binding.moneyMultiplierUpgradeButton.setOnClickListener { listener?.onUpgradeMoneyMultiplier() }


        listener?.requestStatsUpdate()
    }

    fun updateStats(stats: GameStats) {
        binding.moneyTextView.text = "Money: ${stats.money}"

        binding.healthUpgradeTextView.text = "Core Health (Lvl ${stats.healthLevel}): ${stats.health}"
        binding.healthUpgradeButton.text = "Buy (Cost: ${stats.healthCost})"
        binding.healthUpgradeButton.isEnabled = stats.money >= stats.healthCost

        binding.damageUpgradeTextView.text = "Turret Damage (Lvl ${stats.damageLevel}): ${stats.damage}"
        binding.damageUpgradeButton.text = "Buy (Cost: ${stats.damageCost})"
        binding.damageUpgradeButton.isEnabled = stats.money >= stats.damageCost

        binding.fireRateUpgradeTextView.text = "Fire Rate (Lvl ${stats.fireRateLevel}): ${"%.2f".format(stats.fireRate)}/s"
        binding.fireRateUpgradeButton.text = "Buy (Cost: ${stats.fireRateCost})"
        binding.fireRateUpgradeButton.isEnabled = stats.money >= stats.fireRateCost

        binding.damageResistanceUpgradeTextView.text = "Dmg. Resist (Lvl ${stats.damageResistanceLevel}): ${"%.0f".format(stats.damageResistance * 100)}%"
        binding.damageResistanceUpgradeButton.text = "Buy (Cost: ${stats.damageResistanceCost})"
        binding.damageResistanceUpgradeButton.isEnabled = stats.money >= stats.damageResistanceCost

        binding.moneyMultiplierUpgradeTextView.text = "Money Gain (Lvl ${stats.moneyMultiplierLevel}): ${"%.1f".format(stats.moneyMultiplier)}x"
        binding.moneyMultiplierUpgradeButton.text = "Buy (Cost: ${stats.moneyMultiplierCost})"
        binding.moneyMultiplierUpgradeButton.isEnabled = stats.money >= stats.moneyMultiplierCost
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ShopDialog"
    }
}
