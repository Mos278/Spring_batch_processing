package com.spring.spring_batch.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "Data_Batch_Target")
class Data_Batch_Target {

    @Id
    @GeneratedValue
    @Column(name = "batch_code", nullable = false)
    open var batch_code: Long? = null


    @Column(name = "batch_name", nullable = false)
    open  var batch_name : String? = null


    @Column(name = "batch_source")
    open var batch_source : String? = null

}