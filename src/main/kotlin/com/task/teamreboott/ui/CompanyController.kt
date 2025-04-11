package com.task.teamreboott.ui

import com.task.teamreboott.application.CompanyService
import com.task.teamreboott.common.ErrorResponse
import com.task.teamreboott.dto.AssignPlanRequest
import com.task.teamreboott.dto.AssignPlanResponse
import com.task.teamreboott.dto.CreatePlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/companies")
@Tag(name = "요금제 연결 API", description = "회사에 요금제를 연결하는 API를 제공합니다")
@Validated
class CompanyController(
    private val service: CompanyService,
) {
    @Operation(summary = "회사에 요금제 연결", description = "회사 ID 및 요금제 ID를 매핑합니다")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200",
            description = "요금제 연결 성공",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = AssignPlanResponse::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청", content = [Content()]),
        ApiResponse(responseCode = "404", description = "존재하지 않는 요금제 조회 요청 / 존재하지 않는 회사 조회 요청", content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
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