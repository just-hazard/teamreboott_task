package com.task.teamreboott.domain

import com.task.teamreboott.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
class FeatureUsage(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company = Company(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    val feature: Feature = Feature(),

    val usageUnit: Int = 0,

    val deductedCredit: Int = 0
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}