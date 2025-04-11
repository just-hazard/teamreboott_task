package com.task.teamreboott.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FeatureUsageStatResponse(
    val featureStatistics: List<FeatureUsageStat>
) {
    companion object {
        fun of(list: List<FeatureUsageStat>): FeatureUsageStatResponse {
            return FeatureUsageStatResponse(list)
        }
    }
}

data class FeatureUsageStat(
    @Schema(description = "기능 이름", example = "AI 번역")
    val featureName: String,
    @Schema(description = "조회 기간 동안 사용한 글자 번역 or 요청 횟수", example = "5000")
    val totalUsage: Long,
    @Schema(description = "조회 기간 동안 사용한 크레딧", example = "5000")
    val totalCreditUsed: Long
)
