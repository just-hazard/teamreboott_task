package com.task.teamreboott.application

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.domain.*
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.FeatureUsageRequest
import com.task.teamreboott.exception.ExceedUsageLimitException
import com.task.teamreboott.exception.InsufficientCreditException
import com.task.teamreboott.exception.NotIncludedFeatureException
import com.task.teamreboott.exception.NotMappingPlanException
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.FeatureRepository
import com.task.teamreboott.repositories.FeatureUsageRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.junit5.MockKExtension
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class FeatureUsageServiceTest : BehaviorSpec({

    val companyRepository = mockk<CompanyRepository>()
    val featureRepository = mockk<FeatureRepository>()
    val featureUsageRepository = mockk<FeatureUsageRepository>()
    val service = FeatureUsageService(companyRepository, featureRepository, featureUsageRepository)

    val feature = Feature(name = "AI 번역", unitLimit = 100, creditCost = 10, type = FeatureLimitType.TEXT).apply {
        id = 1L
    }

    val company = Company(name = "A사", credit = 100).apply {
        id = 1L
    }

    val planFeature = PlanFeature(
        feature = feature,
        maxCustomUnitPerUse = 100,
        maxCustomUsagePerMonth = null,
        customCreditCost = 10
    )

    val plan = Plan(name = "Basic").apply {
        id = 1L
        planFeatures = PlanFeatures(mutableListOf(planFeature))
    }

    company.addPlan(plan)

    val request = FeatureUsageRequest(
        companyId = company.id,
        featureId = feature.id,
        usageUnit = 50
    )

    given("회사가 크레딧을 충분히 가지고 있고, 사용 조건을 만족할 때") {
        every { companyRepository.findByIdWithLock(company.id) } returns Optional.of(company)
        every { featureRepository.findById(feature.id) } returns Optional.of(feature)
        every { featureUsageRepository.countByCompanyIdAndFeatureIdAndMonth(any(), any(), any(), any()) } returns 0
        every { featureUsageRepository.save(any()) } answers { firstArg() }

        `when`("useFeature를 호출하면") {
            val response = service.useFeature(request)

            then("정상적으로 사용되고, 남은 크레딧이 계산된다") {
                response.success shouldBe true
                response.remainingCredit shouldBe 90
            }
        }
    }

    given("존재하지 않는 회사를 조회할 경우") {
        every { companyRepository.findByIdWithLock(request.companyId) } returns Optional.empty()

        `when`("useFeature를 호출하면") {
            then("EntityNotFoundException이 발생해야 한다") {
                shouldThrow<EntityNotFoundException> {
                    service.useFeature(request)
                }.message shouldBe ErrorMessage.NOT_FOUND_COMPANY
            }
        }
    }

    given("존재하지 않는 기능을 조회할 경우") {
        every { companyRepository.findByIdWithLock(company.id) } returns Optional.of(company)
        every { featureRepository.findById(request.featureId) } returns Optional.empty()

        `when`("useFeature를 호출하면") {
            then("EntityNotFoundException이 발생해야 한다") {
                shouldThrow<EntityNotFoundException> {
                    service.useFeature(request)
                }.message shouldBe ErrorMessage.NOT_FOUND_FEATURE
            }
        }
    }

    given("회사가 요금제를 매핑하지 않은 경우") {
        val noPlanCompany = Company(name = "A사", credit = 100).apply {
            id = 4L
        }

        every { companyRepository.findByIdWithLock(noPlanCompany.id) } returns Optional.of(noPlanCompany)
        every { featureRepository.findById(feature.id) } returns Optional.of(feature)

        `when`("useFeature를 호출하면") {
            then("NotMappingPlanException이 발생해야 한다") {
                shouldThrow<NotMappingPlanException> {
                    service.useFeature(request.copy(companyId = noPlanCompany.id))
                }.message shouldBe ErrorMessage.COMPANY_IS_NOT_MAPPING_PLAN
            }
        }
    }

    given("회사의 크레딧이 부족한 경우") {
        val lowCreditCompany = Company(name = "A사", credit = 5).apply {
            id = 2L
            addPlan(plan)
        }

        every { companyRepository.findByIdWithLock(lowCreditCompany.id) } returns Optional.of(lowCreditCompany)
        every { featureRepository.findById(feature.id) } returns Optional.of(feature)

        `when`("useFeature를 호출하면") {
            then("InsufficientCreditException이 발생해야 한다") {
                shouldThrow<InsufficientCreditException> {
                    service.useFeature(request.copy(companyId = lowCreditCompany.id))
                }
            }
        }
    }

    given("TEXT 타입인데 사용 단위를 초과한 경우") {
        val invalidRequest = request.copy(usageUnit = 200) // maxCustomUnitPerUse = 100

        every { companyRepository.findByIdWithLock(company.id) } returns Optional.of(company)
        every { featureRepository.findById(feature.id) } returns Optional.of(feature)

        `when`("useFeature를 호출하면") {
            then("ExceedUsageLimitException이 발생해야 한다") {
                shouldThrow<ExceedUsageLimitException> {
                    service.useFeature(invalidRequest)
                }
            }
        }
    }

    given("MONTH 타입인데 월 사용량 초과한 경우") {
        val monthlyFeature = Feature(name = "AI 초안", unitLimit = 10, creditCost = 5, type = FeatureLimitType.MONTH).apply {
            id = 3L
        }

        val planFeatureMonthly = PlanFeature(
            feature = monthlyFeature,
            maxCustomUsagePerMonth = 5,
            customCreditCost = 5
        )

        val planWithMonthly = Plan(name = "MonthlyPlan").apply {
            id = 2L
            planFeatures = PlanFeatures(mutableListOf(planFeatureMonthly))
        }

        val monthlyCompany = Company(name = "A사", credit = 100).apply {
            id = 3L
            addPlan(planWithMonthly)
        }

        every { companyRepository.findByIdWithLock(monthlyCompany.id) } returns Optional.of(monthlyCompany)
        every { featureRepository.findById(monthlyFeature.id) } returns Optional.of(monthlyFeature)
        every { featureUsageRepository.countByCompanyIdAndFeatureIdAndMonth(any(), any(), any(), any()) } returns 10

        `when`("useFeature를 호출하면") {
            then("ExceedUsageLimitException이 발생해야 한다") {
                shouldThrow<ExceedUsageLimitException> {
                    service.useFeature(
                        request.copy(companyId = monthlyCompany.id, featureId = monthlyFeature.id)
                    )
                }
            }
        }
    }
})