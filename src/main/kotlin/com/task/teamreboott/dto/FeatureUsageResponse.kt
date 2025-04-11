package com.task.teamreboott.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FeatureUsageResponse(
    @Schema(description = "성공 여부", example = "true")
    val success: Boolean,
    @Schema(description = "남은 회사 크레딧", example = "5000")
    val remainingCredit: Int
)
