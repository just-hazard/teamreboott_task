package com.task.teamreboott.ui

import com.task.teamreboott.application.FeatureUsageService
import com.task.teamreboott.common.ErrorResponse
import com.task.teamreboott.dto.AssignPlanResponse
import com.task.teamreboott.dto.FeatureUsageRequest
import com.task.teamreboott.dto.FeatureUsageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usage")
@Tag(name = "기능 사용 API", description = "회사에 연결된 요금제 안의 기능을 사용하는 API를 제공합니다")
class FeatureUsageController(
    private val service: FeatureUsageService
) {
    @Operation(summary = "요금제에 포함된 기능 사용", description = "요금제에 포함된 기능을 사용합니다")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200",
            description = "기능 사용 성공",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = FeatureUsageResponse::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청", content = [Content()]),
        ApiResponse(responseCode = "404", description = "존재하지 않는 회사 조회 요청 / 존재하지 않는 기능 조회 요청 / 회사에 할당되지 않은 요금제 요청 / 회사에 할당된 요금제가 없음", content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "422", description = "결제할 크레딧 부족", content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "429", description = "글자 수 초과 요청 / 월간 요청 한도 초과", content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping
    fun useFeature(
        @RequestBody @Valid request: FeatureUsageRequest
    ): ResponseEntity<FeatureUsageResponse> {
        val response = service.useFeature(request)
        return ResponseEntity.ok(response)
    }
}