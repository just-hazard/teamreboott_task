package com.task.teamreboott.dto

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
    val featureName: String,
    val totalUsage: Long,
    val totalCreditUsed: Long
)
