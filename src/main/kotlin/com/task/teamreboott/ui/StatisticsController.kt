package com.task.teamreboott.ui

import com.task.teamreboott.application.StatisticsService
import com.task.teamreboott.dto.FeatureUsageStatResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@Tag(name = "통계 API", description = "회사 별 통계 API를 제공합니다")
@RequestMapping("/api/statistics")
@Validated
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
        @Parameter(description = "회사 ID" , example = "1", required = true)
        @RequestParam
        @Positive(message = "회사 ID는 0보다 커야 합니다.") companyId: Long,
        @Parameter(description = "조회 시작 날짜", example = "2025-04-11", required = true)
        @RequestParam
        @Past(message = "현재 날짜보다 과거여야합니다")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fromDate: LocalDate,
        @Parameter(description = "조회 마감 날짜", example = "2025-04-11", required = true)
        @RequestParam
        @PastOrPresent(message = "현재 날짜 또는 과거 날짜여야합니다")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) toDate: LocalDate
    ): ResponseEntity<FeatureUsageStatResponse> {
        val response = service.getFeatureUsageStats(companyId, fromDate, toDate)
        return ResponseEntity.ok(response)
    }
}