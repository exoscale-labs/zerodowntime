package com.exoscale.boot.zerodowntime

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY
import org.springframework.stereotype.Component
import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.*
import org.hibernate.internal.SessionFactoryImpl
import org.hibernate.persister.entity.EntityPersister

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

val Person.address: Address
    get() = Address(addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        zipCode = zipCode,
        personId = id)

@Component
class WriteAddressListener(emf: EntityManagerFactory) : PostInsertEventListener {

    private val sessionFactory = emf.unwrap(SessionFactoryImpl::class.java)

    init {
        val registry = sessionFactory.serviceRegistry.getService(EventListenerRegistry::class.java)
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(this)
    }

    override fun onPostInsert(event: PostInsertEvent) {
        val entity = event.entity
        if (entity is Person) {
            val session = sessionFactory.openSession()
            session.save(entity.address)
        }
    }

    override fun requiresPostCommitHanding(persister: EntityPersister) = false
}