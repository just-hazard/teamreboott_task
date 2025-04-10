package com.task.teamreboott.application

import com.task.teamreboott.domain.Company
import com.task.teamreboott.domain.Plan
import com.task.teamreboott.dto.AssignPlanRequest
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.PlanRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import java.util.*

internal class CompanyServiceTest : BehaviorSpec({

    val companyRepository = mockk<CompanyRepository>()
    val planRepository = mockk<PlanRepository>()
    val companyService = CompanyService(companyRepository, planRepository)

    given("회사와 요금제가 존재할 때") {
        val companyId = 1L
        val planId = 100L

        val company = spyk(Company(name = "A사"))
        val plan = Plan(name = "A요금제")

        val request = AssignPlanRequest(planId = planId)

        every { companyRepository.findById(companyId) } returns Optional.of(company)
        every { planRepository.findById(planId) } returns Optional.of(plan)

        `when`("assignPlan을 호출하면") {
            val response = companyService.assignPlan(companyId, request)

            then("회사에 요금제가 할당되고 응답이 반환된다") {
                verify { company.addPlan(plan) }
                response.companyName shouldBe "A사"
                response.plan.planName shouldBe "A요금제"
            }
        }
    }

    given("회사가 존재하지 않을 때") {
        val request = AssignPlanRequest(planId = 1L)

        every { companyRepository.findById(any()) } returns Optional.empty()

        `when`("assignPlan을 호출하면") {
            then("EntityNotFoundException이 발생한다") {
                shouldThrow<EntityNotFoundException> {
                    companyService.assignPlan(10L, request)
                }
            }
        }
    }

    given("요금제가 존재하지 않을 때") {
        val company = Company(name = "A사")
        val request = AssignPlanRequest(planId = 10L)

        every { companyRepository.findById(any()) } returns Optional.of(company)
        every { planRepository.findById(any()) } returns Optional.empty()

        `when`("assignPlan을 호출하면") {
            then("EntityNotFoundException이 발생한다") {
                shouldThrow<EntityNotFoundException> {
                    companyService.assignPlan(1L, request)
                }
            }
        }
    }
})