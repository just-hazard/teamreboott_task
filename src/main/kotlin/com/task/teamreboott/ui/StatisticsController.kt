package com.task.teamreboott.ui

import com.task.teamreboott.application.StatisticsService
import com.task.teamreboott.dto.FeatureUsageStatRequest
import com.task.teamreboott.dto.FeatureUsageStatResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/statistics")
class StatisticsController(
    private val service: StatisticsService
) {

    @GetMapping
    fun getStats(
        @RequestBody @Valid request: FeatureUsageStatRequest
    ): ResponseEntity<FeatureUsageStatResponse> {
        val response = service.getFeatureUsageStats(request)
        return ResponseEntity.ok(response)
    }
}