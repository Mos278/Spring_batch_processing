//package com.spring.spring_batch.Test
//
//import com.spring.spring_batch.Entity.Data_Batch_Source
//import com.spring.spring_batch.Repository.SourceRepository
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.stereotype.Component
//import org.springframework.stereotype.Service
//
//@Component
//class InsertData {
//
//    @Autowired
//    private  lateinit var sourceRepository: SourceRepository
//
//    fun genData(){
//        for (batch_code in 0..100000){
//            var item = Data_Batch_Source()
//            item.batch_code = batch_code.toLong()
//            item.batch_name = "test"
//            sourceRepository.save(item)
//
//        }
//    }
//}