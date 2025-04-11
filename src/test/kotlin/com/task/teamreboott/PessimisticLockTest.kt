package com.task.teamreboott

import com.task.teamreboott.domain.Company
import com.task.teamreboott.repositories.CompanyRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PessimisticLockTest(
    @Autowired
    val companyRepository: CompanyRepository,
    @Autowired
    val transactionManager: PlatformTransactionManager,
) {
    @Test
    fun `비관적 락 테스트`() {
        val company = companyRepository.save(Company("A사", 100))
        val executor = Executors.newFixedThreadPool(2)

        val latch = CountDownLatch(1)

        executor.submit {
            runInTransaction {
                val company = companyRepository.findByIdWithLock(company.id)
                company.get().credit -= 10
                companyRepository.saveAndFlush(company.get())
                println("Thread A: 락 획득")
                latch.countDown() // Thread B 시작하게
                Thread.sleep(3000) // 락을 오래 점유
                println("Thread A: 커밋")
            }
        }

        executor.submit {
            latch.await() // Thread A가 락 잡을 때까지 대기
            runInTransaction {
                println("Thread B: 트랜잭션 시작")
                val start = System.currentTimeMillis()
                val company = companyRepository.findByIdWithLock(company.id)
                company.get().credit -= 10
                companyRepository.saveAndFlush(company.get())
                val end = System.currentTimeMillis()
                println("Thread B: 락 획득까지 ${end - start}ms")
            }
        }

        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
    }

    fun runInTransaction(block: () -> Unit) {
        val txDef = DefaultTransactionDefinition()
        val status = transactionManager.getTransaction(txDef)
        try {
            block()
            transactionManager.commit(status)
        } catch (ex: Exception) {
            transactionManager.rollback(status)
            throw ex
        }
    }
}