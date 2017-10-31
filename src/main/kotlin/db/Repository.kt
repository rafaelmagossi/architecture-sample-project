package db

import db.connection.DataBaseConnection
import java.security.InvalidParameterException

open class Repository : RepositoryContract {

    val dataStore by lazy { DataBaseConnection().createDatastore() }

    override fun save(entity: Any) {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be a direct instance of Any class")
        else
            dataStore.save(entity)
    }

    override fun update(entity: Any): Boolean {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be an direct instance of Any class")
        return false
    }

    override fun getDatastore() = dataStore
}
