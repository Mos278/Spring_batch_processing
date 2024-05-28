package com.spring.spring_batch.Component

import com.spring.spring_batch.Entity.Data_Batch_Source
import com.spring.spring_batch.Mapper.Reader_Mapper
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.batch.support.DatabaseType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class ItemReader {

    @Autowired
    private lateinit var dataSource: DataSource


    @Bean
    @StepScope
    fun Reader() : JdbcPagingItemReader<Data_Batch_Source>{
        var reader =  JdbcPagingItemReaderBuilder<Data_Batch_Source>()
            .name("PagingReader")
            .dataSource(dataSource)
            .rowMapper(Reader_Mapper())
            .queryProvider(query())
            .pageSize(100)
            .fetchSize(100)
            .build()

        reader.afterPropertiesSet()
        return reader
    }


    //"SELECT batch_code , batch_name FROM data_batch_Source ORDER BY batch_code"
    fun query(): PagingQueryProvider{
        var query = SqlPagingQueryProviderFactoryBean()
        query.setDataSource(dataSource)
        query.setDatabaseType(DatabaseType.POSTGRES.toString())//TODO CHANG NAME DB
        query.setSelectClause("SELECT batch_code , batch_name")
        query.setFromClause("FROM data_batch_Source")
        val sortKeys = mapOf("batch_code" to Order.ASCENDING)
        query.setSortKeys(sortKeys)
        return  query.`object`
    }
}