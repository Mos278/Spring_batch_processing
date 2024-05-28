package com.spring.spring_batch.Component

import com.spring.spring_batch.Entity.Data_Batch_Source
import com.spring.spring_batch.Entity.Data_Batch_Target
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class ItemProcess : ItemProcessor<Data_Batch_Source,Data_Batch_Target>{

    override fun process(item: Data_Batch_Source): Data_Batch_Target {
        val transform  = Data_Batch_Target()
        transform.batch_code = item.batch_code!!
        transform.batch_name = item.batch_name!!.uppercase()
        transform.batch_source = "Data_Batch_Source"
        return  transform
    }
}