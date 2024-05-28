package com.spring.spring_batch.Config

import com.spring.spring_batch.Component.*
import com.spring.spring_batch.Entity.Data_Batch_Source
import com.spring.spring_batch.Entity.Data_Batch_Target
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.job.flow.support.SimpleFlow
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource


@Configuration
class BatchConfig{

    companion object {
        private val logger: Logger = LogManager.getLogger(BatchConfig::class.java)
    }

    @Autowired
    private lateinit var dataSource  : DataSource





    @Bean
    fun run(job: Job?, jobLauncher: JobLauncher): JobExecution? { //RUN
        var jobExecution: JobExecution? = null
        try {
            val jobParameters = JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters()

            jobExecution = jobLauncher.run(job!!, jobParameters)
            logger.info("Exit Status : " + jobExecution.status)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jobExecution
    }

    @Bean(name = arrayOf("step1"))
    fun step1(jobRepository: JobRepository
        ,taskletResetDB: TaskletResetDB) : Step{
        return StepBuilder("step1(ResetDB)" , jobRepository).tasklet(taskletResetDB,transactionManager()).build()
    }


    @Bean(name = arrayOf("step2"))
    fun step2(jobRepository: JobRepository
            , taskExecutor: TaskExecutor
              ,itemReader: ItemReader
              ,itemProcess: ItemProcess
              ,itemWriter: ItemWriter

    ): Step {
        return  StepBuilder("Step2(ETL)",jobRepository).chunk<Data_Batch_Source,Data_Batch_Target>(10,transactionManager())
            .reader(itemReader.Reader())
            .processor(itemProcess)
            .writer(itemWriter.Writer())
            .taskExecutor(SimpletaskExecutor())
            .build()
    }


    @Bean(name = arrayOf("flow"))
    fun flow(step1: Step , step2 : Step) : Flow{
        return FlowBuilder<SimpleFlow>("Flow")
            .start(step1)
            .on("FAILED").fail()
            .next(step2)
            .on("FAILED").fail()
            .build()
    }

@Bean
    fun Job1( jobRepository: JobRepository ,flow: Flow , listener : JobCompletionNotificationListener) : Job {
        return JobBuilder("Job1",jobRepository)
            .listener(listener)
            .start(flow)
            .end()
            .build()
    }

    @Bean
    fun transactionManager() : PlatformTransactionManager{
        var transection = DataSourceTransactionManager(dataSource)

        return transection
    }

    @Bean
    fun SimpletaskExecutor() : TaskExecutor{
        var executor = SimpleAsyncTaskExecutor()
        executor.concurrencyLimit = 10 //maximum number of parallel task
        return executor
    }

//    @Bean
//    fun PooltaskExecutor() : ThreadPoolTaskExecutor{
//        var executor = ThreadPoolTaskExecutor()
//        executor.corePoolSize = 5
//        executor.maxPoolSize = 10
//        executor.queueCapacity = 5
//        executor.setThreadNamePrefix("thread Executor")
//        executor.initialize()
//        return executor
//    }


}


//https://docs.spring.io/spring-batch/reference/appendix.html