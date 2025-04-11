package com.task.teamreboott.dto

import jakarta.validation.constraints.Positive

data class FeatureUsageRequest(
    @field:Positive(message = "회사 ID는 0보다 커야 합니다.")
    val companyId: Long,
    @field:Positive(message = "기능 ID는 0보다 커야 합니다.")
    val featureId: Long,
    @field:Positive(message = "글자 또는 요청 횟수는 0보다 커야 합니다")
    val usageUnit: Int
)
