package com.task.teamreboott

import com.task.teamreboott.domain.Company
import com.task.teamreboott.domain.Feature
import com.task.teamreboott.domain.enums.FeatureLimitType
import com.task.teamreboott.repositories.CompanyRepository
import com.task.teamreboott.repositories.FeatureRepository
import com.task.teamreboott.util.DatabaseCleanup
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    @Autowired
    lateinit var featureRepository: FeatureRepository
    @Autowired
    lateinit var companyRepository: CompanyRepository

    @LocalServerPort
    var port = 0

    @Autowired
    private val databaseCleanup: DatabaseCleanup? = null
    @BeforeEach
    fun beforeEach() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
            databaseCleanup!!.afterPropertiesSet()
        }
        databaseCleanup!!.execute()

        companyRepository.saveAll(
            listOf(
                Company("A사", 5000),
                Company("B사", 10000),
                Company("C사", 10000)
            )
        )

        featureRepository.saveAll(
            listOf(
                Feature("AI 번역", 2000, 10, FeatureLimitType.TEXT),
                Feature("AI 교정", 1000, 10, FeatureLimitType.TEXT),
                Feature("AI 뉘앙스 조절", 1500, 20, FeatureLimitType.TEXT),
                Feature("AI 초안 작성", 200, 50, FeatureLimitType.MONTH)
            )
        )
    }
}
