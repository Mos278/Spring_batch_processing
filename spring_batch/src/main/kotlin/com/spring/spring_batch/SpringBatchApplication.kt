package com.spring.spring_batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import javax.batch.runtime.JobExecution

@SpringBootApplication
@EnableAutoConfiguration



class SpringBatchApplication

fun main(args: Array<String>) {
	runApplication<SpringBatchApplication>(*args)
}


