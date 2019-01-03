package com.exoscale.boot.zerodowntime

import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.*
import org.hibernate.internal.SessionFactoryImpl
import org.springframework.stereotype.Component
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
class Person(@Id @GeneratedValue(strategy = IDENTITY) var id: Long = -1,
             val name: String = "") {

    @OneToOne(mappedBy = "person", cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    val address: Address = Address()

    init {
        address.person = this
    }

    internal var addressLine1: String? = null
    internal var addressLine2: String? = null
    internal var city: String? = null
    internal var zipCode: String? = null
}

@Entity
class Address(@Id @GeneratedValue(strategy = IDENTITY) var id: Long? = null,
              var addressLine1: String = "",
              var addressLine2: String? = null,
              var city: String = "",
              var zipCode: String = "") {
    @OneToOne
    @JoinColumn(name = "person_id")
    lateinit var person: Person
}

@Component
class WriteAddressListener(emf: EntityManagerFactory) : PreInsertEventListener {

    init {
        val sessionFactory = emf.unwrap(SessionFactoryImpl::class.java)
        val registry = sessionFactory.serviceRegistry.getService(EventListenerRegistry::class.java)
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(this)
    }

    override fun onPreInsert(event: PreInsertEvent): Boolean {
        val entity = event.entity
        if (entity is Address) {
            entity.person.addressLine1 = entity.addressLine1
            entity.person.addressLine2 = entity.addressLine2
            entity.person.city = entity.city
            entity.person.zipCode = entity.zipCode
        }
        return false
    }
}
