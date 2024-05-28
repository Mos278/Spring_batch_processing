package com.spring.spring_batch.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "Data_Batch_Source")
class Data_Batch_Source {
    @Id
    @GeneratedValue
    @Column(name = "batch_code", nullable = false)
    open var batch_code: Long? = null


    @Column(name = "batch_name", nullable = false)
    open  var batch_name : String? = null
}