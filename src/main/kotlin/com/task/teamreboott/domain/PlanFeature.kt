package com.task.teamreboott.domain

import com.task.teamreboott.domain.common.BaseEntity
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.FeatureLimitRequest
import jakarta.persistence.*
import kotlin.math.max

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
}