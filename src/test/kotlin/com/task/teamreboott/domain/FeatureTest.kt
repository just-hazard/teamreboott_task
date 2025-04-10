package com.task.teamreboott.domain

import com.task.teamreboott.domain.enums.FeatureLimitType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class FeatureTest : BehaviorSpec({

    given("type이 TEXT일 때") {
        val feature = Feature(name = "AI 번역", type = FeatureLimitType.TEXT, unitLimit = 1500, creditCost = 10)

        `when`("maxUnitPerUse가 주어지면") {
            val result = feature.checkCustomMaxUnitPerUse(50)

            then("해당 값이 반환된다") {
                result shouldBe 50
            }
        }

        `when`("maxUnitPerUse가 null이면") {
            val result = feature.checkCustomMaxUnitPerUse(null)

            then("기존값이 반환된다") {
                result shouldBe 1500
            }
        }

        `when`("checkCustomUsagePerMonth를 호출하면") {
            val result = feature.checkCustomUsagePerMonth(100)

            then("null이 반환된다") {
                result shouldBe null
            }
        }
    }

    given("type이 MONTH일 때") {
        val feature = Feature(name = "AI 초안 작성", type = FeatureLimitType.MONTH, unitLimit = 200, creditCost = 50)

        `when`("checkCustomMaxUnitPerUse를 호출하면") {
            val result = feature.checkCustomMaxUnitPerUse(100)

            then("null이 반환된다") {
                result shouldBe null
            }
        }

        `when`("maxUsagePerMonth가 주어지면") {
            val result = feature.checkCustomUsagePerMonth(150)

            then("해당 값이 반환된다") {
                result shouldBe 150
            }
        }

        `when`("maxUsagePerMonth가 null이면") {
            val result = feature.checkCustomUsagePerMonth(null)

            then("기존값이 반환된다") {
                result shouldBe 200
            }
        }
    }


    given("크레딧 사용 조건") {
        val feature = Feature(name = "credit_feature", creditCost = 15)

        `when`("creditPerUse가 null이 아니면") {
            val result = feature.checkCustomCreditCost(5)

            then("해당 값이 반환된다") {
                result shouldBe 5
            }
        }

        `when`("creditPerUse가 null이면") {
            val result = feature.checkCustomCreditCost(null)

            then("기본 creditCost 값이 반환된다") {
                result shouldBe 15
            }
        }
    }
})