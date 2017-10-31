package db

import db.connection.DataBaseConnection
import java.security.InvalidParameterException

class Repository {

    val dataStore by lazy { DataBaseConnection().createDatastore() }

    fun save(entity: Any) {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be a direct instance of Any class")
        else
            dataStore.save(entity)
    }
}

