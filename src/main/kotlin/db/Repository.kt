package db

import db.connection.DataBaseConnection
import java.security.InvalidParameterException
import kotlin.reflect.KClass

open class Repository : RepositoryContract {

    val dataStore by lazy { DataBaseConnection().createDatastore() }

    override fun save(entity: Any) {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be a direct instance of Any class")
        else
            dataStore.save(entity)
    }

    override fun <T : Any> findAll(collection: KClass<T>): List<T> {
        if (collection == Any::class)
            throw InvalidParameterException("collection: KClass<T> can't be an instance of Any class")
        else
            return dataStore.find(collection.java).retrieveKnownFields().asList() ?: listOf()
    }

    override fun update(entity: Any): Boolean {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be an direct instance of Any class")
        return false
    }

    override fun getDatastore() = dataStore
}
