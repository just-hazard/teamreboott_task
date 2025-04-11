package com.task.teamreboott.dto

import com.task.teamreboott.domain.Plan
import io.swagger.v3.oas.annotations.media.Schema

data class CreatePlanResponse(
    @Schema(description = "요금제 ID", example = "1")
    val planId: Long,
    @Schema(description = "요금제 이름", example = "A 요금제")
    val name: String,
    @Schema(description = "요금제에 추가된 기능")
    val features: List<FeatureResponse>,
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
    @Schema(description = "기능 ID", example = "1")
    val featureId: Long,
    @Schema(description = "기능 Type", example = "TEXT or MONTH")
    val type: String,
    @Schema(description = "요청마다 최대 요청 글자 수", example = "2000")
    val maxCustomUnitPerUse: Int?,
    @Schema(description = "월 최대 요청 수", example = "200")
    val maxCustomUsagePerMonth: Int?,
    @Schema(description = "요청마다 부과되는 크레딧", example = "200")
    val customCreditCost: Int,
)
