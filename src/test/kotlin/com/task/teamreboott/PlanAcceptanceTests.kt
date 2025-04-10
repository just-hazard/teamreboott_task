package com.task.teamreboott

import com.task.teamreboott.common.ErrorMessage
import com.task.teamreboott.domain.Company
import com.task.teamreboott.domain.Feature
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.dto.CreatePlanRequest
import com.task.teamreboott.dto.CreatePlanResponse
import com.task.teamreboott.dto.FeatureLimitRequest
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.FeatureRepository
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.test.assertEquals

@DisplayName("요금제 기능")
class PlanAcceptanceTests : AcceptanceTest() {
    @Test
    fun `요금제 생성`() {
        val request = CreatePlanRequest(
            name = "A 요금제",
            features = listOf(
                FeatureLimitRequest(1, 3000, null, 20),
                FeatureLimitRequest(4, null, null, null)
            )
        )

        val response = Given {
            body(request)
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/plans")
        } Then {
            statusCode(HttpStatus.CREATED.value())
        } Extract {
            `as`(CreatePlanResponse::class.java)
        }

        assertEquals(request.name, response.name)
        assertEquals(2, response.features.size)
        assertEquals(1L, response.features[0].featureId)
        assertEquals("TEXT", response.features[0].type)
        assertEquals(3000, response.features[0].maxCustomUnitPerUse)
        assertEquals(null, response.features[0].maxCustomUsagePerMonth)
        assertEquals(20, response.features[0].customCreditCost)
    }

    @Test
    fun `요금제 생성 시 유효하지 않는 파라미터`() {
        val request = CreatePlanRequest(
            name = "",
            features = listOf(
                FeatureLimitRequest(0, 3000, null, 20),
            )
        )

        Given {
            body(request)
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/plans")
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
            body(containsString("요금제 이름은 필수입니다."))
            body(containsString("기능 ID는 0보다 커야 합니다."))
        }
    }

    @Test
    fun `요금제 생성 시 존재하지 않는 기능 추가 예외`() {
        val request = CreatePlanRequest(
            name = "A 요금제",
            features = listOf(
                FeatureLimitRequest(1, 3000, null, 20),
                FeatureLimitRequest(5, null, null, null)
            )
        )

        Given {
            body(request)
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            post("/api/plans")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
            body("message", equalTo(ErrorMessage.NON_EXIST_FEATURE))
        }
    }
}