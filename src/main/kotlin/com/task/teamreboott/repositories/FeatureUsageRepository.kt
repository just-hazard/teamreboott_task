package com.task.teamreboott.repositories

import com.task.teamreboott.domain.FeatureUsage
import com.task.teamreboott.dto.FeatureUsageStat
import com.task.teamreboott.dto.FeatureUsageStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
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

    @Query("""
        SELECT new com.task.teamreboott.dto.FeatureUsageStat(
            f.name,
            SUM(u.usageUnit),
            SUM(u.usedCredit)
        )
        FROM FeatureUsage u
        JOIN u.feature f
        WHERE u.company.id = :companyId
          AND u.createdDate BETWEEN :from AND :to
        GROUP BY f.name
    """)
    fun getUsageStats(
        @Param("companyId") companyId: Long,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): List<FeatureUsageStat>
}