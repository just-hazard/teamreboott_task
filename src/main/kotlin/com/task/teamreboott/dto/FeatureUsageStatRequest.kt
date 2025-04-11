package com.task.teamreboott.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import java.time.LocalDate

data class FeatureUsageStatRequest(
    @field:Positive(message = "회사 ID는 0보다 커야 합니다.")
    @Schema(description = "회사 ID", example = "1", required = true)
    val companyId: Long,
    @field:Past(message = "현재 날짜보다 과거여야합니다")
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(description = "조회를 시작 날짜", example = "2025.04.11", required = true)
    val fromDate: LocalDate,
    @field:PastOrPresent(message = "현재 날짜 또는 과거 날짜여야합니다")
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(description = "조회 마감 날짜", example = "2025.04.11", required = true)
    val toDate: LocalDate
)