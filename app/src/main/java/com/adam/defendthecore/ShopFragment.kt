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
        fun onUpgradeLifesteal()
        fun onUpgradeCritChance()
        fun onUpgradeCritDamage()
        fun onHeal()
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
            System.err.println(e.toString())
            System.err.println(e.stackTrace)
            throw ClassCastException((context.toString() +
                    " must implement ShopListener"))
        }

        binding.healthUpgradeButton.setOnClickListener { listener?.onUpgradeHealth() }
        binding.damageUpgradeButton.setOnClickListener { listener?.onUpgradeDamage() }
        binding.fireRateUpgradeButton.setOnClickListener { listener?.onUpgradeFireRate() }
        binding.damageResistanceUpgradeButton.setOnClickListener { listener?.onUpgradeDamageResistance() }
        binding.moneyMultiplierUpgradeButton.setOnClickListener { listener?.onUpgradeMoneyMultiplier() }
        binding.lifestealUpgradeButton.setOnClickListener { listener?.onUpgradeLifesteal() }
        binding.critChanceUpgradeButton.setOnClickListener { listener?.onUpgradeCritChance() }
        binding.critDamageUpgradeButton.setOnClickListener { listener?.onUpgradeCritDamage() }
        binding.healButton.setOnClickListener { listener?.onHeal() }


        listener?.requestStatsUpdate()
    }

    fun updateStats(stats: GameStats, gameState: GameView.GameState) {
        binding.moneyTextView.text = getString(R.string.shop_money, stats.money)

        // Health Upgrade
        binding.healthUpgradeLevelTextView.text = getString(R.string.shop_level, stats.healthLevel)
        binding.healthUpgradeCurrentTextView.text = getString(R.string.shop_current_health, stats.health)
        binding.healthUpgradeNextTextView.text = getString(R.string.shop_next_health, stats.nextHealth)
        binding.healthUpgradeCostTextView.text = getString(R.string.shop_cost, stats.healthCost)
        binding.healthUpgradeButton.isEnabled = stats.money >= stats.healthCost

        // Damage Upgrade
        binding.damageUpgradeLevelTextView.text = getString(R.string.shop_level, stats.damageLevel)
        binding.damageUpgradeCurrentTextView.text = getString(R.string.shop_current_damage, stats.damage)
        binding.damageUpgradeNextTextView.text = getString(R.string.shop_next_damage, stats.nextDamage)
        binding.damageUpgradeCostTextView.text = getString(R.string.shop_cost, stats.damageCost)
        binding.damageUpgradeButton.isEnabled = stats.money >= stats.damageCost

        // Fire Rate Upgrade
        binding.fireRateUpgradeLevelTextView.text = getString(R.string.shop_level, stats.fireRateLevel)
        binding.fireRateUpgradeCurrentTextView.text = getString(R.string.shop_current_fire_rate, stats.fireRate)
        binding.fireRateUpgradeNextTextView.text = getString(R.string.shop_next_fire_rate, stats.nextFireRate)
        binding.fireRateUpgradeCostTextView.text = getString(R.string.shop_cost, stats.fireRateCost)
        binding.fireRateUpgradeButton.isEnabled = stats.money >= stats.fireRateCost

        // Damage Resistance Upgrade
        binding.damageResistanceUpgradeLevelTextView.text = getString(R.string.shop_level, stats.damageResistanceLevel)
        binding.damageResistanceUpgradeCurrentTextView.text = getString(R.string.shop_current_damage_resistance, stats.damageResistance * 100)
        binding.damageResistanceUpgradeNextTextView.text = getString(R.string.shop_next_damage_resistance, stats.nextDamageResistance * 100)
        binding.damageResistanceUpgradeCostTextView.text = getString(R.string.shop_cost, stats.damageResistanceCost)
        binding.damageResistanceUpgradeButton.isEnabled = stats.money >= stats.damageResistanceCost

        // Money Multiplier Upgrade
        binding.moneyMultiplierUpgradeLevelTextView.text = getString(R.string.shop_level, stats.moneyMultiplierLevel)
        binding.moneyMultiplierUpgradeCurrentTextView.text = getString(R.string.shop_current_money_multiplier, stats.moneyMultiplier)
        binding.moneyMultiplierUpgradeNextTextView.text = getString(R.string.shop_next_money_multiplier, stats.nextMoneyMultiplier)
        binding.moneyMultiplierUpgradeCostTextView.text = getString(R.string.shop_cost, stats.moneyMultiplierCost)
        binding.moneyMultiplierUpgradeButton.isEnabled = stats.money >= stats.moneyMultiplierCost

        // Lifesteal Upgrade
        binding.lifestealUpgradeLevelTextView.text = getString(R.string.shop_level, stats.lifestealLevel)
        binding.lifestealUpgradeCurrentTextView.text = getString(R.string.shop_current_lifesteal, stats.lifesteal * 100)
        binding.lifestealUpgradeNextTextView.text = getString(R.string.shop_next_lifesteal, stats.nextLifesteal * 100)
        binding.lifestealUpgradeCostTextView.text = getString(R.string.shop_cost, stats.lifestealCost)
        binding.lifestealUpgradeButton.isEnabled = stats.money >= stats.lifestealCost

        // Crit Chance Upgrade
        binding.critChanceUpgradeLevelTextView.text = getString(R.string.shop_level, stats.critChanceLevel)
        binding.critChanceUpgradeCurrentTextView.text = getString(R.string.shop_current_crit_chance, stats.critChance * 100)
        binding.critChanceUpgradeNextTextView.text = getString(R.string.shop_next_crit_chance, stats.nextCritChance * 100)
        binding.critChanceUpgradeCostTextView.text = getString(R.string.shop_cost, stats.critChanceCost)
        binding.critChanceUpgradeButton.isEnabled = stats.money >= stats.critChanceCost

        // Crit Damage Upgrade
        binding.critDamageUpgradeLevelTextView.text = getString(R.string.shop_level, stats.critDamageLevel)
        binding.critDamageUpgradeCurrentTextView.text = getString(R.string.shop_current_crit_damage, stats.critDamage)
        binding.critDamageUpgradeNextTextView.text = getString(R.string.shop_next_crit_damage, stats.nextCritDamage)
        binding.critDamageUpgradeCostTextView.text = getString(R.string.shop_cost, stats.critDamageCost)
        binding.critDamageUpgradeButton.isEnabled = stats.money >= stats.critDamageCost

        // Heal Button
        if (gameState == GameView.GameState.WAVE_TRANSITION) {
            binding.healLayout.visibility = View.VISIBLE
            binding.healDivider.visibility = View.VISIBLE
            val atFullHealth = stats.health >= stats.nextHealth - 25 // A bit of a hack to get max health
            if (atFullHealth) {
                binding.healCostTextView.text = getString(R.string.shop_heal_at_full_health)
                binding.healButton.isEnabled = false
            } else {
                binding.healCostTextView.text = getString(R.string.shop_heal_cost, stats.healCost)
                binding.healButton.isEnabled = stats.money >= stats.healCost
            }
        } else {
            binding.healLayout.visibility = View.GONE
            binding.healDivider.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ShopDialog"
    }
}
