package com.task.teamreboott

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
import java.time.LocalDate
import kotlin.test.assertEquals

@DisplayName("통계 조회 기능")
class StatisticsAcceptanceTests : AcceptanceTest() {

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

        Given {
            body(AssignPlanRequest(planAId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            put("/api/companies/{id}/plan", 1L)
        }


        Given {
            body(FeatureUsageRequest(1,1,1500))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        }

        Given {
            body(FeatureUsageRequest(1,1,1500))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        }

        Given {
            body(AssignPlanRequest(planBId))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            put("/api/companies/{id}/plan", 1L)
        }

        Given {
            body(FeatureUsageRequest(1,4,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        }

        Given {
            body(FeatureUsageRequest(1,4,1))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/usage")
        }
    }

    @Test
    fun `통계 조회 기능`() {
        val response = Given {
            body(FeatureUsageStatRequest(1, LocalDate.now().minusDays(1), LocalDate.now()))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/statistics")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            `as`(FeatureUsageStatResponse::class.java)
        }

        assertEquals(2, response.featureStatistics.size)
        assertEquals("AI 번역", response.featureStatistics[0].featureName)
        assertEquals(3000, response.featureStatistics[0].totalUsage)
        assertEquals(40, response.featureStatistics[0].totalCreditUsed)

        assertEquals("AI 초안 작성", response.featureStatistics[1].featureName)
        assertEquals(2, response.featureStatistics[1].totalUsage)
        assertEquals(100, response.featureStatistics[1].totalCreditUsed)
    }

    @Test
    fun `통계 조회 시 유효하지 않는 파라미터`() {
        Given {
            body(FeatureUsageStatRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)))
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/statistics")
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
            body(CoreMatchers.containsString("현재 날짜보다 과거여야합니다"))
            body(CoreMatchers.containsString("현재 날짜 또는 과거 날짜여야합니다"))
        }
    }

}