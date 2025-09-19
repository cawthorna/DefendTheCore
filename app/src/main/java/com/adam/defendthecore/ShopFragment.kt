package com.adam.defendthecore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.adam.defendthecore.databinding.FragmentShopBinding

class ShopFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

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

        // Health Upgrade
        binding.healthUpgradeLevelTextView.text = "(Lvl ${stats.healthLevel})"
        binding.healthUpgradeCurrentTextView.text = "Current: ${stats.health}"
        binding.healthUpgradeNextTextView.text = "Next: ${stats.nextHealth}"
        binding.healthUpgradeCostTextView.text = "Cost: ${stats.healthCost}"
        binding.healthUpgradeButton.isEnabled = stats.money >= stats.healthCost

        // Damage Upgrade
        binding.damageUpgradeLevelTextView.text = "(Lvl ${stats.damageLevel})"
        binding.damageUpgradeCurrentTextView.text = "Current: ${stats.damage}"
        binding.damageUpgradeNextTextView.text = "Next: ${stats.nextDamage}"
        binding.damageUpgradeCostTextView.text = "Cost: ${stats.damageCost}"
        binding.damageUpgradeButton.isEnabled = stats.money >= stats.damageCost

        // Fire Rate Upgrade
        binding.fireRateUpgradeLevelTextView.text = "(Lvl ${stats.fireRateLevel})"
        binding.fireRateUpgradeCurrentTextView.text = "Current: ${"%.2f".format(stats.fireRate)}/s"
        binding.fireRateUpgradeNextTextView.text = "Next: ${"%.2f".format(stats.nextFireRate)}/s"
        binding.fireRateUpgradeCostTextView.text = "Cost: ${stats.fireRateCost}"
        binding.fireRateUpgradeButton.isEnabled = stats.money >= stats.fireRateCost

        // Damage Resistance Upgrade
        binding.damageResistanceUpgradeLevelTextView.text = "(Lvl ${stats.damageResistanceLevel})"
        binding.damageResistanceUpgradeCurrentTextView.text = "Current: ${"%.0f".format(stats.damageResistance * 100)}%"
        binding.damageResistanceUpgradeNextTextView.text = "Next: ${"%.0f".format(stats.nextDamageResistance * 100)}%"
        binding.damageResistanceUpgradeCostTextView.text = "Cost: ${stats.damageResistanceCost}"
        binding.damageResistanceUpgradeButton.isEnabled = stats.money >= stats.damageResistanceCost

        // Money Multiplier Upgrade
        binding.moneyMultiplierUpgradeLevelTextView.text = "(Lvl ${stats.moneyMultiplierLevel})"
        binding.moneyMultiplierUpgradeCurrentTextView.text = "Current: ${"%.1f".format(stats.moneyMultiplier)}x"
        binding.moneyMultiplierUpgradeNextTextView.text = "Next: ${"%.1f".format(stats.nextMoneyMultiplier)}x"
        binding.moneyMultiplierUpgradeCostTextView.text = "Cost: ${stats.moneyMultiplierCost}"
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
