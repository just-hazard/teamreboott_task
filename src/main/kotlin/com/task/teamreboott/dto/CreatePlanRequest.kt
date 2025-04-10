package com.task.teamreboott.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CreatePlanRequest(
    @field:NotBlank(message = "요금제 이름은 필수입니다.")
    val name: String,
    @field:Valid
    val features: List<FeatureLimitRequest>
) {
    fun generateFeatureIdToMap(): Map<Long, FeatureLimitRequest> =
        features.associateBy { it.featureId }
}

data class FeatureLimitRequest(
    @field:NotNull(message = "기능 PK 값은 필수입니다.")
    @field:Positive(message = "기능 PK 값은 0보다 커야 합니다.")
    val featureId: Long,
    val maxUnitPerUse: Int?,
    val maxUsagePerMonth: Int?,
    val creditPerUse: Int?
)