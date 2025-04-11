package com.task.teamreboott.domain

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.exception.InsufficientCreditException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*

internal class CompanyTest : BehaviorSpec({

    given("일정 크레딧을 가진 회사가 있을 때") {
        val company = Company(name = "A사", credit = 100)
        val plan = Plan(name = "A 요금제") // Plan은 간단한 더미 객체로 가정

        `when`("요금제를 회사에 추가하면") {
            company.addPlan(plan)

            then("회사의 plan 필드가 해당 요금제로 설정되어야 한다") {
                company.plan shouldBe plan
            }
        }

        `when`("충분한 크레딧으로 결제 가능 여부를 확인하면") {
            then("예외가 발생하지 않아야 한다") {
                company.checkPaymentAvailability(50) // 충분한 크레딧
            }
        }

        `when`("부족한 크레딧으로 결제 가능 여부를 확인하면") {
            then("InsufficientCreditException 예외가 발생해야 한다") {
                shouldThrow<InsufficientCreditException> {
                    company.checkPaymentAvailability(200) // 부족한 크레딧
                }.message shouldBe ErrorMessage.INSUFFICIENT_CREDIT
            }
        }

        `when`("크레딧을 차감하면") {
            company.deductCredit(30)

            then("회사의 크레딧이 차감되어야 한다") {
                company.credit shouldBe 70
            }
        }
    }
})