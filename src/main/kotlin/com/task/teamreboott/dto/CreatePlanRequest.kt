package com.task.teamreboott.dto

data class CreatePlanRequest(
    val name: String,
    val features: List<FeatureLimitRequest>
) {
    fun generateFeatureIdToMap(): Map<Long, FeatureLimitRequest> =
        features.associateBy { it.featureId }
}

data class FeatureLimitRequest(
    val featureId: Long,
    val maxUnitPerUse: Int?,
    val maxUsagePerMonth: Int?,
    val creditPerUse: Int?
)