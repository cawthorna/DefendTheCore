package com.adam.defendthecore

data class GameStats(
    val money: Int,
    val health: Int,
    val healthLevel: Int,
    val healthCost: Int,
    val damage: Int,
    val damageLevel: Int,
    val damageCost: Int,
    val fireRate: Float,
    val fireRateLevel: Int,
    val fireRateCost: Int,
    val damageResistance: Float,
    val damageResistanceLevel: Int,
    val damageResistanceCost: Int,
    val moneyMultiplier: Float,
    val moneyMultiplierLevel: Int,
    val moneyMultiplierCost: Int
)
