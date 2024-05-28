package com.spring.spring_batch


import TestDatasource
import com.spring.spring_batch.Config.BatchConfig
import com.spring.spring_batch.Entity.Data_Batch_Source
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.batch.core.Step

import javax.sql.DataSource



@ExtendWith(MockitoExtension::class)
@DisplayName("Spring Batch with JUnit 5 + Mockito")
@SpringBatchTest
@SpringJUnitConfig(classes = [SpringBatchApplication::class , BatchConfig::class , TestDatasource::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SpringBatch_Test {



    @Autowired
    private lateinit var jobRepositoryTestUtils: JobRepositoryTestUtils

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils




    @Autowired
    private lateinit var jdbcTemplate :JdbcTemplate

    @Autowired
    private lateinit var dataSource: DataSource

    companion object {
        private val logger: Logger = LogManager.getLogger(BatchConfig::class.java)
    }



    @Test
    @Order(99)
    fun cleanUp() {
        logger.info("Remove_JobTest")
        jobRepositoryTestUtils.removeJobExecutions()
    }



    @Test
    @Order(1)
    fun setup(){
        logger.info("setup")
        jobRepositoryTestUtils.removeJobExecutions()
        jdbcTemplate.dataSource = dataSource
        var metadata = dataSource.getConnection().metaData
        Assertions.assertNotNull(metadata)
        logger.info("DataSource : " + metadata)
    }


    @Test
    @Order(2)
    fun InsertData(@Autowired job : Job){
        logger.info("InsertData")
        this.jobLauncherTestUtils.job = job
        this.jdbcTemplate.dataSource = dataSource

        this.jdbcTemplate.update("delete from Data_Batch_source")
        assertEquals(0, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_Source", Int::class.java))
        this.jdbcTemplate.update("INSERT into Data_Batch_Source VALUES("+ 1+ " , 'test')")
        val dataList: List<Data_Batch_Source> = jdbcTemplate.query("SELECT * FROM Data_Batch_Source") { rs, _ ->
            val data = Data_Batch_Source()
            data.batch_code = rs.getLong("batch_code")
            data.batch_name = rs.getString("batch_name")
            data
        }
        assertEquals(dataList.get(0).batch_name,"test")
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_Source", Int::class.java))
    }

    @Test
    @Order(3)
    fun TestStep1DeleteData(@Autowired step1 : Step){
        logger.info("Step1Test")
        this.jdbcTemplate.update("INSERT into Data_Batch_target VALUES("+ 1+ " , 'test','Data_Batch_Source')")
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_target", Int::class.java))
        var jobExecution = jobLauncherTestUtils.launchStep(step1.name)
        assertEquals("COMPLETED" , jobExecution.status.toString())
        assertEquals("COMPLETED" , jobExecution.exitStatus.exitCode)
        assertEquals(0, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_target", Int::class.java))

    }

    @Test
    @Order(4)
    fun TestStep2ETL(@Autowired step2 : Step){
        logger.info("Step2Test")
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_Source", Int::class.java))
        assertEquals(0, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_target", Int::class.java))
        var jobExecution = jobLauncherTestUtils.launchStep(step2.name)
        assertEquals("COMPLETED" , jobExecution.status.toString())
        assertEquals("COMPLETED" , jobExecution.exitStatus.exitCode)
        val datasourceList: List<Data_Batch_Source> = jdbcTemplate.query("SELECT * FROM Data_Batch_Source") { rs, _ ->
            val data = Data_Batch_Source()
            data.batch_code = rs.getLong("batch_code")
            data.batch_name = rs.getString("batch_name")
            data
        }

        val datatargetList: List<Data_Batch_Source> = jdbcTemplate.query("SELECT * FROM Data_Batch_target") { rs, _ ->
            val data = Data_Batch_Source()
            data.batch_code = rs.getLong("batch_code")
            data.batch_name = rs.getString("batch_name")
            data
        }

        assertEquals(datasourceList.get(0).batch_name,datatargetList.get(0).batch_name.toString().lowercase())
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_target", Int::class.java))
    }

    @Test
    @Order(5)
    fun TestJob(){
        logger.info("TestJOB")
        var jobExecution = jobLauncherTestUtils.launchJob()
        assertEquals("COMPLETED" , jobExecution.status.toString())
        assertEquals("COMPLETED" , jobExecution.exitStatus.exitCode)

        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_Source", Int::class.java))
        assertEquals(1, jdbcTemplate.queryForObject("SELECT count(*) FROM Data_Batch_target", Int::class.java))

        val datasourceList: List<Data_Batch_Source> = jdbcTemplate.query("SELECT * FROM Data_Batch_Source") { rs, _ ->
            val data = Data_Batch_Source()
            data.batch_code = rs.getLong("batch_code")
            data.batch_name = rs.getString("batch_name")
            data
        }

        val datatargetList: List<Data_Batch_Source> = jdbcTemplate.query("SELECT * FROM Data_Batch_target") { rs, _ ->
            val data = Data_Batch_Source()
            data.batch_code = rs.getLong("batch_code")
            data.batch_name = rs.getString("batch_name")
            data
        }
        assertEquals(datasourceList.get(0).batch_name,datatargetList.get(0).batch_name.toString().lowercase())

    }

}
