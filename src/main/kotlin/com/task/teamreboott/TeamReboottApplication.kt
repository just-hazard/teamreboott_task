package com.task.teamreboott

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TeamReboottApplication

fun main(args: Array<String>) {
    runApplication<TeamReboottApplication>(*args)
}
