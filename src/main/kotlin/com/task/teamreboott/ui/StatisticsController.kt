package com.task.teamreboott.ui

import com.task.teamreboott.application.StatisticsService
import com.task.teamreboott.common.ErrorResponse
import com.task.teamreboott.dto.CreatePlanResponse
import com.task.teamreboott.dto.FeatureUsageStatRequest
import com.task.teamreboott.dto.FeatureUsageStatResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "통계 API", description = "회사 별 통계 API를 제공합니다")
@RequestMapping("/api/statistics")
class StatisticsController(
    private val service: StatisticsService
) {

    @Operation(summary = "회사 별 통계 조회", description = "회사 기능별 사용량을 조회합니다")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200",
            description = "통계 조회 성공",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = FeatureUsageStatResponse::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청", content = [Content()])
    ])
    @GetMapping
    fun getStats(
        @RequestBody @Valid request: FeatureUsageStatRequest
    ): ResponseEntity<FeatureUsageStatResponse> {
        val response = service.getFeatureUsageStats(request)
        return ResponseEntity.ok(response)
    }
}