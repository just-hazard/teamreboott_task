package com.task.teamreboott.ui

import com.task.teamreboott.application.FeatureUsageService
import com.task.teamreboott.dto.FeatureUsageRequest
import com.task.teamreboott.dto.FeatureUsageResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usage")
class FeatureUsageController(
    private val service: FeatureUsageService
) {
    @PostMapping
    fun useFeature(
        @RequestBody @Valid request: FeatureUsageRequest
    ): ResponseEntity<FeatureUsageResponse> {
        val response = service.useFeature(request)
        return ResponseEntity.ok(response)
    }
}