package com.task.teamreboott.domain

import com.task.teamreboott.domain.common.BaseEntity
import com.task.teamreboott.domain.enums.FeatureLimitType
import jakarta.persistence.*

@Entity
class Feature(
    @Column(unique = true)
    val name: String = "",

    val unitLimit: Int = 0,

    val creditCost: Int = 0,

    @Enumerated(EnumType.STRING)
    val type: FeatureLimitType = FeatureLimitType.TEXT,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun checkCustomMaxUnitPerUse(maxUnitPerUse: Int?): Int? {
        return if (type == FeatureLimitType.TEXT && maxUnitPerUse != null) {
            maxUnitPerUse
        } else if (type == FeatureLimitType.MONTH) {
            null
        } else {
            unitLimit
        }
    }

    fun checkCustomUsagePerMonth(maxUsagePerMonth: Int?): Int? {
        return if (type == FeatureLimitType.MONTH && maxUsagePerMonth != null) {
            maxUsagePerMonth
        } else if (type == FeatureLimitType.TEXT) {
            null
        } else {
            unitLimit
        }
    }

    fun checkCustomCreditCost(creditPerUse: Int?): Int {
        return creditPerUse ?: creditCost
    }

    fun confirmSameType(type: FeatureLimitType): Boolean {
        return this.type == type
    }
}