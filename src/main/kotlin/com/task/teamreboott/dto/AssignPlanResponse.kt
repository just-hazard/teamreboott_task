package com.task.teamreboott.dto

import com.task.teamreboott.domain.Company

data class AssignPlanResponse(
    val companyId: Long,
    val companyName: String,
    val plan: PlanInfo
) {
    companion object {
        fun of(company: Company): AssignPlanResponse {
            return AssignPlanResponse(
                companyId = company.id,
                companyName = company.name,
                PlanInfo(
                    planId = company.plan!!.id,
                    planName = company.plan!!.name
                )
            )
        }
    }
}

data class PlanInfo(
    val planId: Long,
    val planName: String
)
