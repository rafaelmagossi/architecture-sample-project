package db

import org.mongodb.morphia.Datastore
import kotlin.reflect.KClass

interface RepositoryContract {

    fun save(entity: Any)

    fun <T : Any> findAll(collection: KClass<T>): List<T>

    fun update(entity: Any): Boolean

    fun getDatastore(): Datastore
}