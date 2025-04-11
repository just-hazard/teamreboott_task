package com.task.teamreboott.domain

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.exception.ExceedUsageLimitException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class PlanFeatureTest : BehaviorSpec({

    given("PlanFeature가 있을 때") {
        val planFeature = PlanFeature(
            maxCustomUsagePerMonth = 100,
            maxCustomUnitPerUse = 10,
            customCreditCost = 5
        )

        `when`("월 사용량이 제한 이하일 경우") {
            then("예외가 발생하지 않아야 한다") {
                planFeature.confirmUsagePerMonth(50)
            }
        }

        `when`("월 사용량이 제한을 초과한 경우") {
            then("ExceedUsageLimitException 예외가 발생해야 한다") {
                shouldThrow<ExceedUsageLimitException> {
                    planFeature.confirmUsagePerMonth(150)
                }.message shouldBe ErrorMessage.EXCEED_UNIT_PER_USE
            }
        }

        `when`("1회 사용 단위가 제한 이하일 경우") {
            then("예외가 발생하지 않아야 한다") {
                planFeature.confirmUnitPerUse(5)
            }
        }

        `when`("1회 사용 단위가 제한을 초과한 경우") {
            then("ExceedUsageLimitException 예외가 발생해야 한다") {
                shouldThrow<ExceedUsageLimitException> {
                    planFeature.confirmUnitPerUse(20)
                }.message shouldBe ErrorMessage.EXCEED_USAGE_PER_MONTH
            }
        }
    }
})