package com.task.teamreboott.domain

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.domain.common.BaseEntity
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.exception.ExceedUsageLimitException
import jakarta.persistence.*

@Entity
class PlanFeature(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    var plan: Plan = Plan(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    val feature: Feature = Feature(),

    val maxCustomUnitPerUse: Int? = null,

    val maxCustomUsagePerMonth: Int? = null,

    val customCreditCost: Int = 0
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun confirmUsagePerMonth(monthUsageCount: Int) {
        if(monthUsageCount > maxCustomUsagePerMonth!!) {
            throw ExceedUsageLimitException(ErrorMessage.EXCEED_UNIT_PER_USE)
        }
    }

    fun confirmUnitPerUse(usageUnit: Int) {
        if(usageUnit > maxCustomUnitPerUse!!) {
            throw ExceedUsageLimitException(ErrorMessage.EXCEED_USAGE_PER_MONTH)
        }
    }
}