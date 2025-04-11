package com.task.teamreboott.repositories

import com.task.teamreboott.domain.FeatureUsage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.time.YearMonth

interface FeatureUsageRepository : JpaRepository<FeatureUsage, Long> {
    @Query("""
        SELECT COUNT(f) FROM FeatureUsage f 
        WHERE f.company.id = :companyId 
          AND f.feature.id = :featureId 
          AND f.createdDate BETWEEN :start AND :end
    """)
    fun countByCompanyIdAndFeatureIdAndMonth(
        @Param("companyId") companyId: Long,
        @Param("featureId") featureId: Long,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): Int
}