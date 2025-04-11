package com.task.teamreboott.repositories

import com.task.teamreboott.domain.Company
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface CompanyRepository : JpaRepository<Company, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Company c WHERE c.id = :id")
    fun findByIdWithLock(@Param("id") id: Long): Optional<Company>
}