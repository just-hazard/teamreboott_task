package com.task.teamreboott.application

import com.task.teamreboott.domain.Feature
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.CreatePlanRequest
import com.task.teamreboott.dto.FeatureLimitRequest
import com.task.teamreboott.repositories.FeatureRepository
import com.task.teamreboott.repositories.PlanRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class PlanServiceTest: BehaviorSpec({

    val planRepository = mockk<PlanRepository>(relaxed = true)
    val featureRepository = mockk<FeatureRepository>()

    val planService = PlanService(planRepository, featureRepository)

    given("CreatePlanRequest가 주어졌을 때") {
        val feature1 = Feature(name = "Feature A", type = FeatureLimitType.TEXT, unitLimit = 100, creditCost = 10).apply { this.id = 1L }
        val feature2 = Feature(name = "Feature B", type = FeatureLimitType.MONTH, unitLimit = 200, creditCost = 5).apply { this.id = 2L }

        val request = CreatePlanRequest(
            name = "A 요금제",
            features = listOf(
                FeatureLimitRequest(1L, 50, null, 7),
                FeatureLimitRequest(2L, null, 80, null)
            )
        )

        val expectedFeatureMap = request.generateFeatureIdToMap()
        val expectedFeatures = listOf(feature1, feature2)

        every { featureRepository.findAllById(expectedFeatureMap.keys) } returns expectedFeatures
        every { planRepository.save(any()) } answers { firstArg() }

        `when`("createPlan을 호출하면") {
            val response = planService.createPlan(request)

            then("findAllById가 호출된다") {
                verify(exactly = 1) { featureRepository.findAllById(expectedFeatureMap.keys) }
            }

            then("save가 호출된다") {
                verify(exactly = 1) { planRepository.save(any()) }
            }

            then("response 반환된다") {
                response.name shouldBe "A 요금제"
                response.features shouldHaveSize 2
            }
        }
    }
})