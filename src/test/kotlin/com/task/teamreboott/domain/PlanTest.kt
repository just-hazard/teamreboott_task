package com.task.teamreboott.domain

import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.FeatureLimitRequest
import com.task.teamreboott.exception.NotExistFeatureException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

internal class PlanTest : BehaviorSpec({

    given("유효한 requestFeatures와 features가 주어졌을 때") {
        val feature1 = Feature(name = "Feature A", type = FeatureLimitType.TEXT, unitLimit = 100, creditCost = 10).apply { this.id = 1L }
        val feature2 = Feature(name = "Feature B", type = FeatureLimitType.MONTH, unitLimit = 200, creditCost = 5).apply { this.id = 2L }

        val request = mapOf(
            1L to FeatureLimitRequest(1, 50, null, 7),
            2L to FeatureLimitRequest(2, null, 80, null)
        )

        val plan = Plan(name = "A 요금제")

        `when`("addFeatures를 호출하면") {
            plan.addFeatures(request, mutableListOf(feature1, feature2))

            then("plan의 planFeatures에 2개의 PlanFeature가 추가된다") {
                plan.planFeatures.planFeatures shouldHaveSize 2
            }

            then("모든 PlanFeature는 해당 plan을 참조한다") {
                plan.planFeatures.planFeatures.forEach {
                    it.plan shouldBe plan
                }
            }
        }
    }

    given("요청된 feature 수와 실제 feature 수가 다르면") {
        val feature1 = Feature(name = "Feature A").apply { this.id = 1L }
        val feature2 = Feature(name = "Feature B").apply { this.id = 2L }

        val invalidRequestMap = mapOf(
            1L to FeatureLimitRequest(1, 10, 20, 5)
        )

        val plan = Plan(name = "A 요금제")

        `when`("addFeatures를 호출하면") {
            then("NotExistFeatureException이 발생한다") {
                shouldThrow<NotExistFeatureException> {
                    plan.addFeatures(invalidRequestMap, mutableListOf(feature1, feature2))
                }
            }
        }
    }
})