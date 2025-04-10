package com.task.teamreboott.repositories

import com.task.teamreboott.domain.Plan
import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<Plan, Long> {
}