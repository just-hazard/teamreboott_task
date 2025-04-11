package com.task.teamreboott.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

class AssignPlanRequest(
    @field:NotNull(message = "기능 ID 값은 필수입니다.")
    @field:Positive(message = "요금제 ID는 0보다 커야 합니다.")
    @Schema(description = "요금제 ID", example = "1", required = true)
    val planId: Long
)
