package com.spring.spring_batch.Component

import com.spring.spring_batch.Entity.Data_Batch_Target
import com.spring.spring_batch.Mapper.Writer_Mapper
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.ItemPreparedStatementSetter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class ItemWriter {

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean
    @StepScope
    fun Writer(): JdbcBatchItemWriter<Data_Batch_Target> {
        return JdbcBatchItemWriterBuilder<Data_Batch_Target>()
            .sql("INSERT INTO data_batch_Target (batch_code, batch_name, batch_source) VALUES (?,?,?) ON CONFLICT (batch_code) DO UPDATE SET batch_name = EXCLUDED.batch_name, batch_source = EXCLUDED.batch_source")
            .dataSource(dataSource)
            .itemPreparedStatementSetter(itemPreparedStatementSetter())
            .beanMapped()
            .build()
    }

    fun itemPreparedStatementSetter():ItemPreparedStatementSetter<Data_Batch_Target>{
        return Writer_Mapper()
    }

}