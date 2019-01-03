package com.exoscale.boot.zerodowntime

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
class Person(@Id @GeneratedValue(strategy = IDENTITY) var id: Long = -1,
             val name: String = "",
             val addressLine1: String = "",
             val addressLine2: String? = null,
             val city: String = "",
             val zipCode: String = "")

@Entity
class Address(@Id @GeneratedValue(strategy = IDENTITY) var id: Long = -1,
              val addressLine1: String,
              val addressLine2: String? = null,
              val city: String,
              val zipCode: String,
              var personId: Long? = null)