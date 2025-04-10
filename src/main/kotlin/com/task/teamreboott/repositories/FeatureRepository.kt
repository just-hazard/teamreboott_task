package com.task.teamreboott.repositories

import com.task.teamreboott.domain.Feature
import org.springframework.data.jpa.repository.JpaRepository

interface FeatureRepository : JpaRepository<Feature, Long> {
}