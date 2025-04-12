package com.task.teamreboott.application

import com.task.teamreboott.dto.FeatureUsageStatResponse
import com.task.teamreboott.repositories.FeatureUsageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@Service
class StatisticsService(
    private val featureUsageRepository: FeatureUsageRepository
) {
    @Transactional(readOnly = true)
    fun getFeatureUsageStats(companyId: Long, fromDate: LocalDate, toDate: LocalDate): FeatureUsageStatResponse {
        return FeatureUsageStatResponse.of(featureUsageRepository.getUsageStats(
            companyId,
            fromDate.atStartOfDay(),
            toDate.atTime(LocalTime.MAX)))
    }
}
