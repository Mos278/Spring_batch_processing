package com.spring.spring_batch.Mapper

import com.spring.spring_batch.Entity.Data_Batch_Source
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
@Component
class Reader_Mapper : RowMapper<Data_Batch_Source> {

    companion object{
        open val batch_code : String = "batch_code"
        open val batch_name : String = "batch_name"
    }


    override fun mapRow(rs: ResultSet, rowNum: Int): Data_Batch_Source {
        var item = Data_Batch_Source()
        item.batch_code = rs.getLong(batch_code)
        item.batch_name = rs.getString(batch_name)
        return  item
    }
}