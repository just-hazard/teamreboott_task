package com.task.teamreboott.repositories

import com.task.teamreboott.domain.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
}