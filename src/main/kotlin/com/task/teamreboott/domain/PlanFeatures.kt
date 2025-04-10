package com.task.teamreboott.domain

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.dto.FeatureLimitRequest
import com.task.teamreboott.exception.NotExistFeatureException
import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class PlanFeatures(
    @OneToMany(mappedBy = "plan", cascade = [CascadeType.ALL], orphanRemoval = true)
    var planFeatures: MutableList<PlanFeature>
) {
    fun addPlan(plan: Plan) {
        this.planFeatures.forEach {
            it.plan = plan
        }
    }

    fun validateAndAddFeature(
        requestFeatures: Map<Long, FeatureLimitRequest>,
        features: MutableList<Feature>,
    ) {
        checkSize(requestFeatures, features)
        planFeatures = features.map {
            val request = requestFeatures[it.id]!!
            PlanFeature(
                feature = it,
                maxCustomUnitPerUse = it.checkCustomMaxUnitPerUse(request.maxUnitPerUse),
                maxCustomUsagePerMonth = it.checkCustomUsagePerMonth(request.maxUsagePerMonth),
                customCreditCost = it.checkCustomCreditCost(request.creditPerUse)
            )
        }.toMutableList()

    }

    private fun checkSize(requestFeatures: Map<Long, FeatureLimitRequest>, planFeatures: MutableList<Feature>) {
        if(requestFeatures.size != planFeatures.size) {
            throw NotExistFeatureException(ErrorMessage.NON_EXIST_FEATURE)
        }
    }
}