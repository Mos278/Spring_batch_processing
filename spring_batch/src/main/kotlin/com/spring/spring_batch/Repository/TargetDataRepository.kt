package com.spring.spring_batch.Repository

import com.spring.spring_batch.Entity.Data_Batch_Source
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TargetDataRepository : JpaRepository<Data_Batch_Source,Long> {
}