package com.task.teamreboott.ui

import com.task.teamreboott.application.CompanyService
import com.task.teamreboott.dto.AssignPlanRequest
import com.task.teamreboott.dto.AssignPlanResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/companies")
@Validated
class CompanyController(
    private val service: CompanyService,
) {
    @PostMapping("/{id}/plan")
    fun assignPlanToCompany(
        @Positive(message = "회사 ID는 0보다 커야 합니다.")
        @PathVariable id: Long,
        @RequestBody @Valid request: AssignPlanRequest,
    ): ResponseEntity<AssignPlanResponse> {
        val response = service.assignPlan(id, request)
        return ResponseEntity.ok(response)
    }
}