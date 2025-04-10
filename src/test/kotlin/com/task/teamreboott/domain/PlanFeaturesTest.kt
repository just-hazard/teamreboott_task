package com.task.teamreboott.domain

import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.FeatureLimitRequest
import com.task.teamreboott.exception.NotExistFeatureException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

internal class PlanFeaturesTest : BehaviorSpec({

    given("PlanFeature 리스트가 존재할 때") {
        val planFeatures = PlanFeatures(
            planFeatures = mutableListOf(
                PlanFeature(feature = Feature(name = "AI 번역")),
                PlanFeature(feature = Feature(name = "AI 교정"))
            )
        )

        val plan = Plan(name = "A 요금제")

        `when`("addPlan을 호출하면") {
            planFeatures.addPlan(plan)

            then("모든 PlanFeature에 해당 plan이 설정된다") {
                planFeatures.planFeatures.forEach {
                    it.plan shouldBe plan
                }
            }
        }
    }

    given("유효한 FeatureLimitRequest와 Feature 리스트가 주어졌을 때") {
        val feature1 = Feature(name = "Feature 1", type = FeatureLimitType.TEXT, unitLimit = 10, creditCost = 5).apply { this.id = 1L }
        val feature2 = Feature(name = "Feature 2", type = FeatureLimitType.MONTH, unitLimit = 20, creditCost = 3).apply { this.id = 2L }

        val requestMap = mapOf(
            1L to FeatureLimitRequest(1L, 5, null, 2),
            2L to FeatureLimitRequest(2L, null, 100, null)
        )

        val planFeatures = PlanFeatures(planFeatures = mutableListOf())

        `when`("validateAndAddFeature를 호출하면") {
            planFeatures.validateAndAddFeature(requestMap, mutableListOf(feature1, feature2))

            then("Feature마다 PlanFeature가 생성된다") {
                planFeatures.planFeatures shouldHaveSize 2
                val pf1 = planFeatures.planFeatures[0]
                pf1.feature shouldBe feature1
                pf1.maxCustomUnitPerUse shouldBe 5
                pf1.maxCustomUsagePerMonth shouldBe null
                pf1.customCreditCost shouldBe 2

                val pf2 = planFeatures.planFeatures[1]
                pf2.feature shouldBe feature2
                pf2.maxCustomUnitPerUse shouldBe null
                pf2.maxCustomUsagePerMonth shouldBe 100
                pf2.customCreditCost shouldBe 3
            }
        }
    }

    given("요청된 Feature 개수와 실제 Feature 개수가 다르면") {
        val feature1 = Feature(name = "Feature 1").apply { this.id = 1L }
        val feature2 = Feature(name = "Feature 2").apply { this.id = 2L }
        val requestMap = mapOf(
            1L to FeatureLimitRequest(1, null, null, null)
        )

        val planFeatures = PlanFeatures(planFeatures = mutableListOf())

        `when`("validateAndAddFeature를 호출하면") {
            then("NotExistFeatureException이 발생한다") {
                shouldThrow<NotExistFeatureException> {
                    planFeatures.validateAndAddFeature(requestMap, mutableListOf(feature1, feature2))
                }
            }
        }
    }
})