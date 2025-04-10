package com.task.teamreboott.domain

import com.task.teamreboott.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
class Company(
    val name: String = "",

    var credit: Int = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    var plan: Plan? = null
) : BaseEntity() {
    fun addPlan(plan: Plan) {
        this.plan = plan
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}