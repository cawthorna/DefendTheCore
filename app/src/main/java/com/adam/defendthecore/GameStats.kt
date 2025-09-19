package com.adam.defendthecore

data class GameStats(
    val money: Int,
    val health: Int,
    val healthLevel: Int,
    val healthCost: Int,
    val nextHealth: Int,
    val damage: Int,
    val damageLevel: Int,
    val damageCost: Int,
    val nextDamage: Int,
    val fireRate: Float,
    val fireRateLevel: Int,
    val fireRateCost: Int,
    val nextFireRate: Float,
    val damageResistance: Float,
    val damageResistanceLevel: Int,
    val damageResistanceCost: Int,
    val nextDamageResistance: Float,
    val moneyMultiplier: Float,
    val moneyMultiplierLevel: Int,
    val moneyMultiplierCost: Int,
    val nextMoneyMultiplier: Float
)
