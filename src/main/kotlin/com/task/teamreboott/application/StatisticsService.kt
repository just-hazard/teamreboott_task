package com.task.teamreboott.application

import com.task.teamreboott.dto.FeatureUsageStatRequest
import com.task.teamreboott.dto.FeatureUsageStatResponse
import com.task.teamreboott.repositories.FeatureUsageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@Service
class StatisticsService(
    private val featureUsageRepository: FeatureUsageRepository
) {
    @Transactional(readOnly = true)
    fun getFeatureUsageStats(request: FeatureUsageStatRequest): FeatureUsageStatResponse {
        return FeatureUsageStatResponse.of(featureUsageRepository.getUsageStats(
            request.companyId,
            request.fromDate.atStartOfDay(),
            request.toDate.atTime(LocalTime.MAX)))
    }
}
