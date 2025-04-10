package com.task.teamreboott.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class FeatureLimitType {
    TEXT,
    MONTH
}