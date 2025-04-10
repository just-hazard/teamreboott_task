package com.task.teamreboott.ui

import com.task.teamreboott.application.PlanService
import com.task.teamreboott.dto.CreatePlanRequest
import com.task.teamreboott.dto.CreatePlanResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/plans")
class PlanController(
    private val service: PlanService
) {
    @PostMapping
    fun createPlan(@RequestBody request: CreatePlanRequest): ResponseEntity<CreatePlanResponse> {
        val response = service.createPlan(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}