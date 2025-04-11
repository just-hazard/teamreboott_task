package com.task.teamreboott.ui

import com.task.teamreboott.application.PlanService
import com.task.teamreboott.common.ErrorResponse
import com.task.teamreboott.dto.CreatePlanRequest
import com.task.teamreboott.dto.CreatePlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "요금제 API", description = "요금제 생성 API를 제공합니다")
@RequestMapping("/api/plans")
class PlanController(
    private val service: PlanService,
) {
    @Operation(summary = "요금제 생성", description = "요금제 이름 및 기능 정보를 받아 요금제를 생성합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201",
        description = "요금제 생성 성공",
        content = [Content(mediaType = "application/json",
            schema = Schema(implementation = CreatePlanResponse::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청", content = [Content()]),
        ApiResponse(responseCode = "404", description = "존재하지 않는 기능 등록 요청", content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping
    fun createPlan(@RequestBody @Valid request: CreatePlanRequest): ResponseEntity<CreatePlanResponse> {
        val response = service.createPlan(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)

    }
}