package com.task.teamreboott

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.dto.*
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@DisplayName("기능 사용")
class FeatureUsageAcceptanceTests : AcceptanceTest() {

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
    fun `AI 번역 기능을 사용`() {
        Given {
            body(AssignPlanRequest(planAId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        }

        Given {
            body(FeatureUsageRequest(1,1,1500))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.OK.value())
            body("success", equalTo(true))
            body("remainingCredit", equalTo(4980))
        }
    }

    @Test
    fun `AI 초안 기능을 사용`() {
        Given {
            body(AssignPlanRequest(planBId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        }

        Given {
            body(FeatureUsageRequest(1,4,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.OK.value())
            body("success", equalTo(true))
            body("remainingCredit", equalTo(4950))
        }
    }

    @Test
    fun `예외 케이스 확인`() {
        // 없는 회사 조회 시
        Given {
            body(FeatureUsageRequest(10,4,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.NOT_FOUND_COMPANY))
        }

        // 없는 기능 조회 시
        Given {
            body(FeatureUsageRequest(1,10,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.NOT_FOUND_FEATURE))
        }

        // 회사와 연결되어 있는 요금제가 없을 시
        Given {
            body(FeatureUsageRequest(1,1,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.COMPANY_IS_NOT_MAPPING_PLAN))
        }

        // 요금제 연결
        Given {
            body(AssignPlanRequest(planBId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/companies/{id}/plan", 1L)
        }

        // 요금제에 사용할 기능이 없을 시
        Given {
            body(FeatureUsageRequest(1,1,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", CoreMatchers.equalTo(ErrorMessage.NOT_INCLUDED_FEATURE))
        }
    }
}