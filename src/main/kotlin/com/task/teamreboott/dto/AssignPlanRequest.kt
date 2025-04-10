package com.task.teamreboott.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

class AssignPlanRequest(
    @field:NotNull(message = "기능 ID 값은 필수입니다.")
    @field:Positive(message = "요금제 ID는 0보다 커야 합니다.")
    val planId: Long
)
