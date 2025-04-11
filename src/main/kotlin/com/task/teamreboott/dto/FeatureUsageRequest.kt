package com.task.teamreboott.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive

data class FeatureUsageRequest(
    @field:Positive(message = "회사 ID는 0보다 커야 합니다.")
    @Schema(description = "회사 ID", example = "1", required = true)
    val companyId: Long,
    @field:Positive(message = "기능 ID는 0보다 커야 합니다.")
    @Schema(description = "기능 ID", example = "1", required = true)
    val featureId: Long,
    @field:Positive(message = "글자 수는 0보다 커야 합니다")
    @Schema(description = "요청하는 글자 수", example = "1500", required = true)
    val usageUnit: Int
)
