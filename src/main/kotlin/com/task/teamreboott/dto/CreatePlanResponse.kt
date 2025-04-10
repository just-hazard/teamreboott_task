package com.task.teamreboott.dto

import com.task.teamreboott.domain.Plan

data class CreatePlanResponse(
    val planId: Long,
    val name: String,
    val features: List<FeatureResponse>
) {
    companion object {
        fun of(plan: Plan): CreatePlanResponse {
            return CreatePlanResponse(
                planId = plan.id,
                name = plan.name,
                plan.planFeatures.planFeatures.map {
                    FeatureResponse(
                        it.feature.id,
                        it.feature.type.name,
                        it.maxCustomUnitPerUse,
                        it.maxCustomUsagePerMonth,
                        it.customCreditCost
                    )
                }.toList()
            )
        }
    }
}

class FeatureResponse(
    val featureId: Long,
    val type: String,
    val maxCustomUnitPerUse: Int?,
    val maxCustomUsagePerMonth: Int?,
    val customCreditCost: Int
)
