package com.task.teamreboott.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import java.time.LocalDate

data class FeatureUsageStatRequest(
    @field:Positive(message = "회사 ID는 0보다 커야 합니다.")
    val companyId: Long,
    @field:Past(message = "현재 날짜보다 과거여야합니다")
    @JsonFormat(pattern = "yyyy.MM.dd")
    val fromDate: LocalDate,
    @field:PastOrPresent(message = "현재 날짜 또는 과거 날짜여야합니다")
    @JsonFormat(pattern = "yyyy.MM.dd")
    val toDate: LocalDate
)