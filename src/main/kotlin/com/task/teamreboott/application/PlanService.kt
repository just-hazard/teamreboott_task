package com.task.teamreboott.application

import com.task.teamreboott.domain.Plan
import com.task.teamreboott.dto.CreatePlanRequest
import com.task.teamreboott.dto.CreatePlanResponse
import com.task.teamreboott.repositories.FeatureRepository
import com.task.teamreboott.repositories.PlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlanService(
    private val planRepository: PlanRepository,
    private val featureRepository: FeatureRepository
) {
    @Transactional
    fun createPlan(request: CreatePlanRequest): CreatePlanResponse {
        val plan = Plan(name = request.name)

        val requestFeatures = request.generateFeatureIdToMap()
        val features = featureRepository.findAllById(requestFeatures.keys)

        plan.addFeatures(requestFeatures, features);
        planRepository.save(plan)

        return CreatePlanResponse.of(plan)
    }
}