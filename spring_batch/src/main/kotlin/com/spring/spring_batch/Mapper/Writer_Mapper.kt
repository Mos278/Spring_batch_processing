package com.spring.spring_batch.Mapper

import com.spring.spring_batch.Entity.Data_Batch_Target
import org.springframework.batch.item.database.ItemPreparedStatementSetter
import org.springframework.stereotype.Component
import java.sql.PreparedStatement


@Component
class Writer_Mapper : ItemPreparedStatementSetter<Data_Batch_Target> {
    override fun setValues(item: Data_Batch_Target, ps: PreparedStatement) {
        ps.setLong(1,item.batch_code!!)
        ps.setString(2,item.batch_name)
        ps.setString(3,item.batch_source)
    }
}