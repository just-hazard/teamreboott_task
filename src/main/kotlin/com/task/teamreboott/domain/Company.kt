package com.task.teamreboott.domain

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.domain.common.BaseEntity
import com.task.teamreboott.exception.InsufficientCreditException
import jakarta.persistence.*

@Entity
class Company(
    val name: String = "",

    var credit: Int = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    var plan: Plan? = null
) : BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun addPlan(plan: Plan) {
        this.plan = plan
    }

    fun checkPaymentAvailability(customCreditCost: Int) {
        if(credit < customCreditCost) {
            throw InsufficientCreditException(ErrorMessage.INSUFFICIENT_CREDIT)
        }
    }

    fun deductCredit(customCreditCost: Int) {
        credit -= customCreditCost
    }
}