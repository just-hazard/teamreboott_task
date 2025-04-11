package com.task.teamreboott.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive


data class CreatePlanRequest(
    @field:NotBlank(message = "요금제 이름은 필수입니다.")
    @Schema(description = "요금제 이름", example = "A 요금제", required = true)
    val name: String,
    @Schema(description = "요금제에 추가할 기능", required = true)
    @field:Valid
    val features: List<FeatureLimitRequest>
) {
    fun generateFeatureIdToMap(): Map<Long, FeatureLimitRequest> =
        features.associateBy { it.featureId }
}

data class FeatureLimitRequest(
    @field:NotNull(message = "기능 ID 값은 필수입니다.")
    @field:Positive(message = "기능 ID는 0보다 커야 합니다.")
    @Schema(description = "기능 ID", example = "1", required = true)
    val featureId: Long,
    @Schema(description = "기능의 글자제한 수를 임의로 변경", example = "10", required = false, nullable = true)
    val maxUnitPerUse: Int?,
    @Schema(description = "기능의 월 사용 횟수를 임의로 변경", example = "10", required = false, nullable = true)
    val maxUsagePerMonth: Int?,
    @Schema(description = "기능 1회 사용 시 차감 크레딧 임의로 변경", example = "10", required = false, nullable = true)
    val creditPerUse: Int?
)