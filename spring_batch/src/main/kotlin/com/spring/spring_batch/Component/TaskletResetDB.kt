package com.spring.spring_batch.Component

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class TaskletResetDB : Tasklet {

    @Autowired
    private lateinit var dataSource: DataSource

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        var jdbc = JdbcTemplate()
        jdbc.dataSource = dataSource
        jdbc.execute("DELETE FROM public.data_batch_target")
        return  RepeatStatus.FINISHED
    }

}