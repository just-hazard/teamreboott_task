package com.task.teamreboott

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.dto.*
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.test.assertEquals

@DisplayName("회사 기능")
class CompanyAcceptanceTests : AcceptanceTest() {

    private var planAId: Long = 0
    private var planBId: Long = 0

    @BeforeEach
    fun setUp() {
        val request_one = CreatePlanRequest(
            name = "A 요금제",
            features = listOf(
                FeatureLimitRequest(1, 3000, null, 20),
            )
        )

        val request_two = CreatePlanRequest(
            name = "B 요금제",
            features = listOf(
                FeatureLimitRequest(4, null, null, null)
            )
        )

        planAId = Given {
            body(request_one)
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/plans")
        } Extract {
            path<Long>("planId").toLong()
        }

        planBId = Given {
            body(request_two)
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/plans")
        } Extract {
            path<Long>("planId").toLong()
        }
    }

    @Test
    fun `회사에 요금제 매핑`() {
        val response = Given {
            body(AssignPlanRequest(planAId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            `as`(AssignPlanResponse::class.java)
        }

        assertEquals(1L, response.companyId)
        assertEquals("A사", response.companyName)
        assertEquals(planAId, response.plan.planId)
        assertEquals("A 요금제", response.plan.planName)
    }

    @Test
    fun `다른 요금제 매핑`() {
        Given {
            body(AssignPlanRequest(planAId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        }

        val response = Given {
            body(AssignPlanRequest(planBId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            `as`(AssignPlanResponse::class.java)
        }

        assertEquals(1L, response.companyId)
        assertEquals("A사", response.companyName)
        assertEquals(planBId, response.plan.planId)
        assertEquals("B 요금제", response.plan.planName)
    }

    @Test
    fun `회사에 요금제 매핑 시 유효하지 않은 파라미터`() {
        Given {
            body(AssignPlanRequest(1L))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 0)
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
            body(CoreMatchers.containsString("회사 ID는 0보다 커야 합니다."))
        }

        Given {
            body(AssignPlanRequest(0))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
            body(CoreMatchers.containsString("요금제 ID는 0보다 커야 합니다."))
        }
    }

    @Test
    fun `회사에 요금제 매핑 시 유효하지 않는 회사 ID 및 요금제 ID로 요청 시`() {
        Given {
            body(AssignPlanRequest(1L))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 10)
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.NOT_FOUND_COMPANY))
        }

        Given {
            body(AssignPlanRequest(10L))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.NOT_FOUND_PLAN))
        }
    }
}