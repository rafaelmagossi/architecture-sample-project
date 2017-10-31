package db

import org.mongodb.morphia.Datastore

interface RepositoryContract {

    fun save(entity: Any)

    fun update(entity: Any): Boolean

    fun getDatastore(): Datastore
}