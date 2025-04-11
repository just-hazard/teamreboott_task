package com.task.teamreboott.application

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.domain.Company
import com.task.teamreboott.domain.Feature
import com.task.teamreboott.domain.FeatureUsage
import com.task.teamreboott.domain.PlanFeature
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.FeatureUsageRequest
import com.task.teamreboott.dto.FeatureUsageResponse
import com.task.teamreboott.exception.NotMappingPlanException
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.FeatureRepository
import com.task.teamreboott.repositories.FeatureUsageRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.YearMonth

@Service
class FeatureUsageService(
    private val companyRepository: CompanyRepository,
    private val featureRepository: FeatureRepository,
    private val featureUsageRepository: FeatureUsageRepository
) {
    @Transactional
    fun useFeature(request: FeatureUsageRequest): FeatureUsageResponse {
        val company = companyRepository.findByIdWithLock(request.companyId)
            .orElseThrow { EntityNotFoundException(ErrorMessage.NOT_FOUND_COMPANY) }

        val feature = featureRepository.findById(request.featureId)
            .orElseThrow { EntityNotFoundException(ErrorMessage.NOT_FOUND_FEATURE) }

        val plan = company.plan ?: throw NotMappingPlanException(ErrorMessage.COMPANY_IS_NOT_MAPPING_PLAN)

        val planFeature = plan.planFeatures.findSameFeature(feature.id)

        company.checkPaymentAvailability(planFeature.customCreditCost)

        if(planFeature.feature.confirmSameType(FeatureLimitType.MONTH)) {
            validateMonthlyUse(company, feature, planFeature)
        } else if(planFeature.feature.confirmSameType(FeatureLimitType.TEXT)) {
            planFeature.confirmUnitPerUse(request.usageUnit)
        }

        company.deductCredit(planFeature.customCreditCost)

        featureUsageRepository.save(
            FeatureUsage(
                company = company,
                feature = feature,
                usageUnit = request.usageUnit,
                usedCredit = planFeature.customCreditCost
            )
        )

        return FeatureUsageResponse(
            success = true,
            remainingCredit = company.credit
        )
    }

    private fun validateMonthlyUse(
        company: Company,
        feature: Feature,
        planFeature: PlanFeature,
    ) {
        val startOfMonth = YearMonth.now().atDay(1).atStartOfDay()
        val endOfMonth = YearMonth.now().atEndOfMonth().atTime(LocalTime.MAX)
        val monthUsageCount = featureUsageRepository.countByCompanyIdAndFeatureIdAndMonth(
            company.id, feature.id, startOfMonth, endOfMonth)
        planFeature.confirmUsagePerMonth(monthUsageCount)
    }
}
