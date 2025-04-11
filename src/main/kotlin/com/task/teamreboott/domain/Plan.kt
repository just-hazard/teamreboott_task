package com.task.teamreboott.domain

import com.task.teamreboott.domain.common.BaseEntity
import com.task.teamreboott.dto.FeatureLimitRequest
import jakarta.persistence.*

@Entity
class Plan(
    @Column(unique = true)
    val name: String = "",

    @Embedded
    var planFeatures: PlanFeatures = PlanFeatures(mutableListOf())
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun addFeatures(requestFeatures: Map<Long, FeatureLimitRequest>, features: MutableList<Feature>) {
        this.planFeatures.validateAndAddFeature(requestFeatures, features)
        this.planFeatures.addPlan(this)
    }
}