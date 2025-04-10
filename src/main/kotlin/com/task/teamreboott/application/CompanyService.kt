package com.task.teamreboott.application

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.dto.AssignPlanRequest
import com.task.teamreboott.dto.AssignPlanResponse
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.PlanRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val planRepository: PlanRepository
) {
    @Transactional
    fun assignPlan(id: Long, request: AssignPlanRequest): AssignPlanResponse {
        val company = companyRepository.findById(id)
            .orElseThrow { EntityNotFoundException(ErrorMessage.NOT_FOUND_COMPANY) }

        val plan = planRepository.findById(request.planId)
            .orElseThrow { EntityNotFoundException(ErrorMessage.NOT_FOUND_PLAN) }

        company.addPlan(plan)

        return AssignPlanResponse.of(company)
    }
}