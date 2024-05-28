package com.spring.spring_batch.Component

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class JobCompletionNotificationListener(private val jdbcTemplate : JdbcTemplate) : JobExecutionListener {

    companion object {
        private val logger: Logger = LogManager.getLogger(JobCompletionNotificationListener::class.java)
    }

    override fun afterJob(jobExecution: JobExecution) {
        if(jobExecution.status == BatchStatus.COMPLETED){
            logger.info("Job success!!")

        }else if(jobExecution.status == BatchStatus.FAILED){
            logger.info("Not Success!!")
        }
    }

}