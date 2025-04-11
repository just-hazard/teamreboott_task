package com.task.teamreboott.dto

import com.task.teamreboott.domain.Company
import io.swagger.v3.oas.annotations.media.Schema

data class AssignPlanResponse(
    @Schema(description = "회사 ID", example = "1")
    val companyId: Long,
    @Schema(description = "회사 이름", example = "A사")
    val companyName: String,
    @Schema(description = "요금제 정보")
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
    @Schema(description = "요금제 ID", example = "1")
    val planId: Long,
    @Schema(description = "요금제 이름", example = "A 요금제")
    val planName: String
)
